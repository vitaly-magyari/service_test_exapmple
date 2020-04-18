import entities.Triangle;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test_data.TestDataProvider;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class APITests extends TestBase {

    protected static final Logger logger = LogManager.getLogger();

    @Tag("disabled")
    @Test
    void testStub() {
        assertTrue(false);
    }

    @Test
    @Tag("debug")
    void debug() {
    }


    /**
     * using wrapper to avoid call of method by string literal in @MethodSource
     */
    private static Stream<Arguments> createTriangleTest() {
        return TestDataProvider.createTriangle();
    }

    @Tag("api")
    @DisplayName("Create triangle by request, get triangle back, check sides equality")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void createTriangleTest(String caseName, double[] sides, String sep) {
        String requestBody = util.generateRequestBody(sides[0], sides[1], sides[2], sep);
        logger.warn(requestBody);

        var createdTriResponse = api.createTriangle(requestBody);
        createdTriResponse
                .then()
                .assertThat()
                .statusCode(200);
        String id = createdTriResponse.path("id");

        Triangle createdTriangle = util.extractTriangle(createdTriResponse);
        Triangle expectedTriangle = new Triangle(sides[0], sides[1], sides[2]);
        assertEquals(expectedTriangle, createdTriangle, "created triangle sides don't match");

        Response existingTriResponse = api.getTriangle(id);
        existingTriResponse
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
        Triangle existingTriangle = util.extractTriangle(existingTriResponse);
        assertEquals(createdTriangle, existingTriangle);
    }


    @Test
    @DisplayName("Create and delete triangle, check get by id returns 404, check absence in all triangles response")
    @Tag("api")
    void deleteTriangleTest() {
        String createdId = api.createTriangle(util.generateRequestBody(3, 4, 5, ";"))
                .then().assertThat()
                .statusCode(200)
                .extract()
                .path("id");

        api.deleteTriangle(createdId)
                .then().assertThat()
                .statusCode(200);

        api.getTriangle(createdId)
                .then().assertThat()
                .statusCode(404)
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Not Found"));

        List<String> allTriangleIds = util.extractAllTrianglesIds(api.getAllTriangles());

        assertFalse(
                allTrianglesContainsId(createdId, allTriangleIds),
                String.format("deleted triangle %s still present in all triangles response", createdId));
    }


    static Stream<Arguments> roundingBorderTest() {
        return Stream.of(
                Arguments.of(10.000001f, 10.000001),
                Arguments.of(10f, 10.0000001)
        );
    }

    @Tag("api")
    @ParameterizedTest
    @MethodSource
    void roundingBorderTest(float expectedValue, double inputValue) {
        String requestBody = util.generateRequestBody(inputValue, inputValue, inputValue, null);
        logger.warn(requestBody);

        String responseBody = api.createTriangle(requestBody)
                .then().assertThat()
                .statusCode(200)
                .assertThat()
                .body("firstSide", is(expectedValue))
                .body("secondSide", is(expectedValue))
                .body("thirdSide", is(expectedValue))
                .extract()
                .body().asString();
        logger.warn(responseBody);
    }

    boolean allTrianglesContainsId(String deletedId, List<String> triangleList) {
        for (String id : triangleList) {
            if (id.equals(deletedId)) {
                return true;
            }
        }
        return false;
    }

}
