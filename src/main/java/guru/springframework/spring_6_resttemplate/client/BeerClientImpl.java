package guru.springframework.spring_6_resttemplate.client;

import com.fasterxml.jackson.databind.JsonNode;

import guru.springframework.spring_6_resttemplate.model.BeerDTO;
import guru.springframework.spring_6_resttemplate.model.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";

//    @Override
//    public Page<BeerDTO> listBeers() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//
//        ResponseEntity<String> stringResponse =
//                restTemplate.getForEntity(BASE_URL + GET_BEER_PATH , String.class);
//
//        ResponseEntity<Map> mapResponse =
//                restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, Map.class);
//
//         ResponseEntity<JsonNode> jsonResponse =
//                restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, JsonNode.class);
//
//         jsonResponse.getBody().findPath("beer")
//                 .elements().forEachRemaining(node ->{
//                     System.out.println("Node: "+ node.get("beerName").asText());
//         });
//
//        System.out.println("stringResponse: "+ stringResponse.getBody());
//        return null;
//    }

//    @Override
//    public Page<BeerDTO> listBeers() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//
//        ResponseEntity<RestPageImpl> stringResponse =
//                restTemplate.getForEntity(BASE_URL + GET_BEER_PATH , RestPageImpl.class);
//
//        System.out.println("stringResponse: "+ stringResponse.getBody());
//        return null;
//    }

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();

//        ResponseEntity<String> response = restTemplate.exchange(
//                BASE_URL + GET_BEER_PATH,
//                HttpMethod.GET,
//                null,
//                String.class
//        );
//
//        System.out.println("Raw JSON response: " + response.getBody());

        // Log before making the request
        System.out.println("Making a request to: " + BASE_URL + GET_BEER_PATH);

        ResponseEntity<RestPageImpl<BeerDTO>> stringResponse = restTemplate.exchange(
                BASE_URL + GET_BEER_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<BeerDTO>>() {}
        );

        // Log the response headers
        HttpHeaders headers = stringResponse.getHeaders();
        MediaType contentType = headers.getContentType();
        System.out.println("Response Content-Type: " + contentType);

        // Log after receiving the response
        System.out.println("Response Body: " + stringResponse.getBody());

        return stringResponse.getBody();
    }

//    @Override
//    public Page<BeerDTO> listBeers() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                BASE_URL + GET_BEER_PATH,
//                HttpMethod.GET,
//                null,
//                String.class
//        );
//
//        System.out.println("Raw JSON response: " + response.getBody());
//
//        ResponseEntity<Map<String, Object>> stringResponse = restTemplate.exchange(
//                BASE_URL + GET_BEER_PATH,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Map<String, Object>>() {}
//        );
//
//        System.out.println("Mapped response: " + stringResponse.getBody());
//
//
//        return null;
//    }

}
