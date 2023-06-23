package com.twenty.inhub.boundedContext.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.member.entity.QMember;
import com.twenty.inhub.boundedContext.question.entity.QQuestion;
import com.twenty.inhub.boundedContext.underline.QUnderline;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryQueryRepository {

    private final JPAQueryFactory query;
    private QCategory category = QCategory.category;
    private QQuestion question = QQuestion.question;
    private QMember member = QMember.member;
    private QUnderline underline = QUnderline.underline;

    public CategoryQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- find categories by input --//
    public PageResForm<Category> findByInput(SearchForm form) {
        List<Category> categories = query
                .selectFrom(category)
                .where(category.name.like("%" + form.getInput() + "%"))
                .offset(form.getPage() * 7)
                .limit(7)
                .fetch();

        Long count = query
                .select(category.count())
                .from(category)
                .where(category.name.like("%" + form.getInput() + "%"))
                .fetchOne();

        return new PageResForm(categories, form.getPage(), count);
    }
}
