package util;

import com.google.gson.Gson;
import entities.Triangle;
import io.restassured.response.Response;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DataUtil {

    private static final String INPUT_FORMAT_DOUBLE = "%1$f" + "%4$s" + "%2$f" + "%4$s" + "%3$f";
    private static final String INPUT_FORMAT_ROUND_TO_INT = "%1$.0f" + "%4$s" + "%2$.0f" + "%4$s" + "%3$.0f";
    Gson gson = new Gson();

    public Triangle extractTriangle(Response triangleResponse) {
        return triangleResponse
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getObject(".", Triangle.class);
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
        return generateRequestBody(firstSide, secondSide, thirdSide, separator, false);
    }

    public String generateRequestBody(double firstSide, double secondSide, double thirdSide, String separator, boolean showTrailingZeros) {
        String formatter = showTrailingZeros ?
                INPUT_FORMAT_ROUND_TO_INT
                : INPUT_FORMAT_DOUBLE;

        var body = new HashMap<String, String>();
        body.put("separator", separator);
        body.put("input", String.format(
                formatter,
                firstSide, secondSide, thirdSide,
                Objects.nonNull(separator) ? separator : ";")
        );

        return gson.toJson(body);
    }

    public List<String> extractAllTrianglesIds(Response allTriResponse) {
        return allTriResponse
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("id");
    }
}
