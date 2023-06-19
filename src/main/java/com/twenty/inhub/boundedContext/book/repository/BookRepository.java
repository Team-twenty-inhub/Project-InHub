package com.twenty.inhub.boundedContext.book.repository;

import com.twenty.inhub.boundedContext.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
