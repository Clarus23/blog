package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.service.PostService;
import kr.pe.hw.blog.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostsController {
    Logger logger = Logger.getLogger(PostsController.class.getName());
    private final PostService postService;

    @Autowired
    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue="0") int page) {
        Page<Post> posts = postService.findPostPages(page);
        model.addAttribute("posts", posts);
        model.addAttribute("userId", SecurityUtil.getCurrentUser()); // default : anonymousUser

        return "post/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));

        model.addAttribute("userId", SecurityUtil.getCurrentUser());
        model.addAttribute("userRole", SecurityUtil.getCurrentUserRole());

        return "post/detail";
    }

    @GetMapping("/write")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("userId", SecurityUtil.getCurrentUser());

        return "post/write";
    }
    @RequestMapping(value="/write", method=RequestMethod.POST)
    public String createPost(Post newPost, Model model) {
        logger.info(newPost.toString());
        postService.save(newPost);

        model.addAttribute("msg", "게시글을 작성하였습니다.");
        model.addAttribute("url", "/post/list");
        return "/alert";
    }
    @GetMapping("/modify/{id}")
    public String modifyPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));

        return "post/write";
    }
    @RequestMapping(value="/modify/{id}", method=RequestMethod.POST)
    public String modifyPost(@PathVariable Long id, Post modifiedPost, Model model) {
        logger.info(modifiedPost.toString());
        postService.update(id, modifiedPost);

        model.addAttribute("msg", "게시글을 수정하였습니다.");
        model.addAttribute("url", "/post/list");
        return "/alert";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
    public String deletePost(@PathVariable Long id, Model model) {
        String msg = postService.delete(id);

        model.addAttribute("msg", msg);
        model.addAttribute("url", "/post/list");
        return "/alert";
    }
}
