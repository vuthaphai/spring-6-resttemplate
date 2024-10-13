package guru.springframework.spring_6_resttemplate.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {
    @Autowired
    BeerClientImpl beerClient;

    @Test
    void listBeers() {
        beerClient.listBeers();
    }
}