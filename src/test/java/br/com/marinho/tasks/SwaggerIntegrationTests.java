package br.com.marinho.tasks;

import br.com.marinho.tasks.configuration.TestConfiguration;
import br.com.marinho.tasks.configuration.TestContainersDatabaseConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTests extends TestContainersDatabaseConfiguration {

    @Test
    @DisplayName("When application starts, swagger UI page should be available")
    void shouldDisplaySwaggerUIPage() {
        String response = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfiguration.SERVER_PORT)
                .when().get().then().statusCode(200).extract().body().asString();

        Assertions.assertTrue(response.contains("Swagger UI"));
    }

}
