package com.twenty.inhub.boundedContext.underline;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUnderline is a Querydsl query type for Underline
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUnderline extends EntityPathBase<Underline> {

    private static final long serialVersionUID = 353538140L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUnderline underline = new QUnderline("underline");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final StringPath about = createString("about");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.twenty.inhub.boundedContext.question.entity.QQuestion question;

    public QUnderline(String variable) {
        this(Underline.class, forVariable(variable), INITS);
    }

    public QUnderline(Path<? extends Underline> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUnderline(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUnderline(PathMetadata metadata, PathInits inits) {
        this(Underline.class, metadata, inits);
    }

    public QUnderline(Class<? extends Underline> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new com.twenty.inhub.boundedContext.question.entity.QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

