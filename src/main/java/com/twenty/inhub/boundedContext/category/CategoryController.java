package com.twenty.inhub.boundedContext.category;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.category.form.CreateCategoryForm;
import com.twenty.inhub.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.twenty.inhub.boundedContext.member.entity.MemberRole.ADMIN;

@Slf4j
@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final Rq rq;


    //-- category 생성 폼 --//
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(CreateCategoryForm form) {
        log.info("스터디 생성폼 요청 확인");
        return "usr/category/top/create";
    }

    //-- category 생성 처리 --//
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(CreateCategoryForm form) {
        log.info("카테고리 생성 요청 확인 name = {}", form.getName());

        Member member = rq.getMember();
        if (member.getRole() == ADMIN) {
            log.info("등급 미달로 인한 권한 없음");
            return rq.historyBack("카테고리 생성은 주니어 등급 부터 가능합니다.");
        }

        RsData<Category> categoryRs = categoryService.create(form);

        if (categoryRs.isFail()) {
            log.info("카테고리 생성 실패 msg = {}", categoryRs.getMsg());
            return rq.historyBack(categoryRs.getMsg());
        }

        log.info("카테고리 생성 완료");
        return rq.redirectWithMsg("/category/list", categoryRs.getMsg());
    }


    //-- category list.html --//
    @GetMapping("/list")
    public String list(Model model) {
        log.info("카테고리 리스트 요청 확인");

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "usr/category/top/list";
    }
}
