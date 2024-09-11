package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.domain.Member;
import kr.pe.hw.blog.dto.LoginRequest;
import kr.pe.hw.blog.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.logging.Logger;

@Controller
public class MemberController {
    Logger logger = Logger.getLogger(MemberController.class.getName());
    MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/join")
    public String join(Model model) {
        return "member/join";
    }
    @RequestMapping(value="/member/join", method= RequestMethod.POST)
    public String join(Model model, Member member) {

        return "";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginRequest loginRequest) {
        boolean validate = memberService.validateMember(loginRequest);
        logger.info(validate ? "success" : "fail");

        return "redirect:/";
    }
}
