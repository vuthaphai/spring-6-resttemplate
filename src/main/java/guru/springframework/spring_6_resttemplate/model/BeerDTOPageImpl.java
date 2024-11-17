package guru.springframework.spring_6_resttemplate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;


@JsonDeserialize(using = BeerDTOPageDeserializer.class)
public class BeerDTOPageImpl<T> extends PageImpl<T> {
    public BeerDTOPageImpl(List<T> content, PageRequest pageRequest, long total) {
        super(content, pageRequest, total);
    }

    // No-args constructor
    public BeerDTOPageImpl() {
        super(List.of(), PageRequest.of(0, 1), 0);
    }
}

