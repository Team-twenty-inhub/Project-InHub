package com.twenty.inhub.boundedContext.post.entity;

import com.twenty.inhub.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String image;

    private String file;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setTitle(String title) {
    }

    public void setContent(String content) {
    }

    public void setImage(String image) {
    }

    public void setFile(String file) {
    }
}
