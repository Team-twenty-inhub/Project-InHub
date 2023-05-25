package com.twenty.inhub.boundedContext.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -880971625L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final ListPath<com.twenty.inhub.boundedContext.Answer.entity.Answer, com.twenty.inhub.boundedContext.Answer.entity.QAnswer> answers = this.<com.twenty.inhub.boundedContext.Answer.entity.Answer, com.twenty.inhub.boundedContext.Answer.entity.QAnswer>createList("answers", com.twenty.inhub.boundedContext.Answer.entity.Answer.class, com.twenty.inhub.boundedContext.Answer.entity.QAnswer.class, PathInits.DIRECT2);

    public final com.twenty.inhub.boundedContext.category.QCategory category;

    public final StringPath choice = createString("choice");

    public final StringPath content = createString("content");

    public final StringPath difficulty = createString("difficulty");

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    public final StringPath name = createString("name");

    public final StringPath tag = createString("tag");

    public final EnumPath<QuestionType> type = createEnum("type", QuestionType.class);

    public final ListPath<com.twenty.inhub.boundedContext.underline.Underline, com.twenty.inhub.boundedContext.underline.QUnderline> underlines = this.<com.twenty.inhub.boundedContext.underline.Underline, com.twenty.inhub.boundedContext.underline.QUnderline>createList("underlines", com.twenty.inhub.boundedContext.underline.Underline.class, com.twenty.inhub.boundedContext.underline.QUnderline.class, PathInits.DIRECT2);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.twenty.inhub.boundedContext.category.QCategory(forProperty("category")) : null;
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
    }

}

