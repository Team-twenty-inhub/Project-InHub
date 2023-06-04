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

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final com.twenty.inhub.boundedContext.answer.entity.QAnswerCheck answerCheck;

    public final ListPath<com.twenty.inhub.boundedContext.answer.entity.Answer, com.twenty.inhub.boundedContext.answer.entity.QAnswer> answers = this.<com.twenty.inhub.boundedContext.answer.entity.Answer, com.twenty.inhub.boundedContext.answer.entity.QAnswer>createList("answers", com.twenty.inhub.boundedContext.answer.entity.Answer.class, com.twenty.inhub.boundedContext.answer.entity.QAnswer.class, PathInits.DIRECT2);

    public final com.twenty.inhub.boundedContext.category.QCategory category;

    public final ListPath<Choice, QChoice> choiceList = this.<Choice, QChoice>createList("choiceList", Choice.class, QChoice.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Integer> difficulty = createNumber("difficulty", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath name = createString("name");

    public final StringPath oldTag = createString("oldTag");

    public final ListPath<Tag, QTag> tags = this.<Tag, QTag>createList("tags", Tag.class, QTag.class, PathInits.DIRECT2);

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
        this.answerCheck = inits.isInitialized("answerCheck") ? new com.twenty.inhub.boundedContext.answer.entity.QAnswerCheck(forProperty("answerCheck"), inits.get("answerCheck")) : null;
        this.category = inits.isInitialized("category") ? new com.twenty.inhub.boundedContext.category.QCategory(forProperty("category")) : null;
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
    }

}

