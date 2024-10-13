package guru.springframework.spring_6_resttemplate.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestPageDeserializer extends JsonDeserializer<RestPageImpl<BeerDTO>> {

    @Override
    public RestPageImpl<BeerDTO> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        mapper.registerModule(new JavaTimeModule());  // Register JavaTimeModule with ObjectMapper

        JsonNode node = mapper.readTree(p);

        // Log the entire node structure
        System.out.println("JSON Node: " + node.toString());




        JsonNode embedded = node.get("_embedded");
        JsonNode beerArray = embedded.get("beer");

        List<BeerDTO> beers = new ArrayList<>();
        for (JsonNode beerNode : beerArray) {
            BeerDTO beer = mapper.treeToValue(beerNode, BeerDTO.class);
            beers.add(beer);
        }

        // Access the page object
        JsonNode pageNode = node.get("page");

        // Now access the properties from the pageNode
        int page = pageNode.get("number").asInt();
        int size = pageNode.get("size").asInt();
        long total = pageNode.get("totalElements").asLong();

        // Log the values retrieved
        System.out.println("Page: " + page);
        System.out.println("Size: " + size);
        System.out.println("Total: " + total);

        return new RestPageImpl<>(beers, PageRequest.of(page, size), total);

    }
}

