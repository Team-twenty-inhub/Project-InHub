package com.twenty.inhub.boundedContext.book.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.twenty.inhub.base.appConfig.S3Config;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.BookCreateForm;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.book.entity.Book;
import com.twenty.inhub.boundedContext.book.repository.BookQueryRepository;
import com.twenty.inhub.boundedContext.book.repository.BookRepository;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.question.entity.Question;
import com.twenty.inhub.boundedContext.question.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookQueryRepository bookQueryRepository;
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;
    @Value("${cloud.aws.s3.storage}")
    private String storage;

    /**
     * ** CREATE METHOD **
     * create book
     * save img by s3
     */

    //-- create book --//
    @Transactional
    public RsData<Book> create(BookCreateForm form, Member member) {

        List<Tag> tags = createTags(form.getTagList());

        Book book = bookRepository.save(
                Book.createBook(form, member, tags)
        );

        if (form.getImg() != null) {
            String img = saveByS3(book, form.getImg());
            book.createImg(img);
        }

        return RsData.of("S-1", "문제집이 생성되었습니다.", book);
    }

    // save img by s3 //
    private String saveByS3(Book book, MultipartFile img) {
        String fileName = "image_bookId_" + book.getId();
        String imgUrl = "https://s3." + s3Config.getRegion() + ".amazonaws.com/" + s3Config.getBucket() + "/" + storage + "/" + fileName;

        try {
            ObjectMetadata data = new ObjectMetadata();
            data.setContentType(img.getContentType());
            data.setContentLength(img.getSize());

            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), storage + "/" + fileName, img.getInputStream(), data));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgUrl;
    }


    /**
     * ** SELECT METHOD **
     * find by id
     * find random books
     * find by name $ tag
     * find all
     * find by member
     */

    //-- find by id --//
    public RsData<Book> findById(Long id) {
        Optional<Book> byId = bookRepository.findById(id);

        if (byId.isPresent())
            return RsData.of(byId.get());

        return RsData.of("F-1", "존재하지 않는 id");
    }

    //-- find random books --//
    public List<Book> findRandomBooks(int random, int count) {
        return bookQueryRepository.findRandomBooks(random, count);
    }

    //-- find by name $ tag --//
    public PageResForm<Book> findByNameTag(SearchForm form) {
        return bookQueryRepository.findByNameTag(form);
    }

    //-- find all --//
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    //-- find by member --//
    public List<Book> findByMember(Member member) {
        return bookQueryRepository.findByMember(member);
    }


    /**
     * ** NOT RELATED TO DB **
     * 난수 생성
     * create tag
     */

    //-- 난수 생성 --//
    public int random(int max) {

        // 0 <= return < max
        return new Random().nextInt(max);
    }

    //-- create tag --//
    private static List<Tag> createTags(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String tag : tags)
            tagList.add(Tag.createTag(tag));
        return tagList;
    }
}
