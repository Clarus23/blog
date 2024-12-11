package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.service.PostCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/comment")
public class PostCommentController {
    private final PostCommentService postCommentService;

    @Autowired
    PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    String addComment(Model model, @RequestParam("postId") Long postId, @RequestParam("comment") String comment, @RequestParam("userName") String userName) {
        postCommentService.save(postId, comment, userName);

        model.addAttribute("url", "/post/detail/"+postId);
        model.addAttribute("msg", "댓글이 작성되었습니다.");
        return "/alert";
    }

    @RequestMapping(value="/delete", method= RequestMethod.POST)
    String deleteComment(Model model, @RequestParam("postId") Long postId, @RequestParam("commentId") Long commentId) {

        model.addAttribute("url", "/post/detail/"+postId);
        model.addAttribute("msg", "댓글이 삭제되었습니다.");
        return "/alert";
    }

    @RequestMapping(value="/modify", method= RequestMethod.POST)
    String modifyComment(Model model, @RequestParam("postId") Long postId, @RequestParam("comment") String comment, @RequestParam("commentId") Long commentId) {

        model.addAttribute("url", "/post/detail/"+postId);
        model.addAttribute("msg", "댓글이 성공적으로 수정되었습니다.");
        return "/alert";
    }
}
