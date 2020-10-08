package example.client;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import example.dto.HeadersGetRespDTO;
import example.dto.TimeObjectGetRespDTO;
import example.dto.TransformCollectionPostReqDTO;
import example.dto.TransformCollectionPostRespDTO;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
@FeignClient(url = "${postman-echo.domain}")
public interface PostmanEchoClient {

    @RequestLine("GET /time/object?timestamp={timestamp}")
    TimeObjectGetRespDTO getTimeObject(@Param("timestamp") String timestamp);

    @Headers({"my-sample-header: {mySampleHeader}"})
    @RequestLine("GET /headers")
    HeadersGetRespDTO getHeaders(@Param("mySampleHeader") String mySampleHeader);

    @RequestLine("POST /transform/collection?from={from}&to={to}")
    @Body("{body}")
    TransformCollectionPostRespDTO postTransformCollection(@Param("from") int from, @Param("to") int to, @Param("body") TransformCollectionPostReqDTO body);

}