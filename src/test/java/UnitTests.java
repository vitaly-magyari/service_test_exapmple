import entities.Triangle;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test_data.TestDataProvider;
import util.DataUtil;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTests {

    DataUtil util = new DataUtil();

    private static Stream<Arguments> testAreaCalc() {
        return TestDataProvider.areaSource();
    }

    @Tag("unit")
    @ParameterizedTest(name = "Check area of {0} is {1}")
    @MethodSource
    void testAreaCalc(Triangle tri, double expectedArea) {
        double area1 = util.calculateHeronArea(tri);

        assertEquals(area1, expectedArea);
        double area2 = util.calculateHeronArea(tri);
        assertEquals(area2, expectedArea);
    }

    private static Stream<Arguments> testFormatter() {
        return TestDataProvider.formatterSource();
    }

    @Tag("unit")
    @ParameterizedTest(name = "Test input of {0}, {1}, {2}, separator:\"{3}\" results in {4} ")
    @MethodSource
    void testFormatter(double side1, double side2, double side3, String separator,
                       String expectedStringLiteral) {
        String generated = util.generateRequestBody(side1, side2, side3, separator);
        assertEquals(expectedStringLiteral, generated);
    }
}
