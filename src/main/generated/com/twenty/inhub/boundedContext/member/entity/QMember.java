package com.twenty.inhub.boundedContext.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1242104129L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.twenty.inhub.boundedContext.answer.entity.Answer, com.twenty.inhub.boundedContext.answer.entity.QAnswer> answers = this.<com.twenty.inhub.boundedContext.answer.entity.Answer, com.twenty.inhub.boundedContext.answer.entity.QAnswer>createList("answers", com.twenty.inhub.boundedContext.answer.entity.Answer.class, com.twenty.inhub.boundedContext.answer.entity.QAnswer.class, PathInits.DIRECT2);

    public final ListPath<com.twenty.inhub.boundedContext.book.entity.Book, com.twenty.inhub.boundedContext.book.entity.QBook> books = this.<com.twenty.inhub.boundedContext.book.entity.Book, com.twenty.inhub.boundedContext.book.entity.QBook>createList("books", com.twenty.inhub.boundedContext.book.entity.Book.class, com.twenty.inhub.boundedContext.book.entity.QBook.class, PathInits.DIRECT2);

    public final ListPath<com.twenty.inhub.boundedContext.comment.entity.Comment, com.twenty.inhub.boundedContext.comment.entity.QComment> comments = this.<com.twenty.inhub.boundedContext.comment.entity.Comment, com.twenty.inhub.boundedContext.comment.entity.QComment>createList("comments", com.twenty.inhub.boundedContext.comment.entity.Comment.class, com.twenty.inhub.boundedContext.comment.entity.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final ListPath<com.twenty.inhub.boundedContext.device.Device, com.twenty.inhub.boundedContext.device.QDevice> devices = this.<com.twenty.inhub.boundedContext.device.Device, com.twenty.inhub.boundedContext.device.QDevice>createList("devices", com.twenty.inhub.boundedContext.device.Device.class, com.twenty.inhub.boundedContext.device.QDevice.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.twenty.inhub.boundedContext.likeBook.LikeBook, com.twenty.inhub.boundedContext.likeBook.QLikeBook> likeList = this.<com.twenty.inhub.boundedContext.likeBook.LikeBook, com.twenty.inhub.boundedContext.likeBook.QLikeBook>createList("likeList", com.twenty.inhub.boundedContext.likeBook.LikeBook.class, com.twenty.inhub.boundedContext.likeBook.QLikeBook.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final ListPath<com.twenty.inhub.boundedContext.post.entity.Post, com.twenty.inhub.boundedContext.post.entity.QPost> posts = this.<com.twenty.inhub.boundedContext.post.entity.Post, com.twenty.inhub.boundedContext.post.entity.QPost>createList("posts", com.twenty.inhub.boundedContext.post.entity.Post.class, com.twenty.inhub.boundedContext.post.entity.QPost.class, PathInits.DIRECT2);

    public final StringPath profileImg = createString("profileImg");

    public final StringPath providerTypeCode = createString("providerTypeCode");

    public final ListPath<com.twenty.inhub.boundedContext.question.entity.Question, com.twenty.inhub.boundedContext.question.entity.QQuestion> questions = this.<com.twenty.inhub.boundedContext.question.entity.Question, com.twenty.inhub.boundedContext.question.entity.QQuestion>createList("questions", com.twenty.inhub.boundedContext.question.entity.Question.class, com.twenty.inhub.boundedContext.question.entity.QQuestion.class, PathInits.DIRECT2);

    public final ListPath<com.twenty.inhub.boundedContext.note.entity.Note, com.twenty.inhub.boundedContext.note.entity.QNote> receiveList = this.<com.twenty.inhub.boundedContext.note.entity.Note, com.twenty.inhub.boundedContext.note.entity.QNote>createList("receiveList", com.twenty.inhub.boundedContext.note.entity.Note.class, com.twenty.inhub.boundedContext.note.entity.QNote.class, PathInits.DIRECT2);

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public final ListPath<com.twenty.inhub.boundedContext.note.entity.Note, com.twenty.inhub.boundedContext.note.entity.QNote> sendList = this.<com.twenty.inhub.boundedContext.note.entity.Note, com.twenty.inhub.boundedContext.note.entity.QNote>createList("sendList", com.twenty.inhub.boundedContext.note.entity.Note.class, com.twenty.inhub.boundedContext.note.entity.QNote.class, PathInits.DIRECT2);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    public final StringPath token = createString("token");

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

