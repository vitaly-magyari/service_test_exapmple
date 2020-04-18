import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import util.ApiLibrary;
import util.Config;
import util.DataUtil;

import static io.restassured.RestAssured.baseURI;

public class TestBase {
    protected ApiLibrary api = new ApiLibrary();
    protected DataUtil util = new DataUtil();
    protected Logger logger = LogManager.getLogger();

    @BeforeAll
    protected static void setUp() {
        baseURI = Config.getBaseURI();
        deleteAllTriangles();
    }

    @AfterAll
    protected static void cleanUp() {
        deleteAllTriangles();
    }

    private static void deleteAllTriangles() {
        new ApiLibrary().deleteAllTriangles(); // a crutch as method has to be static
    }
}
