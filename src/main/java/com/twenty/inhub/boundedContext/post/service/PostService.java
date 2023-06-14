package com.twenty.inhub.boundedContext.post.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.repository.CommentRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.repository.PostRepository;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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
    private static final int PAGE_SIZE = 10;


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

    public void updatePost(PostDto postDto, Member member) {
        Post post = postRepository.findById(postDto.getId()).orElse(null);
        if (post != null && post.isCreatedBy(member)) {
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            postRepository.save(post);
        }
        else {
            throw new IllegalArgumentException("이 게시글을 수정할 권한이 없습니다.");
        }
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id, Member member) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        if (post.isCreatedBy(member) || member.getRole() == MemberRole.ADMIN) {
            // 댓글 삭제
            List<Comment> comments = post.getComments();
            commentRepository.deleteAll(comments);

            // 회원 엔티티에서 게시글 제거
            Member postAuthor = post.getMember();
            postAuthor.getPosts().remove(post);
            memberRepository.save(postAuthor);

            // 게시글 삭제
            postRepository.delete(post);
        }
        else {
            throw new IllegalArgumentException("이 게시글을 삭제할 권한이 없습니다.");
        }
    }

    public Page<Post> getList(String board, int page, String kw, String searchType) {
        Specification<Post> spec = Specification.where(null);

        if (!StringUtils.isEmpty(board)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("board"), board));
        }

        if (!StringUtils.isEmpty(kw) && !StringUtils.isEmpty(searchType)) {
            switch (searchType) {
                case "title":
                    spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("title"), "%" + kw + "%"));
                    break;
                case "titleAndContent":
                    spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                            criteriaBuilder.like(root.get("title"), "%" + kw + "%"),
                            criteriaBuilder.like(root.get("content"), "%" + kw + "%")
                    ));
                    break;
                case "content":
                    spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("content"), "%" + kw + "%"));
                    break;
                case "author":
                    spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.join("member").get("nickname"), "%" + kw + "%"));
                    break;
                default:
                    break;
            }
        }

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdTime").descending());
        return postRepository.findAll(spec, pageable);
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

    public List<Post> findByMemberId(Long memberId) {
        return postRepository.findByMemberId(memberId);
    }
}
