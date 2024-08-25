package com.example.rev_task_management_project02.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long clientId;

    @Column(name = "client_name", nullable = false, length = 50)
    private String clientName;

    @Column(name = "client_company_name", nullable = false, length = 100)
    private String clientCompanyName;

    @Column(name = "client_email", nullable = false, length = 100, unique = true)
    private String clientEmail;

    @Column(name = "client_phone", nullable = false, length = 20)
    private String clientPhone;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;
}
