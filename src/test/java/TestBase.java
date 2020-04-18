import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import util.ApiLibrary;
import util.Config;
import util.DataUtil;

import static io.restassured.RestAssured.baseURI;

public class TestBase {
    protected ApiLibrary api = new ApiLibrary();
    protected DataUtil util = new DataUtil();


    @BeforeAll
    protected static void setUp() {

        baseURI = Config.getBaseURI();
    }

    @AfterAll
    protected static void cleanUp() {
        new ApiLibrary().deleteAllTriangles(); // a crutch as method has to be static

    }
}
