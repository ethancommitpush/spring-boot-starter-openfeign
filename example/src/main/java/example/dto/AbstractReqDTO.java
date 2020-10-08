package example.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class AbstractReqDTO {

    private static ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Override
    public final String toString() {
        String body = null;
        try {
            body = ow.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;

    }

}