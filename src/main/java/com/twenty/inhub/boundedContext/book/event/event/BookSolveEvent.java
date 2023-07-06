package com.twenty.inhub.boundedContext.book.event.event;

import com.twenty.inhub.boundedContext.book.entity.Book;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookSolveEvent extends ApplicationEvent {

    private Book book;
    private double score;

    public BookSolveEvent(Object source, Book book, double score) {
        super(source);
        this.book = book;
        this.score = score;
    }
}
