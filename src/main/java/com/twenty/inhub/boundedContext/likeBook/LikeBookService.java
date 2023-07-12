package com.twenty.inhub.boundedContext.likeBook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeBookService {

    private final LikeBookRepository likeBookRepository;


}
