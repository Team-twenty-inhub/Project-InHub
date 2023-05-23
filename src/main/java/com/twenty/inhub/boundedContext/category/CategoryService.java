package com.twenty.inhub.boundedContext.category;

import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * ** CREATE METHOD **
     * create Category
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


    /**
     * ** READ METHOD **
     * find by name
     */

    //-- find by name --//
    public RsData<Category> findByName(String name) {
        Optional<Category> byName = categoryRepository.findByName(name);

        if (byName.isPresent())
            return RsData.successOf(byName.get());

        return RsData.of("F-1", "존재하지 않는 카테고리 입니다.");
    }
}