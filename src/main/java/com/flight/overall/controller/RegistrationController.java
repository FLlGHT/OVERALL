package com.flight.overall.controller;

import com.flight.overall.dto.AccountDTO;
import com.flight.overall.entity.Account;
import com.flight.overall.exception.ProfileAlreadyExistException;
import com.flight.overall.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        AccountDTO accountDTO = new AccountDTO();
        model.addAttribute("account", accountDTO);

        return "registration";
    }

    @PostMapping("/registration")
    public String registerNewAccount(@ModelAttribute("account") @Valid AccountDTO account, Model model) {
        try {
            Account registered = accountService.registerNewAccount(account);
            accountService.authenticate(registered);
        } catch (ProfileAlreadyExistException uaeEx) {
            model.addAttribute("message", "An account for that username already exists.");
            model.addAttribute("account", account);

            return "redirect:/registration?error";
        }

        return "redirect:/" + account.getUsername();
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login";
    }
}
