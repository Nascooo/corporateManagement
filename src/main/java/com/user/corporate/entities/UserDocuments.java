package com.user.corporate.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_DOCUMENTS")
public class UserDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long documentId;

    @Column(name = "USER_ID", nullable = false, insertable = false, updatable = false)
    private Long userId;

    @Lob
    @Column(name = "DOCUMENT")
    private byte[] document;
}
