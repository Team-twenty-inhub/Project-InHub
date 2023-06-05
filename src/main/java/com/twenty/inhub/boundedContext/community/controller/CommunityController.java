//package com.twenty.inhub.boundedContext.community.controller;
//
//import com.twenty.inhub.base.request.Rq;
//import com.twenty.inhub.base.request.RsData;
//import com.twenty.inhub.boundedContext.community.controller.form.CreateCommunityForm;
//import com.twenty.inhub.boundedContext.community.entity.Community;
//import com.twenty.inhub.boundedContext.community.service.CommunityService;
//import com.twenty.inhub.boundedContext.post.entity.Post;
//import com.twenty.inhub.boundedContext.post.service.PostService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@Controller
//@RequestMapping("/community")
//@RequiredArgsConstructor
//public class CommunityController {
//    private final CommunityService communityService;
//    private final PostService postService;
//
//    @GetMapping("/{boardId}")
//    public String list(@PathVariable("boardId") Long boardId, Model model) {
//        List<Post> posts = (List<Post>) postService.getPostsByBoardId(boardId);
//        model.addAttribute("posts", posts);
//        return "usr/community/list";
//    }
//}