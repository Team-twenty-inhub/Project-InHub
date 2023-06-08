package com.twenty.inhub.boundedContext.community.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.community.controller.form.CommunityForm;
import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.service.CommunityService;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("communityForm", new CommunityForm());
        return "usr/community/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("communityForm") @Valid CommunityForm communityForm, BindingResult result) {
        if (result.hasErrors()) {
            return "usr/community/create";
        }

        communityService.createCommunity(communityForm);
        return "redirect:/community/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Community> communities = communityService.getAllCommunities();
        model.addAttribute("communities", communities);
        return "usr/community/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Community community = communityService.getCommunityById(id);
        model.addAttribute("community", community);
        model.addAttribute("posts", communityService.getPostsByCommunityId(id));
        return "usr/community/view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Community community = communityService.getCommunityById(id);
        model.addAttribute("communityForm", new CommunityForm(community.getId(), community.getName()));
        return "usr/community/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @ModelAttribute("communityForm") @Valid CommunityForm communityForm,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "usr/community/edit";
        }

        communityService.updateCommunity(id, communityForm);
        return "redirect:/community/view/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        communityService.deleteCommunity(id);
        return "redirect:/community/list";
    }
}