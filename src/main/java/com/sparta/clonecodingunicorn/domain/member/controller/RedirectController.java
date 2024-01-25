package com.sparta.clonecodingunicorn.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping("/redirect-to-neeks-shop")
    public String redirectToNeeksShop() {
        return "redirect:https://www.neeks.shop";
    }
}