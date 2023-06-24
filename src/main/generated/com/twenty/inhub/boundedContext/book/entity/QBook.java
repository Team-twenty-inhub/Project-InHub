package com.twenty.inhub.boundedContext.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -1608046179L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final StringPath about = createString("about");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath img = createString("img");

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> playCount = createNumber("playCount", Integer.class);

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    public final NumberPath<Integer> recommend = createNumber("recommend", Integer.class);

    public final ListPath<com.twenty.inhub.boundedContext.question.entity.Tag, com.twenty.inhub.boundedContext.question.entity.QTag> tagList = this.<com.twenty.inhub.boundedContext.question.entity.Tag, com.twenty.inhub.boundedContext.question.entity.QTag>createList("tagList", com.twenty.inhub.boundedContext.question.entity.Tag.class, com.twenty.inhub.boundedContext.question.entity.QTag.class, PathInits.DIRECT2);

    public final ListPath<com.twenty.inhub.boundedContext.underline.Underline, com.twenty.inhub.boundedContext.underline.QUnderline> underlines = this.<com.twenty.inhub.boundedContext.underline.Underline, com.twenty.inhub.boundedContext.underline.QUnderline>createList("underlines", com.twenty.inhub.boundedContext.underline.Underline.class, com.twenty.inhub.boundedContext.underline.QUnderline.class, PathInits.DIRECT2);

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
    }

}

