package kr.pe.hw.blog.controller;

import kr.pe.hw.blog.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class MainController {
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("userId", SecurityUtil.getCurrentUser());
        return "/main";
    }
}
