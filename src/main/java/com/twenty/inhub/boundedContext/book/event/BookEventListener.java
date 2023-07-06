package com.twenty.inhub.boundedContext.book.event;

import com.twenty.inhub.boundedContext.book.event.event.BookSolveEvent;
import com.twenty.inhub.boundedContext.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class BookEventListener {

    private final BookService bookService;

    @EventListener
    public void listen(BookSolveEvent event) {
        bookService.updateAccuracy(event);
    }
}
