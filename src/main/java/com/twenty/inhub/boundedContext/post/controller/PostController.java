package com.twenty.inhub.boundedContext.post.controller;

import com.twenty.inhub.base.request.Rq;
import com.twenty.inhub.base.request.RsData;
import com.twenty.inhub.boundedContext.comment.entity.Comment;
import com.twenty.inhub.boundedContext.comment.repository.CommentRepository;
import com.twenty.inhub.boundedContext.comment.service.CommentService;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.member.entity.MemberRole;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import com.twenty.inhub.boundedContext.post.entity.Post;
import com.twenty.inhub.boundedContext.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final Rq rq;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(Model model) {
        log.info("확인");
        model.addAttribute("postDto", new PostDto());
        return "usr/post/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@ModelAttribute("postDto") PostDto postDto, @RequestParam("board") String board) {
        Member member = rq.getMember();
        postDto.setBoard(board); // 선택한 게시판 정보 설정
        postService.createPost(postDto, member);
        return rq.redirectWithMsg("/post/list", RsData.of("S-50","게시물이 생성되었습니다."));
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "free") String board,
                       @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> paging = postService.getList(board, page);

        model.addAttribute("paging", paging);
        model.addAttribute("board", board);
        model.addAttribute("currentPage", page); // 현재 페이지 정보 전달
        model.addAttribute("totalPages", paging.getTotalPages()); // 전체 페이지 수 전달

        return "usr/post/list";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("isAuthenticated()")
    public String view(@PathVariable("id") Long id, Model model, HttpSession session) {
        Post post = postService.increasedHits(id, session);
        List<Comment> comments = commentRepository.findByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("editUrl", "/post/edit/" + id);
        model.addAttribute("comments", comments);
        model.addAttribute("authorNickname", post.getAuthorNickname());

        return "usr/post/view";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPost(id);

        if (!post.isCreatedBy(rq.getMember())) {
            return rq.historyBack("이 게시글을 수정할 권한이 없습니다.");
        }

        model.addAttribute("post", post);
        return "usr/post/edit";
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable("id") Long id, @ModelAttribute("post") PostDto postDto) {
        Post post = postService.getPost(id);

        if (!post.isCreatedBy(rq.getMember())) {
            return rq.historyBack("이 게시글을 수정할 권한이 없습니다.");
        }
        postDto.setId(id);
        postService.updatePost(postDto, rq.getMember());
        return rq.redirectWithMsg("/post/view/" + id, RsData.of("S-51","게시물이 수정 되었습니다."));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id) {
        Member member = rq.getMember();
        Post post = postService.getPost(id);

        if (post.isCreatedBy(member) || member.getRole() == MemberRole.ADMIN) {
            postService.deletePost(id, member);
            return rq.redirectWithMsg("/post/list", RsData.of("S-52", "게시물이 삭제되었습니다."));
        } else {
            return rq.historyBack("이 게시글을 삭제할 권한이 없습니다.");
        }
    }

    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/";
    }
}