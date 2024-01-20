package br.com.marinho.tasks.model;

import br.com.marinho.tasks.model.enums.TaskStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String guid;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private TaskStatusEnum status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    private boolean deleted;

    @ManyToMany
    @JoinTable(name = "assignees_to_tasks",
            joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> assignees = new HashSet<>();
}
