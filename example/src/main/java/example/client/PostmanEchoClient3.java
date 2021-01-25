package example.client;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import example.dto.HeadersGetRespDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
@FeignClient(url = "${postman-echo.domain}")
public interface PostmanEchoClient3 {

    @Headers({"my-sample-header: {mySampleHeader}"})
    @RequestLine("GET /headers")
    HeadersGetRespDTO getHeaders(@Param("mySampleHeader") String mySampleHeader);

}