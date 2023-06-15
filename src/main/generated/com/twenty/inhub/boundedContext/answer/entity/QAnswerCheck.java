package com.twenty.inhub.boundedContext.answer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswerCheck is a Querydsl query type for AnswerCheck
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswerCheck extends EntityPathBase<AnswerCheck> {

    private static final long serialVersionUID = 1466074945L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswerCheck answerCheck = new QAnswerCheck("answerCheck");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.twenty.inhub.boundedContext.question.entity.QQuestion question;

    public final SetPath<com.twenty.inhub.boundedContext.member.entity.Member, com.twenty.inhub.boundedContext.member.entity.QMember> voter = this.<com.twenty.inhub.boundedContext.member.entity.Member, com.twenty.inhub.boundedContext.member.entity.QMember>createSet("voter", com.twenty.inhub.boundedContext.member.entity.Member.class, com.twenty.inhub.boundedContext.member.entity.QMember.class, PathInits.DIRECT2);

    public final StringPath word1 = createString("word1");

    public final StringPath word2 = createString("word2");

    public final StringPath word3 = createString("word3");

    public QAnswerCheck(String variable) {
        this(AnswerCheck.class, forVariable(variable), INITS);
    }

    public QAnswerCheck(Path<? extends AnswerCheck> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswerCheck(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswerCheck(PathMetadata metadata, PathInits inits) {
        this(AnswerCheck.class, metadata, inits);
    }

    public QAnswerCheck(Class<? extends AnswerCheck> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new com.twenty.inhub.boundedContext.question.entity.QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

