import entities.Triangle;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    @Tag("disabled")
    @Test
    void testStub() {
        assertTrue(false);
    }

    @Test
    @Tag("debug")
    void debug() {
    }


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
    @Tag("defect")
        // I expect a non-empty response after deletion
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

    // using wrapper to avoid call of method by string literal in @MethodSource
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

        // as ids are exempt from Triangle equality
        Set<String> createdIds = created.stream()
                .map(tri -> tri.id)
                .collect(Collectors.toSet());
        Set<String> receivedIds = created.stream()
                .map(tri -> tri.id)
                .collect(Collectors.toSet());
        assertThat(createdIds, equalTo(receivedIds));
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
