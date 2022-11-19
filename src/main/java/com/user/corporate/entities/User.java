package com.user.corporate.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "CORPORATE_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CIVIL_ID")
    private Integer civilId;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", nullable = false)
    private List<UserDocuments> userDocuments;

}

