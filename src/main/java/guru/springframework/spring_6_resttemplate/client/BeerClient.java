package guru.springframework.spring_6_resttemplate.client;

import guru.springframework.spring_6_resttemplate.model.BeerDTO;
import org.springframework.data.domain.Page;

public interface BeerClient {
    Page<BeerDTO> listBeers();
}
