package com.twenty.inhub.boundedContext.likeBook;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeBook is a Querydsl query type for LikeBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeBook extends EntityPathBase<LikeBook> {

    private static final long serialVersionUID = 829023520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeBook likeBook = new QLikeBook("likeBook");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final com.twenty.inhub.boundedContext.book.entity.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QLikeBook(String variable) {
        this(LikeBook.class, forVariable(variable), INITS);
    }

    public QLikeBook(Path<? extends LikeBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeBook(PathMetadata metadata, PathInits inits) {
        this(LikeBook.class, metadata, inits);
    }

    public QLikeBook(Class<? extends LikeBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.twenty.inhub.boundedContext.book.entity.QBook(forProperty("book"), inits.get("book")) : null;
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
    }

}

