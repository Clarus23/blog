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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
//@RestController
@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/sign-in")
    public ModelAndView signIn() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/member/sign-in");

        return mv;
    }
    @RequestMapping(value="/member/sign-in", method=RequestMethod.POST)
    public String signIn(HttpServletResponse response
                         , @RequestParam String username
                         , @RequestParam String password) {

        TokenDto token = memberService.signIn(username, password);

        Cookie cookie = new Cookie("Authorization", "Bearer_" + token.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3 * 60 * 60);

        response.addCookie(cookie);

        log.info("request username = {}, password = {}", username, password);
        log.info("\nJWT\naccessToken = {}\nrefreshToken = {}", token.getAccessToken(), token.getRefreshToken());

        return "redirect:/post/list";
    }

    @GetMapping("/member/sign-up")
    public ModelAndView signUp() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/member/sign-up");

        return mv;
    }
    @RequestMapping(value="/member/sign-up", method=RequestMethod.POST)
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }
}
