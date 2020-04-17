package util;

import entities.Triangle;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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
        DataUtil responseUtil = new DataUtil();
        for (Triangle tri : responseUtil.extractAllTriangles(getAllTriangles())) {
            deleteTriangle(tri.id);
        }
    }

}
