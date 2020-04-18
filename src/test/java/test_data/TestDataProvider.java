package test_data;

import entities.Triangle;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestDataProvider {

    private static final double LAST_VALID_DOUBLE = 10.000000000000005;

    private static final String COMMA_SEP_PADDED_SPACE = "{ \"input\": \"10.1, 10.01, 10.001\", \"separator\":\",\"}";
    private static final String HTML_DOC = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
    private static final String SQL_QUERY = "SELECT * FROM Users;";

    public static Stream<Arguments> createTriangle() {
        return Stream.of(
                Arguments.of("Valid sides, separator ';'", new double[]{3, 4, 5}, ";"),
                Arguments.of("Valid sides, separator ','", new double[]{10.1, 10.01, 10.001}, null),
                Arguments.of("Valid sides, separator ','", new double[]{3, 4, 5}, null),
                Arguments.of("Single digit valid sides, zero-length sep", new double[]{4, 5, 6}, "")
        );
    }

    public static Stream<Arguments> roundingBorder() {
        return Stream.of(
                Arguments.of("Positive border of comparison",
                        LAST_VALID_DOUBLE, LAST_VALID_DOUBLE),
                Arguments.of("Negative border of comparison - adjacent numbers are no longer distinct for comparison",
                        10.000000000000004, 10.000000000000003)
        );
    }

    public static Stream<Arguments> areaSource() {
        return Stream.of(
                Arguments.of(new Triangle(3, 4, 5), 6),
                Arguments.of(new Triangle(5, 6, 7), 14.696938456699069),
                Arguments.of(new Triangle(10.1, 10.01, 10.001), 43.61968625973984),
                Arguments.of(new Triangle(0, 1, 1), 0));
    }

    public static Stream<Arguments> perimeterSource() {
        return Stream.of(
                Arguments.of(new Triangle(3, 4, 5), 12),
                Arguments.of(new Triangle(10.1, 10.01, 10.001), 30.110999999999997));
    }

    public static Stream<Arguments> formatterSource() {
        return Stream.of(
                Arguments.of(10.1, 10.01, 10.001, ",",
                        "{\"input\":\"10.1,10.01,10.001\",\"separator\":\",\"}"),
                Arguments.of(LAST_VALID_DOUBLE, LAST_VALID_DOUBLE, LAST_VALID_DOUBLE, ";",
                        "{\"input\":\"10.000000000000005;10.000000000000005;10.000000000000005\",\"separator\":\";\"}"),
                Arguments.of(10.1, 10.01, 10.001, null,
                        "{\"input\":\"10.1;10.01;10.001\"}")
        );
    }

    public static Stream<Arguments> cornerCases() {
        return Stream.of(
                Arguments.of("Space after separator", COMMA_SEP_PADDED_SPACE, 200),
                Arguments.of("Post HTML", HTML_DOC, 400),
                Arguments.of("Post SQL query", SQL_QUERY, 400)
        );
    }
}
