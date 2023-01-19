package dev.educosta.domain.entity.factory;

import dev.educosta.domain.entity.City;
import dev.educosta.domain.vo.Id;
import dev.educosta.domain.vo.State;

public class CityFactory {

    public static City createCity(String name, State state) {
        return new City(Id.withoutId(), name, state);
    }

    public static City createCity(String uuid, String name, String state) {
        return new City(Id.withId(uuid), name, State.valueOf(state));
    }
}
