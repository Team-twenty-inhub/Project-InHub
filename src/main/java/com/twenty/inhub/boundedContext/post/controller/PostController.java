package com.twenty.inhub.boundedContext.post.controller;

import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(Model model, Member member) {
        log.info("확인");
        String username = member.getUsername();
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("username", username);
        return "usr/community/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@ModelAttribute("postDto") PostDto postDto, Member member) {
        String username = member.getUsername();
        postDto.setUsername(username);
        postService.createPost(postDto);
        return "redirect:/community/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<PostDto> postDtoList = postService.findPost();
        model.addAttribute("postList", postDtoList);
        return "usr/community/list";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("isAuthenticated()")
    public String view(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "usr/community/view";
    }

    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/";
    }
}