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
        model.addAttribute("post", new Post());

        return "post/write";
    }
    @RequestMapping(value="/post/write", method=RequestMethod.POST)
    public String createPost(Post newPost) {
        logger.info(newPost.toString());
        postService.save(newPost);

        return "redirect:/post";
    }
    @GetMapping("/post/modify/{id}")
    public String modifyPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));

        return "post/write";
    }
    @RequestMapping(value="/post/modify/{id}", method=RequestMethod.POST)
    public String modifyPost(@PathVariable Long id, Post modifiedPost) {
        logger.info(modifiedPost.toString());
        postService.update(id, modifiedPost);

        return "redirect:/post";
    }

    @RequestMapping(value="/post/delete/{id}", method=RequestMethod.POST)
    public String deletePost(@PathVariable Long id) {
        String msg = postService.delete(id);
        logger.info(msg);
        return "redirect:/post";
    }
}
