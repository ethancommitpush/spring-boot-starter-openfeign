package example.dto;

import lombok.Data;

@Data
public class TransformCollectionPostRespDTO {
    private Info info;

    @Data
    public static class Info {
        private String name;
        private String description;
    }
}