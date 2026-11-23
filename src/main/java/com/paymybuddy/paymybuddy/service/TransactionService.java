package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction pay(Integer senderId, Integer receiverId,
                           String description, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable."));


        boolean estUneRelation = sender.getConnections().stream()
                .anyMatch(c -> c.getId().equals(receiverId));
        if (!estUneRelation) {
            throw new IllegalArgumentException("Ce destinataire ne fait pas partie de vos relations.");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        return transactionRepository.save(transaction);
    }
}
