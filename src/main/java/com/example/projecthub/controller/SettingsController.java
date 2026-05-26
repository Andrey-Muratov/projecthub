package com.example.projecthub.controller;

import com.example.projecthub.entity.User;
import com.example.projecthub.service.CurrentUserService;
import com.example.projecthub.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    public SettingsController(CurrentUserService currentUserService,
                              UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String view(Model model) {
        User current = currentUserService.getCurrent();
        model.addAttribute("user", current);
        return "settings/view";
    }

    @PostMapping("/settings/notifications")
    public String updateNotifications(@RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "emailNotifications", required = false) Boolean emailNotifications,
                                      RedirectAttributes redirectAttributes) {
        User current = currentUserService.getCurrent();
        userService.updateNotificationSettings(current, email, emailNotifications != null && emailNotifications);
        redirectAttributes.addFlashAttribute("flashSuccess", "Настройки сохранены.");
        return "redirect:/settings";
    }
}
