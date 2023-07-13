package com.twenty.inhub.boundedContext.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookTag is a Querydsl query type for BookTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookTag extends EntityPathBase<BookTag> {

    private static final long serialVersionUID = 761584829L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookTag bookTag = new QBookTag("bookTag");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath tag = createString("tag");

    public QBookTag(String variable) {
        this(BookTag.class, forVariable(variable), INITS);
    }

    public QBookTag(Path<? extends BookTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookTag(PathMetadata metadata, PathInits inits) {
        this(BookTag.class, metadata, inits);
    }

    public QBookTag(Class<? extends BookTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book"), inits.get("book")) : null;
    }

}

