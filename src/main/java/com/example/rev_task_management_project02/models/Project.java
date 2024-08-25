package com.example.rev_task_management_project02.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long projectId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private java.sql.Date startDate;

    @Column(name = "end_date")
    private java.sql.Date endDate;

    @Column(name = "percentage_left", nullable = false)
    private float percentageLeft;

    @ManyToOne
    @JoinColumn(name = "manager_id",nullable = false)
    private User manager;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> tasks;
}
