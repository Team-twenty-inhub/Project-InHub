package com.twenty.inhub.boundedContext.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -863297824L;

    public static final QCategory category = new QCategory("category");

    public final StringPath about = createString("about");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final ListPath<com.twenty.inhub.boundedContext.question.entity.Question, com.twenty.inhub.boundedContext.question.entity.QQuestion> questions = this.<com.twenty.inhub.boundedContext.question.entity.Question, com.twenty.inhub.boundedContext.question.entity.QQuestion>createList("questions", com.twenty.inhub.boundedContext.question.entity.Question.class, com.twenty.inhub.boundedContext.question.entity.QQuestion.class, PathInits.DIRECT2);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

