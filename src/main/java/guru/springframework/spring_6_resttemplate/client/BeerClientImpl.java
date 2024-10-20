package guru.springframework.spring_6_resttemplate.client;

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

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";



    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();



        // Log before making the request
        System.out.println("Making a request to: " + BASE_URL + GET_BEER_PATH);

        ResponseEntity<BeerDTOPageImpl<BeerDTO>> stringResponse = restTemplate.exchange(
                BASE_URL + GET_BEER_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BeerDTOPageImpl<BeerDTO>>() {}
        );

        // Log the response headers
        HttpHeaders headers = stringResponse.getHeaders();
        MediaType contentType = headers.getContentType();
        System.out.println("Response Content-Type: " + contentType);

        // Log after receiving the response
        System.out.println("Response Body: " + stringResponse.getBody());

        return stringResponse.getBody();
    }



}
