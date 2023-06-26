package com.twenty.inhub.boundedContext.category;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.book.controller.form.PageResForm;
import com.twenty.inhub.boundedContext.book.controller.form.SearchForm;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.question.controller.controller.dto.CategoryReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    /**
     * ** CREATE METHOD **
     * create Category
     * create Categories api
     */

    //-- create Category --//
    @Transactional
    public RsData<Category> create(CreateCategoryForm form) {
        RsData<Category> byName = this.findByName(form.getName());

        if (byName.isSuccess())
            return RsData.of("F-1", form.getName() + " 은 이미 존재하는 카테고리 입니다.");

        Category category = Category.crateCategory(form);
        Category save = categoryRepository.save(category);
        return RsData.of("S-1", "카테고리가 생성되었습니다.", save);
    }

    //-- create Categories api --//
    @Transactional
    public RsData<List<Long>> create(List<CategoryReqDto> reqDtoList) {

        List<Long> resList = new ArrayList<>();

        for (CategoryReqDto reqDto : reqDtoList) {
            Category category = categoryRepository.save(Category.crateCategory(reqDto));
            resList.add(category.getId());
        }
        return RsData.of(resList);
    }


    /**
     * ** READ METHOD **
     * find by name
     * find all
     * find by id
     * find by input
     */

    //-- find by name --//
    public RsData<Category> findByName(String name) {
        Optional<Category> byName = categoryRepository.findByName(name);

        if (byName.isPresent())
            return RsData.of(byName.get());

        return RsData.of("F-1", "존재하지 않는 카테고리 입니다.");
    }

    //-- find all --//
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    //-- find by id --//
    public RsData<Category> findById(Long id) {
        Optional<Category> byId = categoryRepository.findById(id);

        if (byId.isPresent())
            return RsData.of(byId.get());

        return RsData.of("F-1", "존재하지 않는 id");
    }

    //-- find By Input --//
    public PageResForm<Category> findByInput(SearchForm form) {
        return categoryQueryRepository.findByInput(form);
    }
}