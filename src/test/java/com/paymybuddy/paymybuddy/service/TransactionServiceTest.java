package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void pay_fonctionneDansLeSensDeLaRelation() {
        User nina = userService.register("Nina", "nina@gmail.com", "mdp12345");
        User paula = userService.register("Paula", "paula@gmail.com", "mdp12345");
        userService.addConnection(nina.getId(), "paula@gmail.com");

        Transaction t = transactionService.pay(nina.getId(), paula.getId(),
                "Restaurant", new BigDecimal("10.50"));

        assertNotNull(t.getId());
    }

    @Test
    void pay_refuseDansLeSensInverseSiLAutreNeVousAPasAjoute() {
        User nina = userService.register("Nina", "nina@gmail.com", "mdp12345");
        User paula = userService.register("Paula", "paula@gmail.com", "mdp12345");
        userService.addConnection(nina.getId(), "paula@gmail.com");

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.pay(paula.getId(), nina.getId(),
                        "Remboursement", new BigDecimal("5.00")));
    }

    @Test
    void pay_refuseUnMontantNulOuNegatif() {
        User nina = userService.register("Nina", "nina@gmail.com", "mdp12345");
        User paula = userService.register("Paula", "paula@gmail.com", "mdp12345");
        userService.addConnection(nina.getId(), "paula@gmail.com");

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.pay(nina.getId(), paula.getId(), "Test", BigDecimal.ZERO));
    }
}
