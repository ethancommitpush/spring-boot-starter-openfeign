package example.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class HeadersGetRespDTO {
    private Map<String, Object> headers = new HashMap<String, Object>();
}
