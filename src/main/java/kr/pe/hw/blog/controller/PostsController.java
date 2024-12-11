package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.dto.PostUploadDto;
import kr.pe.hw.blog.service.FileService;
import kr.pe.hw.blog.service.PostCommentService;
import kr.pe.hw.blog.service.PostService;
import kr.pe.hw.blog.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostsController {
    private final PostService postService;
    private final FileService fileService;
    private final PostCommentService postCommentService;

    @Autowired
    public PostsController(PostService postService, FileService fileService, PostCommentService postCommentService) {
        this.postService = postService;
        this.fileService = fileService;
        this.postCommentService = postCommentService;
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
        model.addAttribute("comments", postCommentService.viewComments(id));

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
    public String createPost(@ModelAttribute PostUploadDto newPost, Model model
                            , @RequestPart(value = "summernoteImages", required = false) MultipartFile[] images
                            , @RequestParam(value = "fileUri", required = false) List<String> newUri) {
        log.info(newPost.toString());
        postService.save(newPost, images, newUri);

        model.addAttribute("msg", "게시글을 작성하였습니다.");
        model.addAttribute("url", "/post/list");
        return "/alert";
    }
    @GetMapping("/modify/{id}")
    public String modifyPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        model.addAttribute("userId", SecurityUtil.getCurrentUser());

        return "post/write";
    }
    @RequestMapping(value="/modify/{id}", method=RequestMethod.POST)
    public String modifyPost(@PathVariable Long id
                            , @ModelAttribute PostUploadDto modifiedPost, Model model
                            , @RequestPart(value = "summernoteImages", required = false) MultipartFile[] images
                            , @RequestParam(value = "fileUri", required = false) List<String> newUri
                            , @RequestParam(value = "deleteFilename", required = false) List<String> deleteFilenames) {
        log.info(modifiedPost.toString());
        postService.update(id, modifiedPost, images, newUri, deleteFilenames);

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

    @RequestMapping(value="/image-upload", method=RequestMethod.POST)
    @ResponseBody
    public String imageUpload(@RequestParam("file") MultipartFile multipartFile) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("url", fileService.tempSave(multipartFile));
            jsonObject.addProperty("responseCode", "success");
        } catch(Exception e) {
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        log.info("jsonObject.toString() : {}", jsonObject.toString());
        return jsonObject.toString();
    }

    @RequestMapping(value="/image-delete", method=RequestMethod.POST)
    @ResponseBody
    public String imageDelete(@RequestParam("file") String filename) {
        JsonObject jsonObject = new JsonObject();
        log.info("filename: {}", filename);
        try {
            fileService.tempDelete(filename);
            jsonObject.addProperty("responseCode", "success");
        } catch(RuntimeException e) {
            jsonObject.addProperty("responseCode", "error");
        }

        return jsonObject.toString();
    }
}
