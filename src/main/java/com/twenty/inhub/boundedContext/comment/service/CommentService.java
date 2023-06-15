package com.twenty.inhub.boundedContext.comment.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.comment.dto.CommentDto;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.repository.CommentRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.member.repository.MemberRepository;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public RsData createComment(CommentDto commentDto, Member member, Post post) {
        Comment comment = Comment.toSaveEntity(commentDto, member, post);
        Comment savedComment = commentRepository.save(comment);
        CommentDto savedCommentDto = CommentDto.toCommentDto(savedComment);

        updateCommentCount(post); // 댓글 수 업데이트
        return RsData.of("S-60", "댓글이 생성되었습니다.", savedCommentDto);
    }

    public CommentDto getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        return CommentDto.toCommentDto(comment);
    }

    public RsData updateComment(Long id, String content, Member member) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null && comment.isCreatedBy(member)) {
            comment.setContent(content);
            Comment updatedComment = commentRepository.save(comment);
            Long postId = updatedComment.getPost().getId();
            return RsData.of("S-61", "댓글이 수정되었습니다.", postId);
        } else {
            return RsData.of("F-61", "이 댓글을 수정할 권한이 없습니다.");
        }
    }

    public RsData deleteComment(Long id, Member member) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

        if (comment.isCreatedBy(member) || member.getRole() == MemberRole.ADMIN) {
            Member commentAuthor = comment.getMember();
            commentAuthor.getComments().remove(comment);
            memberRepository.save(commentAuthor);

            Post post = comment.getPost();
            post.getComments().remove(comment);
            postRepository.save(post); // Post 엔티티 저장

            updateCommentCount(post);
            commentRepository.deleteById(id); // 댓글 삭제
            updateCommentCount(post);

            return RsData.of("S-62", "댓글이 삭제되었습니다.");
        }
        else {
            return RsData.of("F-62", "이 댓글을 삭제할 권한이 없습니다.");
        }
    }

    private void updateCommentCount(Post post) {
        int commentCount = commentRepository.countByPost(post);
        post.setCommentCount(commentCount);
        postRepository.save(post);
    }

    public List<Comment> findByMemberId(Long memberId) {
        return commentRepository.findByMemberId(memberId);
    }

    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPost(post);
    }
}
