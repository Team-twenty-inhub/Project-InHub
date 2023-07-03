package com.twenty.inhub.boundedContext.note;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNote is a Querydsl query type for Note
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNote extends EntityPathBase<Note> {

    private static final long serialVersionUID = -147861152L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNote note = new QNote("note");

    public final com.twenty.inhub.base.entity.QBaseEntity _super = new com.twenty.inhub.base.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isDeleteReceiver = createBoolean("isDeleteReceiver");

    public final BooleanPath isDeleteSender = createBoolean("isDeleteSender");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.twenty.inhub.boundedContext.member.entity.QMember receiver;

    public final com.twenty.inhub.boundedContext.member.entity.QMember sender;

    public final StringPath title = createString("title");

    public QNote(String variable) {
        this(Note.class, forVariable(variable), INITS);
    }

    public QNote(Path<? extends Note> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNote(PathMetadata metadata, PathInits inits) {
        this(Note.class, metadata, inits);
    }

    public QNote(Class<? extends Note> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("sender")) : null;
    }

}

