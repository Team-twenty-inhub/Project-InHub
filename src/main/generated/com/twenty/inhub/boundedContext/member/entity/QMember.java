package com.twenty.inhub.boundedContext.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1242104129L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.twenty.inhub.boundedContext.Answer.entity.Answer, com.twenty.inhub.boundedContext.Answer.entity.QAnswer> answers = this.<com.twenty.inhub.boundedContext.Answer.entity.Answer, com.twenty.inhub.boundedContext.Answer.entity.QAnswer>createList("answers", com.twenty.inhub.boundedContext.Answer.entity.Answer.class, com.twenty.inhub.boundedContext.Answer.entity.QAnswer.class, PathInits.DIRECT2);

    public final ListPath<com.twenty.inhub.boundedContext.comment.Comment, com.twenty.inhub.boundedContext.comment.QComment> comments = this.<com.twenty.inhub.boundedContext.comment.Comment, com.twenty.inhub.boundedContext.comment.QComment>createList("comments", com.twenty.inhub.boundedContext.comment.Comment.class, com.twenty.inhub.boundedContext.comment.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImg = createString("profileImg");

    public final StringPath providerTypeCode = createString("providerTypeCode");

    public final ListPath<com.twenty.inhub.boundedContext.question.entity.Question, com.twenty.inhub.boundedContext.question.entity.QQuestion> questions = this.<com.twenty.inhub.boundedContext.question.entity.Question, com.twenty.inhub.boundedContext.question.entity.QQuestion>createList("questions", com.twenty.inhub.boundedContext.question.entity.Question.class, com.twenty.inhub.boundedContext.question.entity.QQuestion.class, PathInits.DIRECT2);

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    public final StringPath token = createString("token");

    public final ListPath<com.twenty.inhub.boundedContext.underline.Underline, com.twenty.inhub.boundedContext.underline.QUnderline> underlines = this.<com.twenty.inhub.boundedContext.underline.Underline, com.twenty.inhub.boundedContext.underline.QUnderline>createList("underlines", com.twenty.inhub.boundedContext.underline.Underline.class, com.twenty.inhub.boundedContext.underline.QUnderline.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

