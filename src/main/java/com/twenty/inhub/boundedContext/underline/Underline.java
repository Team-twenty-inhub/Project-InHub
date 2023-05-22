package com.twenty.inhub.boundedContext.underline;

import com.twenty.inhub.boundedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Underline {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

//    @ManyToOne(fetch = LAZY)
//    private Member member;

    @ManyToOne(fetch = LAZY)
    private Question question;
}
