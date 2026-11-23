package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

    @Data
    @Entity
    @Table(name = "transaction")
    public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sender_id", nullable = false)
        private User sender;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "receiver_id", nullable = false)
        private User receiver;

        @Column(length = 200)
        private String description;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal amount;
    }

