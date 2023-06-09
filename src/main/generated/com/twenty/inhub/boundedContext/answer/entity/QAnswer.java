package com.twenty.inhub.boundedContext.answer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswer is a Querydsl query type for Answer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswer extends EntityPathBase<Answer> {

    private static final long serialVersionUID = 985700871L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswer answer = new QAnswer("answer");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath feedback = createString("feedback");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.twenty.inhub.boundedContext.question.entity.QQuestion question;

    public final StringPath result = createString("result");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final SetPath<com.twenty.inhub.boundedContext.member.entity.Member, com.twenty.inhub.boundedContext.member.entity.QMember> voter = this.<com.twenty.inhub.boundedContext.member.entity.Member, com.twenty.inhub.boundedContext.member.entity.QMember>createSet("voter", com.twenty.inhub.boundedContext.member.entity.Member.class, com.twenty.inhub.boundedContext.member.entity.QMember.class, PathInits.DIRECT2);

    public final StringPath word1 = createString("word1");

    public final StringPath word2 = createString("word2");

    public final StringPath word3 = createString("word3");

    public QAnswer(String variable) {
        this(Answer.class, forVariable(variable), INITS);
    }

    public QAnswer(Path<? extends Answer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswer(PathMetadata metadata, PathInits inits) {
        this(Answer.class, metadata, inits);
    }

    public QAnswer(Class<? extends Answer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new com.twenty.inhub.boundedContext.question.entity.QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

