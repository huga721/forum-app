package huberts.spring.forumapp;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
public class ContainerIT extends MySQLContainer<ContainerIT> implements BeforeAllCallback {

    private static final String IMAGE_VERSION = "mysql:latest";
    private static final String DATABASE_NAME = "testdb";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";

    public static ContainerIT container = new ContainerIT()
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(USERNAME)
                    .withPassword(PASSWORD);

    public ContainerIT() {
        super(IMAGE_VERSION);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        ContainerIT.container.start();
        setDataSourceProperties(ContainerIT.container);
    }

    private void setDataSourceProperties(ContainerIT registry) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
        System.setProperty("spring.datasource.driverClassName", container.getDriverClassName());
    }
}