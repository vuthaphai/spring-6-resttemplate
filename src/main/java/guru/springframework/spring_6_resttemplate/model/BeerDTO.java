package guru.springframework.spring_6_resttemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    @JsonCreator
    public BeerDTO(
            @JsonProperty("id") UUID id,
            @JsonProperty("version") Integer version,
            @JsonProperty("beerName") String beerName,
            @JsonProperty("beerStyle") BeerStyle beerStyle,
            @JsonProperty("upc") String upc,
            @JsonProperty("quantityOnHand") Integer quantityOnHand,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("createdDate") LocalDateTime createdDate,
            @JsonProperty("updateDate") LocalDateTime updateDate) {
        this.id = id;
        this.version = version;
        this.beerName = beerName;
        this.beerStyle = beerStyle;
        this.upc = upc;
        this.quantityOnHand = quantityOnHand;
        this.price = price;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }
    // No-args constructor
    public BeerDTO() {
    }
}
