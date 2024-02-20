package br.com.marinho.tasks.controller.integration;

import br.com.marinho.tasks.configuration.TestConfiguration;
import br.com.marinho.tasks.configuration.TestContainersDatabaseConfiguration;
import br.com.marinho.tasks.model.transport.PersonDTO;
import br.com.marinho.tasks.model.transport.operations.create.CreatePersonForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends TestContainersDatabaseConfiguration {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static CreatePersonForm createPersonForm;
    private static PersonDTO createdPerson;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfiguration.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        createPersonForm =
                new CreatePersonForm("Usuário Teste", "test@example.com", "Teste@123");
    }

    @Test
    @Order(1)
    @DisplayName("Create user with integration tests should return success and created user")
    void createUserByIntegrationTestShouldReturnSuccess() throws JsonProcessingException {
        String response = given()
                .spec(specification)
                .contentType(TestConfiguration.CONTENT_TYPE_AS_JSON)
                .body(createPersonForm)
                .when().post().then().statusCode(201).extract().body().asString();

        createdPerson = objectMapper.readValue(response, PersonDTO.class);

        Assertions.assertNotNull(createdPerson);
        Assertions.assertNotNull(createdPerson.guid());
        Assertions.assertNotNull(createdPerson.name());
        Assertions.assertNotNull(createdPerson.email());

        Assertions.assertEquals(createPersonForm.name(), createdPerson.name());
        Assertions.assertEquals(createPersonForm.email(), createdPerson.email());
    }

    @Test
    @Order(2)
    @DisplayName("Get user by GUID should return success and user")
    void getUserByIntegrationTestShouldReturnSuccess() throws JsonProcessingException {
        String response = given()
                .spec(specification)
                .pathParam("id", createdPerson.guid())
                .auth().basic(createPersonForm.email(), createPersonForm.password())
                .when().get("{id}").then().statusCode(200).extract().body().asString();

        PersonDTO personById = objectMapper.readValue(response, PersonDTO.class);

        Assertions.assertNotNull(personById);
        Assertions.assertNotNull(personById.guid());
        Assertions.assertNotNull(personById.name());
        Assertions.assertNotNull(personById.email());

        Assertions.assertEquals(createPersonForm.name(), personById.name());
        Assertions.assertEquals(createPersonForm.email(), personById.email());
    }
}
