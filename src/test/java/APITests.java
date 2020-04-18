import entities.Triangle;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import test_data.TestDataProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class APITests extends TestBase {

    // using wrapper to avoid call of method by string literal in @MethodSource
    private static Stream<Arguments> createTriangleTest() {
        return TestDataProvider.createTriangle();
    }

    @Tag("api")
    @DisplayName("Create triangle by request, get triangle back, check sides equality")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void createTriangleTest(String caseName, double[] sides, String sep) {
        String requestBody = util.generateRequestBody(sides[0], sides[1], sides[2], sep);

        var createdTriResponse = api.createTriangle(requestBody);
        Triangle createdTriangle = createdTriResponse
                .then()
                .log().body(true)
                .assertThat()
                .statusCode(200)
                .extract().body().as(Triangle.class);
        String id = createdTriResponse.path("id");

        Triangle expectedTriangle = new Triangle(sides[0], sides[1], sides[2]);
        assertEquals(expectedTriangle, createdTriangle, "created triangle sides don't match");

        Response existingTriResponse = api.getTriangle(id);
        Triangle receivedTriangle = existingTriResponse
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(id))
                .extract().body().as(Triangle.class);
        assertEquals(createdTriangle, receivedTriangle);

        api.deleteTriangle(id)
                .then().statusCode(200);
    }


    @Test
    @DisplayName("Create and delete triangle, check get by id returns 404, check absence in all triangles response")
    @Tag("api")
    @Tag("blocked")
    void deleteTriangleTest() {
        String createdId = api.createTriangle(util.generateRequestBody(3, 4, 5, ";"))
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("id");

        api.deleteTriangle(createdId)
                .then()
                .log().status()
                .assertThat().statusCode(200)
                .assertThat().body(is(not(emptyString())));

        api.getTriangle(createdId)
                .then()
                .log().status()
                .assertThat()
                .statusCode(404)
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Not Found"));

        List<String> allTriangleIds = util.extractAllTrianglesIds(api.getAllTriangles());

        assertFalse(
                allTrianglesContainsId(createdId, allTriangleIds),
                String.format("deleted triangle %s still present in all triangles response", createdId));
    }

    public static Stream<Arguments> roundingBorderTest() {
        return TestDataProvider.roundingBorder();
    }

    @Tag("api")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    void roundingBorderTest(String caseName, double expectedValue, double inputValue) {
        String requestBody = util.generateRequestBody(inputValue, inputValue, inputValue, null);

        api.createTriangle(requestBody)
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstSide", equalTo(expectedValue))
                .body("secondSide", equalTo(expectedValue))
                .body("thirdSide", equalTo(expectedValue))
                .log().body();
    }

    @Tag("api")
    @Test
    void allTrianglesTest() {
        String reqBody1 = util.generateRequestBody(3, 4, 5, ";");
        String reqBody2 = util.generateRequestBody(5, 6, 7, ";");

        Set<Triangle> created = new HashSet<>();
        created.add(
                api.createTriangle(reqBody1)
                        .then().assertThat().statusCode(200)
                        .extract().body().as(Triangle.class));

        created.add(
                api.createTriangle(reqBody2)
                        .then().assertThat().statusCode(200)
                        .extract().body().as(Triangle.class));

        Response allTrianglesResponse = api.getAllTriangles()
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .extract().response();

        Set<Triangle> received = new HashSet<Triangle>(
                util.extractAllTriangles(allTrianglesResponse));

        assertThat(created, equalTo(received));

        // as ids are exempt from Triangle equality, verify them separately
        Set<String> createdIds = created.stream()
                .map(tri -> tri.id)
                .collect(Collectors.toSet());
        Set<String> receivedIds = created.stream()
                .map(tri -> tri.id)
                .collect(Collectors.toSet());
        assertThat(createdIds, equalTo(receivedIds));
    }

    @Tag("api")
    @Tag("blocked")
    @DisplayName("Separator tests")
    @ParameterizedTest(name = "Test separator \"{0}\"")
    @ValueSource(strings = {
            "}", "!", "%", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{",
            "[", "]", "+", "-", "_", "=", "/", "\\", "\n", "\r", "\t", ".",
            ",", "<", ">", "~", "`", "\"", "'", "", "ab"
    })
    void separatorTest(String sep) {
        String reqBody = util.generateRequestBody(10.1, 10.01, 10.001, sep);
        String id = api.createTriangle(reqBody)
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .extract().path("id");

        api.deleteTriangle(id)
                .then().statusCode(200);
    }

    private static Stream<Arguments> areaTest() {
        return TestDataProvider.areaSource();
    }

    @Tag("api")
    @MethodSource
    @ParameterizedTest(name = "Test area of {0} is {1}")
    void areaTest(Triangle triangle, double expectedArea) {
        String id = api.createTriangle(util.generateRequestBody(triangle, ";"))
                .then().assertThat().statusCode(200)
                .extract().path("id");

        api.getArea(id)
                .then()
                .log().status()
                .log().body()
                .assertThat().statusCode(200)
                .assertThat().body("result", equalTo(expectedArea));
    }

    private static Stream<Arguments> perimeterTest() {
        return TestDataProvider.perimeterSource();
    }

    @Tag("api")
    @MethodSource
    @ParameterizedTest(name = "Test perimeter of {0} is {1}")
    void perimeterTest(Triangle triangle, double expectedPerimeter) {
        String id = api.createTriangle(util.generateRequestBody(triangle, ";"))
                .then().assertThat().statusCode(200)
                .extract().path("id");

        api.getPerimeter(id)
                .then()
                .log().status()
                .log().body()
                .assertThat().statusCode(200)
                .assertThat().body("result", equalTo(expectedPerimeter));
    }

    @Tag("api")
    @Tag("blocked")
    @ParameterizedTest(name = "Invalid sides combinations: {arguments}")
    @DisplayName("Zero length sides, negative sides, sides equal or greater than sum of other two")
    @CsvSource({
            "0,1,1", "1,0,1", "1,1,0",
            "-3,4,5", "4,-5,6", "5,6,-7",
            "1,1,2", "1,1,3"
    })
    void invalidSidesCombinationTests(double side1, double side2, double side3) {
        String reqBody = util.generateRequestBody(side1, side2, side3, ";");

        api.createTriangle(reqBody)
                .then()
                .log().all()
                .assertThat().statusCode(422)
                .assertThat().body("error", containsString("Unprocessable Entity"))
                .assertThat().body("message", containsString("Cannot process input"))
                .assertThat().body("exception", containsString("UnprocessableDataException"));
    }

    private static Stream<Arguments> cornerCasesPostTest() {
        return TestDataProvider.cornerCases();
    }

    @Tag("api")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    void cornerCasesPostTest(String caseName,String requestBody, int statusCode) {
        api.createTriangle(requestBody)
                .then()
                .log().status()
                .log().body()
                .assertThat().statusCode(statusCode);
    }

    boolean allTrianglesContainsId(String idToFind, List<String> triangleList) {
        for (String id : triangleList) {
            if (id.equals(idToFind)) {
                return true;
            }
        }
        return false;
    }

}
