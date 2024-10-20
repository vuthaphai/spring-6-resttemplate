package guru.springframework.spring_6_resttemplate.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class BeerDTODeserializer extends JsonDeserializer<BeerDTO> {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter();

    @Override
    public BeerDTO deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        UUID id = getUUID(node, "id");
        Integer version = getInt(node, "version");
        String beerName = getString(node, "beerName");
        BeerStyle beerStyle = getEnum(node, "beerStyle", BeerStyle.class);
        String upc = getString(node, "upc");
        Integer quantityOnHand = getInt(node, "quantityOnHand");
        BigDecimal price = getBigDecimal(node, "price");
        LocalDateTime createdDate = getLocalDateTime(node, "createdDate", formatter);
        LocalDateTime lastModifiedDate = getLocalDateTime(node, "lastModifiedDate", formatter);

        return new BeerDTO(id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, lastModifiedDate);
    }

    private UUID getUUID(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? UUID.fromString(node.get(field).asText()) : null;
    }

    private Integer getInt(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asInt() : null;
    }

    private String getString(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

    private <T extends Enum<T>> T getEnum(JsonNode node, String field, Class<T> enumType) {
        return node.has(field) && !node.get(field).isNull() ? Enum.valueOf(enumType, node.get(field).asText()) : null;
    }

    private BigDecimal getBigDecimal(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? new BigDecimal(node.get(field).asText()) : null;
    }

    private LocalDateTime getLocalDateTime(JsonNode node, String field, DateTimeFormatter formatter) {
        return node.has(field) && !node.get(field).isNull() ? LocalDateTime.parse(node.get(field).asText(), formatter) : null;
    }
}




