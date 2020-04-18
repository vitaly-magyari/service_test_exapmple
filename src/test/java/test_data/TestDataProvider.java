package test_data;

import entities.Triangle;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestDataProvider {
    static RequestBodies bodies = RequestBodies.get();

    public static Stream<Arguments> createTriangle() {
        return Stream.of(
//                Arguments.of("Valid sides, separator ';'", new double[]{3, 4, 5}, ";"),
//                Arguments.of("Valid sides, separator ','", new double[]{3, 4, 5}, ","),
//                Arguments.of("Valid sides, separator ','", new double[]{3, 4, 5}, null),
//                Arguments.of("Valid sides floating point sides, no sep", new double[]{1.5, 1.55, 2.005}, null),
                Arguments.of("Valid, sequence of letters as separator", new double[]{5,6,7}, "ab")
//                Arguments.of(
//                        "Valid sides, letter separator",
//                        bodies.valid_letter_separator.asJson(),
//                        200,
//                        new Triangle(3,4,5))
        );
    }
}
