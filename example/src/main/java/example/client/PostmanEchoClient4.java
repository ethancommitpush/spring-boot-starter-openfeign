package example.client;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import example.dto.TransformCollectionPostReqDTO;
import example.dto.TransformCollectionPostRespDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
@FeignClient(url = "${postman-echo.domain}")
public interface PostmanEchoClient4 {

    @RequestLine("POST /transform/collection?from={from}&to={to}")
    TransformCollectionPostRespDTO postTransformCollection(@Param("from") int from, @Param("to") int to, TransformCollectionPostReqDTO body);

}
