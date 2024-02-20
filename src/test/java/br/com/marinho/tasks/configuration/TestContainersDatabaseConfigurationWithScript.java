package br.com.marinho.tasks.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@ContextConfiguration(initializers = TestContainersDatabaseConfigurationWithScript.Initializer.class)
public class TestContainersDatabaseConfigurationWithScript {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");

        private static void initContainers() {
            postgreSQLContainer
                    .withInitScript("scripts/integrationtests.sql").start();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username", postgreSQLContainer.getUsername(),
                    "spring.datasource.password", postgreSQLContainer.getPassword());
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            initContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers =
                    new MapPropertySource("testcontainers", createConnectionConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }
    }
}
