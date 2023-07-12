package com.twenty.inhub.boundedContext.book.controller;

import com.twenty.inhub.base.excption.InvalidTokenException;
import com.twenty.inhub.base.excption.NotFoundException;
import com.twenty.inhub.base.jwt.JwtProvider;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.dto.BookResDto;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "문제집관련 api", description = "문제집 관련 정보를 조회할 수 있는 open api 입니다.")
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookRestOpenApiController {

    private final BookService bookService;
    private final JwtProvider jwtProvider;


    //-- find by id --//
    @GetMapping("/{id}")
    @Operation(description = "문제집 id 로 문제집 조회")
    public BookResDto findById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader("Authorization");
        if (!jwtProvider.verify(accessToken))
            throw new InvalidTokenException();
        log.info("book id 로 조회 book id = {}", id);

        RsData<Book> bookRs = bookService.findById(id);
        if (bookRs.isFail()) {
            log.info("book 조회 실패 msg = {}", bookRs.getMsg());
            throw new NotFoundException("존재하지 않는 id");
        }

        log.info("book 응답 완료");
        return new BookResDto(bookRs.getData());
    }

    //-- search book list --//
    @GetMapping("/search")
    @Operation(description = "검색어로 문제집 list 조회 / input 생략시 모든 data 조회")
    public PageResForm<BookResDto> search(
            @RequestParam(required = false) String input,
            @RequestParam(defaultValue = "0") int page,
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader("Authorization");
        if (!jwtProvider.verify(accessToken))
            throw new InvalidTokenException();
        log.info("검색어로 book 조회 요청 확인 input = {}", input);

        PageResForm<BookResDto> resDto = bookService.findDtoByInput(new SearchForm(0, input, page));

        log.info("검색어로 book 조회 요청 완료");
        return resDto;
    }
}
