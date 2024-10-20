package guru.springframework.spring_6_resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import guru.springframework.spring_6_resttemplate.model.BeerDTO;
import guru.springframework.spring_6_resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring_6_resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    private static final String GET_BEER_PATH = "/api/v1/beer";
    private static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";


    @Override
    public void deleteBeer(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(GET_BEER_BY_ID_PATH, beerId);

    }

    @Override
    public BeerDTO updateBeer(BeerDTO beerDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        System.out.println("Updating beer: " + beerDto);
        restTemplate.put(GET_BEER_BY_ID_PATH, beerDto, beerDto.getId());

        System.out.println("PUT request sent for beer: " + beerDto.getId());

        BeerDTO updatedBeer;
        for (int i = 0; i < 5; i++) {
            updatedBeer = getBeerById(beerDto.getId());
            if ("Mango Bobs 3".equals(updatedBeer.getBeerName())) {
                System.out.println("Successfully updated beer name to: " + updatedBeer.getBeerName());
                return updatedBeer;
            }

            System.out.println("Retrying to fetch updated beer, attempt: " + (i + 1));
            try {
                Thread.sleep(1000); // Adjust the delay if needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Final fetch attempt
        updatedBeer = getBeerById(beerDto.getId());
        System.out.println("Updated beer retrieved: " + updatedBeer);

        return updatedBeer;
    }



    @Override
    public BeerDTO createBeer(BeerDTO newBeerDto) {

        RestTemplate restTemplate =  restTemplateBuilder.build();

        ResponseEntity<BeerDTO> response = restTemplate.postForEntity(GET_BEER_PATH, newBeerDto,BeerDTO.class);
        URI uri = restTemplate.postForLocation(GET_BEER_PATH, newBeerDto);

        assert uri != null;
        return restTemplate.getForObject(uri.getPath(),BeerDTO.class);
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class,beerId);
    }

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeers(null,null,null,null,null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }

        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }

        if (showInventory != null) {
            uriComponentsBuilder.queryParam("showInventory", showInventory);
        }

        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }

        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }


        // Log before making the request
        System.out.println("Making a request to: " + GET_BEER_PATH);

        // Fetch as String and log raw response
        ResponseEntity<String> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), String.class);
        String json = response.getBody();
        System.out.println("Raw JSON response: " + json);

        // Parse manually
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(BeerDTOPageImpl.class, BeerDTO.class);
            BeerDTOPageImpl<BeerDTO> page = mapper.readValue(json, type);

            // Log parsed content
            System.out.println("Parsed Page Content: " + page.getContent());
            return page;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Page.empty(); // or handle the exception as needed
        }
    }



}
