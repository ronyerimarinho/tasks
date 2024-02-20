package br.com.marinho.tasks.controller;

import br.com.marinho.tasks.model.transport.PersonDTO;
import br.com.marinho.tasks.model.transport.operations.create.CreatePersonForm;
import br.com.marinho.tasks.service.PersonService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private Gson gson;

    @BeforeEach
    void configure() {
        this.gson = new Gson();
    }

    @Test
    @WithMockUser(username = "teste", password = "teste")
    void getUserWithAuthenticationReturnsOk() throws Exception {
        /* Arrange */
        String id = UUID.randomUUID().toString();
        PersonDTO personDTO = new PersonDTO(id, "Usuário teste", "teste@example.com");

        BDDMockito.given(this.personService.getByGuid(id)).willReturn(personDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/person/%s", id)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserWithoutAuthenticationReturnsUnauthorized() throws Exception {
        /* Arrange */
        String id = UUID.randomUUID().toString();
        PersonDTO personDTO = new PersonDTO(id, "Usuário teste", "teste@example.com");

        BDDMockito.given(this.personService.getByGuid(id)).willReturn(personDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/person/%s", id)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Create user with empty body returns 400")
    void createWithEmptyBodyWhoExpectsBadRequest() throws Exception {
        /* Arrange */
        CreatePersonForm form = new CreatePersonForm("", null, null);
        String formAsJson = this.gson.toJson(form);

        /* Act */
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .post("/person")
                .content(formAsJson)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        /* Assert */
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Create user returns 201")
    void createExpectsSuccessfully() throws Exception {
        /* Arrange */
        CreatePersonForm form = new CreatePersonForm("Usuário Teste", "teste@example.com", "UmaSenhaForte");
        String formAsJson = this.gson.toJson(form);

        BDDMockito.given(this.personService.create(form)).willReturn(new PersonDTO(UUID.randomUUID().toString(), form.name(), form.email()));

        /* Act */
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/person")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

    }
}