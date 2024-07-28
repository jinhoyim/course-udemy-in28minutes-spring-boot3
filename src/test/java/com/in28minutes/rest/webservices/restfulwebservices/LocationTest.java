package com.in28minutes.rest.webservices.restfulwebservices;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationTest {
    @ParameterizedTest
    @MethodSource("locationProvider")
    void testLocation(Location location) {
        assertThat(location).isNotNull()
                .asString().matches("http://localhost:8080/users/\\d+");
    }

    static Stream<Location> locationProvider() {
        return Stream.of(
                new Location("http://localhost:8080/users", 1),
                new Location("http://localhost:8080/users", 2)
//                ,null
        );
    }

    @ParameterizedTest
    @MethodSource("numberProvider")
    void testLocation(Integer id) {
        assertThat(id).isNull();
    }

    static Stream<Integer> numberProvider() {
        return Stream.of(
                null, null
        );
    }
}

class Location {

    private final String baseUrl;
    private final Integer id;

    public Location(String baseUrl, Integer id) {
        this.baseUrl = baseUrl;
        this.id = id;
    }

    @Override
    public String toString() {
        return baseUrl + "/" + id;
    }
}
