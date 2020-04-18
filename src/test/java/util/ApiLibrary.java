package util;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.DOUBLE;

public class ApiLibrary {
    Logger logger = LogManager.getLogger();

    protected RequestSpecification baseRequest() {
        return given()
                .config(config().jsonConfig(jsonConfig().numberReturnType(DOUBLE)))
                .header("X-User", Config.getToken())
                .header("Content-Type", "application/json");
    }

    public Response createTriangle(String requestBody) {
        logger.info("createTriangle");
        return baseRequest()
                .log().method()
                .log().body()
                .body(requestBody)
                .post("/triangle");
    }

    public Response getTriangle(String id) {
        logger.info("getTriangle " + id);
        return baseRequest()
                .log().method()
                .pathParam("id", id)
                .get("/triangle/{id}");
    }

    public Response deleteTriangle(String id) {
        logger.info("deleteTriangle "+ id);
        return baseRequest()
                .log().method()
                .pathParam("id", id)
                .delete("/triangle/{id}");
    }

    public Response getAllTriangles() {
        logger.info("getAllTriangles");
        return baseRequest()
                .log().method()
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
