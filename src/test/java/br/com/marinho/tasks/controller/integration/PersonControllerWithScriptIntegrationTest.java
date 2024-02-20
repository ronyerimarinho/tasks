package br.com.marinho.tasks.controller.integration;

import br.com.marinho.tasks.configuration.TestConfiguration;
import br.com.marinho.tasks.configuration.TestContainersDatabaseConfigurationWithScript;
import br.com.marinho.tasks.model.transport.PersonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerWithScriptIntegrationTest extends TestContainersDatabaseConfigurationWithScript {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

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
    }

    @Test
    @DisplayName("Get user by GUID should return success and user")
    void getUserByIntegrationTestShouldReturnSuccess() throws JsonProcessingException {
        String guid = "67db8b80-8e35-486c-8e9a-a7f3fc6e3202";
        String email = "maria.helena@example.com";
        String password = "Teste@123";

        String response = given()
                .spec(specification)
                .pathParam("id", guid)
                .auth().basic(email, password)
                .when().get("{id}").then().statusCode(200).extract().body().asString();

        PersonDTO personById = objectMapper.readValue(response, PersonDTO.class);

        Assertions.assertNotNull(personById);
        Assertions.assertNotNull(personById.guid());
        Assertions.assertNotNull(personById.name());
        Assertions.assertNotNull(personById.email());

        Assertions.assertEquals("Maria Helena", personById.name());
        Assertions.assertEquals(email, personById.email());
    }
}
