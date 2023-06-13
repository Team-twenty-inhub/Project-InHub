package com.twenty.inhub.boundedContext.comment.service;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.comment.dto.CommentDto;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.repository.CommentRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    public RsData createComment(CommentDto commentDto, Member member, Post post) {
        Comment comment = Comment.toSaveEntity(commentDto, member, post);
        Comment savedComment = commentRepository.save(comment);
        CommentDto savedCommentDto = CommentDto.toCommentDto(savedComment);
        return RsData.of("S-60", "댓글이 생성되었습니다.", savedCommentDto);
    }

    public CommentDto getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        return CommentDto.toCommentDto(comment);
    }

    public RsData updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElse(null);
        if (comment != null) {
            comment.setContent(commentDto.getContent());
            commentRepository.save(comment);
            return RsData.of("S-61", "댓글이 수정되었습니다.", commentDto);
        }
        return RsData.of("F-61", "댓글 수정에 실패했습니다.");
    }

    public RsData deleteComment(Long id) {
        commentRepository.deleteById(id);
        return RsData.of("S-62", "댓글이 삭제되었습니다.");
    }
}
