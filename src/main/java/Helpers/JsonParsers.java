package Helpers;

import io.restassured.response.Response;

import java.util.List;

public class JsonParsers {
    public static Object receiveJsonValueFromResponse(Response response, String value) {
        return response.jsonPath().get(value);
    }

    public static List<Object> receiveJsonValuesAsListFromResponse(Response response, String value) {
        return response.jsonPath().getList(value);
    }
}
