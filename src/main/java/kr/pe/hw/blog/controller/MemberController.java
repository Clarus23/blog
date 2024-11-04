package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.dto.SignInDto;
import kr.pe.hw.blog.dto.MemberDto;
import kr.pe.hw.blog.dto.SignUpDto;
import kr.pe.hw.blog.dto.TokenDto;
import kr.pe.hw.blog.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/sign-in")
    public ModelAndView signIn() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/member/sign-in");

        return mv;
    }
    @RequestMapping(value="/sign-in", method=RequestMethod.POST)
    public ModelAndView signIn(@RequestBody SignInDto signInDto) {
        TokenDto token = memberService.signIn(signInDto);
        log.info("request username = {}, password = {}", signInDto.getUsername(), signInDto.getPassword());
        log.info("JWT accessToken = {}, refreshToken = {}", token.getAccessToken(), token.getRefreshToken());

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/post/list");
        mv.addObject("token", token);

        return mv;
    }

    @GetMapping("/sign-up")
    public ModelAndView signUp() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/member/sign-up");

        return mv;
    }
    @RequestMapping(value="/sign-up", method=RequestMethod.POST)
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @RequestMapping(value="/test", method=RequestMethod.POST)
    public String test() { return "success"; }
}
