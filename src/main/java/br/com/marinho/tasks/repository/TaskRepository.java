package br.com.marinho.tasks.repository;

import br.com.marinho.tasks.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findAllByDeletedFalse();

    Optional<Task> findByGuidAndDeletedFalse(String guid);
}
