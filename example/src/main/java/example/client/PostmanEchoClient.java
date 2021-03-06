package example.client;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import example.dto.TimeObjectGetRespDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
@FeignClient(url = "${postman-echo.domain}")
public interface PostmanEchoClient {

    @RequestLine("GET /time/object?timestamp={timestamp}")
    TimeObjectGetRespDTO getTimeObject(@Param("timestamp") String timestamp);

}