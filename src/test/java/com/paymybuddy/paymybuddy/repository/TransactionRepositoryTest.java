package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User creerUtilisateur(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("hash");
        return userRepository.save(user);
    }

    @Test
    void findBySenderOrderByIdDesc_retourneLesTransactionsDeLExpediteur() {
        User sender = creerUtilisateur("Expediteur", "expediteur@gmail.com");
        User receiver = creerUtilisateur("Destinataire", "destinataire@gmail.com");

        Transaction t1 = new Transaction();
        t1.setSender(sender);
        t1.setReceiver(receiver);
        t1.setDescription("Restaurant");
        t1.setAmount(new BigDecimal("10.50"));
        transactionRepository.save(t1);

        Transaction t2 = new Transaction();
        t2.setSender(sender);
        t2.setReceiver(receiver);
        t2.setDescription("Cinéma");
        t2.setAmount(new BigDecimal("8.00"));
        transactionRepository.save(t2);

        List<Transaction> resultat = transactionRepository.findBySenderOrderByIdDesc(sender);

        assertEquals(2, resultat.size());
        assertEquals("Cinéma", resultat.get(0).getDescription());
        assertEquals("Restaurant", resultat.get(1).getDescription());
        assertEquals(0, new BigDecimal("10.50").compareTo(resultat.get(1).getAmount()));
    }

    @Test
    void findBySenderOrderByIdDesc_retourneUneListeVideSiAucuneTransaction() {
        User user = creerUtilisateur("Sans transaction", "sanstransaction@gmail.com");

        assertTrue(transactionRepository.findBySenderOrderByIdDesc(user).isEmpty());
    }
}
