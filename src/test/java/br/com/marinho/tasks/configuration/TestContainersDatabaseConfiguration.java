package br.com.marinho.tasks.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = TestContainersDatabaseConfiguration.Initializer.class)
public class TestContainersDatabaseConfiguration {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");

        private static void initContainers() {
            Startables.deepStart(Stream.of(postgreSQLContainer)).join();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username", postgreSQLContainer.getUsername(),
                    "spring.datasource.password", postgreSQLContainer.getPassword());
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            /* Inicializa o contêiner com o banco PostgreSQL */
            initContainers();

            /* Recupera as configurações de ambiente */
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            /* Cria novas configurações para adicionar ao application.yml */
            MapPropertySource testContainers =
                    new MapPropertySource("testcontainers", createConnectionConfiguration());

            /* Adiciona as propriedades de configuração do banco dinamicamente */
            environment.getPropertySources().addFirst(testContainers);
        }
    }
}
