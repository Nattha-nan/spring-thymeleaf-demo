package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "message",
                "Hello, Natthanan Boodsadee!"
        );

        model.addAttribute(
                "studentId",
                "673380037-1"
        );

        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){

        return "about";
    }

}