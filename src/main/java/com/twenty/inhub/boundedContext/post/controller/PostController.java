package com.twenty.inhub.boundedContext.post.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
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
    private final Rq rq;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(Model model) {
        log.info("확인");
        model.addAttribute("postDto", new PostDto());
        return "usr/community/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@ModelAttribute("postDto") PostDto postDto) {
        postService.createPost(postDto);
        return rq.redirectWithMsg("/community/list", RsData.of("S-50","게시물이 생성되었습니다."));
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
        model.addAttribute("editUrl", "/community/edit/" + id);
        return "usr/community/view";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "usr/community/edit";
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable("id") Long id, @ModelAttribute("post") PostDto postDto) {
        postDto.setId(id);
        postService.updatePost(postDto);
        return rq.redirectWithMsg("/community/view/" + id, RsData.of("S-51","게시물이 수정되었습니다."));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);

        return rq.redirectWithMsg("/community/list", RsData.of("S-52","게시물이 삭제되었습니다."));
    }

    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/";
    }
}