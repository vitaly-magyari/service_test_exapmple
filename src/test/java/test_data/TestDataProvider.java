package test_data;

import entities.Triangle;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestDataProvider {

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
                Arguments.of("Last valid comparison",
                        10.000000000000005, 10.000000000000005),
                Arguments.of("Adjacent numbers are no longer distinct for comparison",
                        10.000000000000004, 10.000000000000003)
        );
    }
}
