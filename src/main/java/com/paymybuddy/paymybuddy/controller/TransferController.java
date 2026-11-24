package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class TransferController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public TransferController(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              TransactionService transactionService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @GetMapping("/transfer")
    public String transferPage(Authentication authentication, Model model) {
        fillModel(authentication, model);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String pay(@RequestParam Integer receiverId,
                      @RequestParam String description,
                      @RequestParam BigDecimal amount,
                      Authentication authentication,
                      Model model) {
        User sender = currentUser(authentication);
        try {
            transactionService.pay(sender.getId(), receiverId, description, amount);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            fillModel(authentication, model);
            return "transfer";
        }
        return "redirect:/transfer";
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElseThrow();
    }

    private void fillModel(Authentication authentication, Model model) {
        User user = currentUser(authentication);
        model.addAttribute("connections", user.getConnections());
        model.addAttribute("transactions", transactionRepository.findBySenderOrderByIdDesc(user));
    }
}
