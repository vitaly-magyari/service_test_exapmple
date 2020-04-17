package test_data;

import entities.Triangle;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestDataProvider {
    static RequestBodies bodies = RequestBodies.get();

    public static Stream<Arguments> createTriangle() {
        return Stream.of(
                Arguments.of("Valid sides, separator ';'", new double[] {3,4,5}, ";")

//                Arguments.of(
//                        "Valid sides, separator ','",
//                        bodies.valid_sep_comma.asJson(),
//                        200,
//                        new Triangle(3,4,5)),
//
//                Arguments.of(
//                        "Valid sides, no separator provided",
//                        bodies.valid_no_sep.asJson(),
//                        200,
//                        new Triangle(3,4,5)),
//
//                Arguments.of(
//                        "Valid sides floating point sides, no sep",
//                        bodies.valid_floating_point_no_sep.asJson(),
//                        200,
//                        new Triangle(1.5,1.5,1.5)),
//
//                Arguments.of(
//                        "Valid sides, letter separator",
//                        bodies.valid_letter_separator.asJson(),
//                        200,
//                        new Triangle(3,4,5))
        );
    }
}
