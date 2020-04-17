package util;

import com.google.gson.Gson;
import entities.Triangle;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;

public class DataUtil {

    Gson gson = new Gson();

    public Triangle extractTriangle(Response triangleResponse) {
        return triangleResponse.getBody().jsonPath().getObject(".", Triangle.class);
    }

    public List<Triangle> extractAllTriangles(Response triangleListResponse) {
        return triangleListResponse
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList(".", Triangle.class);
    }

    public String generateRequestBody(double firstSide, double secondSide, double thirdSide, String separator) {
        var body = new HashMap<String, String>();
        body.put("separator", separator);
        // find better way or better language...
        body.put("input", String.format("%1$.0f"+"%4$s"+"%2$.0f"+"%4$s"+"%3$.0f", firstSide, secondSide, thirdSide, separator));

        return gson.toJson(body);
    }
}
