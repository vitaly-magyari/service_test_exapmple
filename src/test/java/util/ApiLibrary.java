package util;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiLibrary {

    protected RequestSpecification baseRequest() {
        return given()
                .header("X-User", Config.getToken())
                .header("Content-Type", "application/json");
    }

    public Response createTriangle(String requestBody) {
        return baseRequest()
                .body(requestBody)
                .post("/triangle");
    }

    public Response getTriangle(String id) {
        return baseRequest()
                .pathParam("id", id)
                .get("/triangle/{id}");
    }

    public Response deleteTriangle(String id) {
        return baseRequest()
                .pathParam("id", id)
                .delete("/triangle/{id}");
    }

    public Response getAllTriangles() {
        return baseRequest()
                .get("/triangle/all");
    }

    public void deleteAllTriangles() {
        var util = new DataUtil();
        List<String> ids = util.extractAllTrianglesIds(getAllTriangles());
        for (String id : ids) {
            deleteTriangle(id);
        }
    }


}
