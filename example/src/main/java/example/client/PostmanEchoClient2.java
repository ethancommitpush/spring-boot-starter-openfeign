package example.client;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import example.dto.PostPostRespDTO;
import feign.Param;
import feign.RequestLine;

@FeignClient(url = "${postman-echo.domain}", decoder = "myDecoder")
public interface PostmanEchoClient2 {

    @RequestLine("POST /post?foo1={foo1}&foo2={foo2}")
    PostPostRespDTO postPost(@Param("foo1") String foo1, @Param("foo2") String foo2);

}