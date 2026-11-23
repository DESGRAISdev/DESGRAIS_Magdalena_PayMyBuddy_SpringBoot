package com.paymybuddy.paymybuddy.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "user" )

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id ;

    @Column (length = 100)
 private String username ;

    @Column (nullable = false, unique = true, length = 100)
 private String email ;

    @Column (nullable = false, length = 100)
 private String password ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> connections = new ArrayList<>();
}


