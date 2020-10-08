package example.dto;

import lombok.Data;

@Data
public class PostPostRespDTO {

    private Headers headers = new Headers();
    private String url;

    @Data
    public static class Headers {
        private String host;
        private String accept;
    }

}