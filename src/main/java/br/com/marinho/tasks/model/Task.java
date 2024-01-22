package br.com.marinho.tasks.model;

import br.com.marinho.tasks.model.enums.TaskStatusEnum;
import br.com.marinho.tasks.model.transport.operations.create.CreateTaskForm;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    @ManyToMany
    @JoinTable(name = "assignees_to_tasks",
            joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> assignees = new HashSet<>();

    public Task() {

    }

    public Task(CreateTaskForm form, Person person) {
        this.guid = UUID.randomUUID().toString();
        this.title = form.title();
        this.description = form.description();
        this.status = form.status();
        this.createdAt = LocalDateTime.now();
        this.deleted = false;
        this.person = person;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Person> getAssignees() {
        return assignees;
    }
}
