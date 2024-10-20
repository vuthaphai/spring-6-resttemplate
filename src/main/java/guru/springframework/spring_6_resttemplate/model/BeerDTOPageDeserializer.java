package guru.springframework.spring_6_resttemplate.model;

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

public class BeerDTOPageDeserializer extends JsonDeserializer<BeerDTOPageImpl<BeerDTO>> {

    @Override
    public BeerDTOPageImpl<BeerDTO> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        mapper.registerModule(new JavaTimeModule());  // Register JavaTimeModule with ObjectMapper

        JsonNode node = mapper.readTree(p);

        // Log the entire node structure
        System.out.println("JSON Node: " + node.toString());




        JsonNode beerArray = node.get("content");
        List<BeerDTO> beers = new ArrayList<>();
        for (JsonNode beerNode : beerArray) {
            BeerDTO beer = mapper.treeToValue(beerNode, BeerDTO.class);
            beers.add(beer);
        }

        // Ensure 'page' node exists before accessing
        JsonNode pageableNode = node.get("pageable");
        if (pageableNode == null) {
            throw new JsonProcessingException("Missing 'pageable' node in JSON") {};
        }

        // Now access the properties from the pageNode

        int page = pageableNode.has("pageNumber") && !pageableNode.get("pageNumber").isNull() ? pageableNode.get("pageNumber").asInt() : 0;
        int size = pageableNode.has("pageSize") && !pageableNode.get("pageSize").isNull() ? pageableNode.get("pageSize").asInt() : 0;
        long total = node.has("totalElements") && !node.get("totalElements").isNull() ? node.get("totalElements").asLong() : 0L;


        // Log the values retrieved
        System.out.println("Page: " + page);
        System.out.println("Size: " + size);
        System.out.println("Total: " + total);

        return new BeerDTOPageImpl<>(beers, PageRequest.of(page, size), total);
//        return null;


    }
}

