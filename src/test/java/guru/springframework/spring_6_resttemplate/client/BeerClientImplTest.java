package guru.springframework.spring_6_resttemplate.client;

import guru.springframework.spring_6_resttemplate.model.BeerDTO;
import guru.springframework.spring_6_resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testDeleteBeer() {
        // Create new beer DTO
        BeerDTO newBeerDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("113333")
                .build();
        BeerDTO beerDto = beerClient.createBeer(newBeerDto);
        System.out.println("Created beer: " + beerDto);
        beerClient.deleteBeer(beerDto.getId());

        assertThrows(HttpClientErrorException.class,() -> {
            beerClient.getBeerById(beerDto.getId());
        });


    }

    @Test
    void testUpdateBeer() {
        // Create new beer DTO
        BeerDTO newBeerDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("113333")
                .build();
        BeerDTO beerDto = beerClient.createBeer(newBeerDto);
        System.out.println("Created beer: " + beerDto);

        // Update beer name
        final String newBeerName = "Mango Bobs 3";
        beerDto.setBeerName(newBeerName);
        BeerDTO updatedBeer = beerClient.updateBeer(beerDto);
        System.out.println("Updated beer: " + updatedBeer);

        // Verify the update
        assertEquals(newBeerName, updatedBeer.getBeerName());
    }




    @Test
    void testCreateBeer() {

        BeerDTO newBeerDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("113333")
                .build();

        BeerDTO saveBeerDto = beerClient.createBeer(newBeerDto);

        assertNotNull(saveBeerDto);

    }


    @Test
    void getBeerById() {

        Page<BeerDTO> beerDTOS = beerClient.listBeers();

        BeerDTO dto = beerDTOS.getContent().getFirst();

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }

    @Test
    void listBeersNoBeerName() {
        beerClient.listBeers(null,null ,null ,null ,null);
    }

    @Test
    void listBeers() {
        beerClient.listBeers("ALE", null ,null ,null ,null);
    }

}