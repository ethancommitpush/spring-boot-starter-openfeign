package example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransformCollectionPostReqDTO extends AbstractReqDTO {
    private String name;
    private String description;

}