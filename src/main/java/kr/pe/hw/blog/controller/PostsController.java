package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
public class PostsController {
    Logger logger = Logger.getLogger(PostsController.class.getName());
    private final PostService postService;

    @Autowired
    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    public String list(Model model, @RequestParam(value = "page", defaultValue="0") int page) {
        Page<Post> posts = postService.findPostPages(page);
        model.addAttribute("posts", posts);

        return "post/list";
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));

        return "post/detail";
    }

    @GetMapping("/post/write")
    public String newPost(Model model) {

        return "post/write";
    }

    @RequestMapping(value="/post/write", method=RequestMethod.POST)
    public String createPost(Post newPost) {
        logger.info(newPost.toString());
        postService.save(newPost);

        return "redirect:/post";
    }


}
