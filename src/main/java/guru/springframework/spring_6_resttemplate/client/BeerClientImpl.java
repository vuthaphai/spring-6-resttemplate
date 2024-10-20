package guru.springframework.spring_6_resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import guru.springframework.spring_6_resttemplate.model.BeerDTO;
import guru.springframework.spring_6_resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    private static final String GET_BEER_PATH = "/api/v1/beer";



    @Override
    public Page<BeerDTO> listBeers(String beerName) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
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
