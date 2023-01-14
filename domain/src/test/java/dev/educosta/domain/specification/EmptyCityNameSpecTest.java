package dev.educosta.domain.specification;

import dev.educosta.domain.exception.GenericSpecificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmptyCityNameSpecTest {

    @ParameterizedTest
    @ValueSource(
            strings = {"", " ", "null"})
    void shouldThrowErrorWhenInvalidCityName(String name) throws Exception {
        var spec = new EmptyCityNameSpec();
        Assertions.assertThrows(
                GenericSpecificationException.class,
                () -> spec.check("null".equals(name) ? null : name)
        );
    }

}