package com.twenty.inhub.boundedContext.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -533067125L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final StringPath board = createString("board");

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    public final ListPath<com.twenty.inhub.boundedContext.comment.entity.Comment, com.twenty.inhub.boundedContext.comment.entity.QComment> comments = this.<com.twenty.inhub.boundedContext.comment.entity.Comment, com.twenty.inhub.boundedContext.comment.entity.QComment>createList("comments", com.twenty.inhub.boundedContext.comment.entity.Comment.class, com.twenty.inhub.boundedContext.comment.entity.QComment.class, PathInits.DIRECT2);

    public final com.twenty.inhub.boundedContext.community.entity.QCommunity community;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    public final StringPath fileName = createString("fileName");

    public final ListPath<String, StringPath> fileNames = this.<String, StringPath>createList("fileNames", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath fileUrl = createString("fileUrl");

    public final ListPath<String, StringPath> fileUrls = this.<String, StringPath>createList("fileUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.twenty.inhub.boundedContext.member.entity.QMember member;

    public final NumberPath<Integer> postHits = createNumber("postHits", Integer.class);

    public final StringPath title = createString("title");

    public final SetPath<String, StringPath> viewed = this.<String, StringPath>createSet("viewed", String.class, StringPath.class, PathInits.DIRECT2);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.community = inits.isInitialized("community") ? new com.twenty.inhub.boundedContext.community.entity.QCommunity(forProperty("community")) : null;
        this.member = inits.isInitialized("member") ? new com.twenty.inhub.boundedContext.member.entity.QMember(forProperty("member")) : null;
    }

}

