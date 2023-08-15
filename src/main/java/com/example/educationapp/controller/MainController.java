package com.example.educationapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app") // Базовый URL для всех методов в контроллере
public class MainController {

    @GetMapping("/home")
    public String homePage(Model model) {
        // Добавляем атрибуты в модель, которые могут использоваться в представлении
        model.addAttribute("pageTitle", "Home Page");
        return "home"; // Возвращаем имя представления (например, "home")
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("pageTitle", "About Us");
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("pageTitle", "Contact Us");
        return "contact";
    }
}