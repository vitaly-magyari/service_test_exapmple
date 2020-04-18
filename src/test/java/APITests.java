import entities.Triangle;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test_data.RequestBodies;
import test_data.TestDataProvider;

import java.util.List;
import java.util.stream.Stream;

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
    void readJson() {
        var bodies = RequestBodies.get();
        assertEquals("{\"separator\":\";\",\"input\":\"3;4;5\"}", util.generateRequestBody(3, 4, 5, ";"));
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
        String createdId = api.createTriangle(requestBodies.valid_sep_semi.asJson())
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

    boolean allTrianglesContainsId(String deletedId, List<String> triangleList) {
        for (String id : triangleList) {
            if (id.equals(deletedId)) {
                return true;
            }
        }
        return false;
    }

}
