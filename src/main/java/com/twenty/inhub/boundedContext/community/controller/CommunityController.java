package com.twenty.inhub.boundedContext.community.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.community.controller.form.CreateCommunityForm;
import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final Rq rq;

    //-- 게시판 생성 폼 --//
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(CreateCommunityForm form, Model model) {
        return "usr/community/create";
    }

    //-- 게시판 생성 처리 --//
    @PostMapping("/create")
    public String create(CreateCommunityForm form) {
        System.out.println("123");
        Community community = new Community(form.getTitle(), form.getContent());

        communityService.createCommunity(community);
        return "redirect:/";
    }

    //-- 게시판 조회 --//
    @GetMapping("/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        RsData<Community> communityRs = communityService.getCommunityById(id);

        if (communityRs.isSuccess()) {
            Community community = communityRs.getData();
            model.addAttribute("community", community);
            return "usr/community/view";
        } else {
            return rq.historyBack(communityRs.getMsg());
        }
    }

    //-- 게시판 수정 폼 --//
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        RsData<Community> communityRs = communityService.getCommunityById(id);

        if (communityRs.isSuccess()) {
            Community community = communityRs.getData();
            model.addAttribute("community", community);
            return "usr/community/edit";
        } else {
            return rq.historyBack(communityRs.getMsg());
        }
    }

    //-- 게시판 수정 처리 --//
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Community updatedCommunity) {
        RsData<Community> communityRs = communityService.getCommunityById(id);

        if (communityRs.isSuccess()) {
            communityService.updateCommunity(id, updatedCommunity);
            return "redirect:/usr//community/{id}";
        } else {
            return rq.historyBack(communityRs.getMsg());
        }
    }

    //-- 게시판 삭제 처리 --//
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        RsData<Community> communityRs = communityService.getCommunityById(id);

        if (communityRs.isSuccess()) {
            communityService.deleteCommunity(id);
            return "redirect:/usr/community";
        } else {
            return rq.historyBack(communityRs.getMsg());
        }
    }

    //-- 게시판 목록 조회 --//
    @GetMapping
    public String list(Model model) {
        List<Community> communities = communityService.getAllCommunities();
        model.addAttribute("communities", communities);
        model.addAttribute("member", rq.getMember());
        return "usr/community/list";
    }
}