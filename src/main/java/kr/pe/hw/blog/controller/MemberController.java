package kr.pe.hw.blog.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.pe.hw.blog.dto.SignInDto;
import kr.pe.hw.blog.dto.MemberDto;
import kr.pe.hw.blog.dto.SignUpDto;
import kr.pe.hw.blog.dto.TokenDto;
import kr.pe.hw.blog.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
//@RestController
@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "/member/sign-in";
    }
    @RequestMapping(value="/sign-in", method=RequestMethod.POST)
    public String signIn(HttpServletResponse response, Model model, @ModelAttribute SignInDto signInDto) {
        TokenDto token = new TokenDto();
        try {
            token = memberService.signIn(signInDto);
            log.info("\nJWT\naccessToken = {}\nrefreshToken = {}", token.getAccessToken(), token.getRefreshToken());
        } catch(Exception e) {
            model.addAttribute("msg", "존재하지 않는 회원 또는 잘못된 비밀번호입니다.");
            model.addAttribute("url", "/member/sign-in");
            return "/alert";
        }

        Cookie cookie = new Cookie("Authorization", "Bearer_" + token.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3 * 60 * 60);

        response.addCookie(cookie);


        model.addAttribute("msg", "로그인 되었습니다.");
        model.addAttribute("url", "/post/list");
        return "/alert";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "/member/sign-up";
    }
    @RequestMapping(value="/sign-up", method=RequestMethod.POST)
    public String signUp(Model model, @ModelAttribute SignUpDto signUpDto) {
        try {
            memberService.signUp(signUpDto);
        } catch(Exception e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/member/sign-up");
            return "/alert";
        }

        model.addAttribute("msg", "회원가입 되었습니다.");
        model.addAttribute("url", "/post/list");
        return "/alert";
    }

    @RequestMapping(value="/sign-out", method=RequestMethod.POST)
    public String signOut(HttpServletResponse response, Model model) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        model.addAttribute("msg", "로그아웃 되었습니다.");
        model.addAttribute("url", "/post/list");

        return "/alert";
    }
}
