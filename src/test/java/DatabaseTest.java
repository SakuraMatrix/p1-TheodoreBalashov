import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringJUnitConfig(classes = WebConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {
    ApplicationContext context;
    WebTestClient rest;
    /*
    @BeforeAll
    public void setup() {
        rest = WebTestClient.bindToApplicationContext(context).configureClient().build();
    }
    @Test
    public void getAllUsers() {
        rest.get().uri("/user").exchange().expectStatus().isOk();
    }
    @Test
    public void getAllPaintings() {
        rest.get().uri("/paintings").exchange().expectStatus().isOk();
    }
    @Test
    public void getAllUserActions() {
        rest.get().uri("/userActivity").exchange().expectStatus().isOk();
    }
    @Test
    public void getAllPaintingActions() {
        rest.get().uri("/paintingActivity").exchange().expectStatus().isOk();
    }*/
}
