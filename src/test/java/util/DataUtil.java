package util;

import com.google.gson.Gson;
import entities.Triangle;
import io.restassured.response.Response;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.StrictMath.sqrt;

public class DataUtil {

    Gson gson = new Gson();

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

    public String generateRequestBody(Triangle inputTri, String sep) {
        return generateRequestBody(inputTri.firstSide, inputTri.secondSide, inputTri.thirdSide, sep);
    }

    public List<String> extractAllTrianglesIds(Response allTriResponse) {
        return allTriResponse
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("id");
    }

    public double calculatePerimeter(Triangle inputTri) {
        return calculatePerimeter(inputTri.firstSide, inputTri.secondSide, inputTri.thirdSide);
    }

    public double calculatePerimeter(double side1, double side2, double side3) {
        return side1 + side2 + side3;
    }

    public double calculateHeronArea(double side1, double side2, double side3) {
        double semiP = calculatePerimeter(side1, side2, side3) / 2;
        return sqrt(semiP
                * (semiP - side1)
                * (semiP - side2)
                * (semiP - side3)
        );
    }

    public double calculateHeronArea(Triangle inputTri) {
        return calculateHeronArea(inputTri.firstSide, inputTri.secondSide, inputTri.thirdSide);
    }
}
