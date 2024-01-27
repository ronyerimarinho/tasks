package br.com.marinho.tasks.service;

import br.com.marinho.tasks.exceptions.UserNotFoundException;
import br.com.marinho.tasks.model.Person;
import br.com.marinho.tasks.model.Task;
import br.com.marinho.tasks.model.enums.TaskStatusEnum;
import br.com.marinho.tasks.model.transport.operations.create.CreateTaskForm;
import br.com.marinho.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private TaskService taskService;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @Test
    @DisplayName("Create a new task with no fail")
    void create() throws UserNotFoundException {
        /* Arrange */
        String email = "marinho@gmail.com";
        Person person = new Person();
        person.setEmail(email);

        BDDMockito.given(this.personService.findByEmail(email)).willReturn(person);

        CreateTaskForm form =
                new CreateTaskForm("Atividade 01", "Descrição da atividade 01", TaskStatusEnum.NOT_STARTED);

        /* Act */
        this.taskService.create(form, person);

        /* Assert */
        BDDMockito.then(this.taskRepository).should().save(this.taskCaptor.capture());
        Task createdTask = this.taskCaptor.getValue();
        Assertions.assertEquals(form.title(), createdTask.getTitle());
        Assertions.assertEquals(form.description(), createdTask.getDescription());
        Assertions.assertEquals(form.status(), createdTask.getStatus());
        Assertions.assertEquals(person.getEmail(), createdTask.getPerson().getEmail());
    }

    @Test
    void delete() {
    }

    @Test
    void applyAssignee() {
    }

    @Test
    void list() {
    }
}