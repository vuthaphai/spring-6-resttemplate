package guru.springframework.spring_6_resttemplate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

//@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
//@JsonDeserialize(using = RestPageDeserializer.class)
//public class RestPageImpl<T>  extends PageImpl<T> {
//
//    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
//    public RestPageImpl(@JsonProperty("content") List<T> content,
//                        @JsonProperty("pageNumber") int page,
//                        @JsonProperty("pageSize") int size,
//                        @JsonProperty("totalElements") long total) {
//
//        super(content, PageRequest.of(page,size),total);
//    }
//
//    public RestPageImpl(List<T> content) {
//        super(content);
//    }
//
////    public RestPageImpl(List<BeerDTO> beers, PageRequest of, long total) {
////        super();
////    }
//}

@JsonDeserialize(using = BeerDTOPageDeserializer.class)
public class BeerDTOPageImpl<T> extends PageImpl<T> {
    public BeerDTOPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        System.out.println("content = " + content + ", pageable = " + pageable + ", total = " + total);
    }

    public BeerDTOPageImpl(List<T> content) {
        super(content);
    }
    // Your existing constructors
}

