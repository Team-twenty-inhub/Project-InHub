package com.twenty.inhub.boundedContext.post.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.repository.CommentRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final List<PostDto> postDtoList = new ArrayList<>();

    public void createPost(PostDto postDto, Member member) {
        Post post = Post.toSaveEntity(postDto, member);
        post.setBoard(postDto.getBoard()); // 게시글의 board 필드 설정
        Post savedPost = postRepository.save(post);
        postDtoList.add(PostDto.toPostDto(savedPost));
    }

    public List<PostDto> findPost() {
        List<Post> posts = postRepository.findAllByOrderByCreatedTimeDesc();
        List<PostDto> postDtoList = new ArrayList<>();
        for (Post post : posts) {
            PostDto postDto = PostDto.toPostDto(post);
            postDto.setAuthorNickname(post.getAuthorNickname());
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    public void updatePost(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId()).orElse(null);
        if (post != null) {
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            postRepository.save(post);
        }
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        // 댓글 삭제
        List<Comment> comments = post.getComments();
        commentRepository.deleteAll(comments);

        // 회원 엔티티에서 게시글 제거
        Member member = post.getMember();
        member.getPosts().remove(post);
        memberRepository.save(member);

        // 게시글 삭제
        postRepository.deleteById(id);
    }

    public Page<Post> getList(String board, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdTime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if ("free".equals(board)) {
            return postRepository.findByBoard("free", pageable);
        } else if ("interview".equals(board)) {
            return postRepository.findByBoard("interview", pageable);
        } else if ("etc".equals(board)) {
            return postRepository.findByBoard("etc", pageable);
        }

        return postRepository.findAll(pageable);
    }

    public Post increasedHits(Long id, HttpSession session) {
        Post post = getPost(id);

        if (session != null) {

            String sessionId = session.getId();

            if (!post.getViewed().contains(sessionId)) {
                post.setPostHits(post.getPostHits() + 1);
                post.getViewed().add(sessionId);
                postRepository.save(post);
            }
        }
        return post;
    }
}
