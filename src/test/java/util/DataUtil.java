package util;

import com.google.gson.Gson;
import entities.Triangle;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DataUtil {
    private Logger logger = LogManager.getLogger();

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
        // about the extent of double precision
        DecimalFormat sf = new DecimalFormat("#.################");

        var body = new HashMap<String, String>();
        body.put("separator", separator);
        body.put("input", String.format(
                sf.format(firstSide) + "%1$s"
                        + sf.format(secondSide) + "%1$s"
                        + sf.format(thirdSide),
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
