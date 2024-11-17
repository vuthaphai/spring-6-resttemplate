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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BeerDTOPageDeserializer extends JsonDeserializer<BeerDTOPageImpl<BeerDTO>> {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .appendPattern("XXX") // for timezone offset
            .toFormatter();

    @Override
    public BeerDTOPageImpl<BeerDTO> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        mapper.registerModule(new JavaTimeModule());
        JsonNode node = mapper.readTree(p);

        // Log the entire node structure
        System.out.println("JSON Node: " + node.toString());

//        JsonNode contentNode = node.get("_embedded");
        JsonNode contentNode = node.get("_embedded");
        if (contentNode == null) {
            contentNode = node.get("content");
            if (contentNode == null) {
                throw new JsonProcessingException("Missing '_embedded' node in JSON") {
                };
            }

        }

        List<BeerDTO> beers = new ArrayList<>();
        JsonNode content = contentNode.get("beer");
        if (content == null) {
            content = contentNode;
        }

        for (JsonNode beerNode : content) {

            UUID id = getUUID(beerNode, "id");
            if (id == null) {
                String href = beerNode.get("_links").get("self").get("href").asText();
                id = UUID.fromString(href.substring(href.lastIndexOf('/') + 1)); // Extract UUID from href
            }

            String beerName = getString(beerNode, "beerName");
            String beerStyle = getString(beerNode, "beerStyle");
            String upc = getString(beerNode, "upc");
            Integer quantityOnHand = getInt(beerNode, "quantityOnHand");
            BigDecimal price = getBigDecimal(beerNode, "price");
            OffsetDateTime createdDate = getOffsetDateTime(beerNode, "createdDate", formatter);
            OffsetDateTime lastModifiedDate = getOffsetDateTime(beerNode, "lastModifiedDate", formatter);

            beers.add(new BeerDTO(id, null, beerName, BeerStyle.valueOf(beerStyle), upc, quantityOnHand, price, createdDate, lastModifiedDate));
        }

        JsonNode pageNode = node.get("page");

        if (pageNode == null) {
            pageNode = node.get("pageable");
            if (pageNode == null) {
                throw new JsonProcessingException("Missing 'page' node in JSON") {
                };
            }
        }

        int page = pageNode.has("number") && !pageNode.get("number").isNull() ? pageNode.get("number").asInt() : 0;
        int size = pageNode.has("size") && !pageNode.get("size").isNull() ? pageNode.get("size").asInt() : 1;
        long total = pageNode.has("totalElements") && !pageNode.get("totalElements").isNull() ? pageNode.get("totalElements").asLong() : 0L;

        // Log the values retrieved
        System.out.println("Page: " + page);
        System.out.println("Size: " + size);
        System.out.println("Total: " + total);

        return new BeerDTOPageImpl<>(beers, PageRequest.of(page, size), total);
    }

    private UUID getUUID(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? UUID.fromString(node.get(field).asText()) : null;
    }

    private String getString(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

    private Integer getInt(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asInt() : null;
    }

    private BigDecimal getBigDecimal(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? new BigDecimal(node.get(field).asText()) : null;
    }

    private OffsetDateTime getOffsetDateTime(JsonNode node, String field, DateTimeFormatter formatter) {
        return node.has(field) && !node.get(field).isNull() ? OffsetDateTime.parse(node.get(field).asText(), formatter) : null;
    }


}
