package dev.educosta.application.usescases;

import dev.educosta.domain.entity.City;
import dev.educosta.domain.vo.Id;
import dev.educosta.domain.vo.State;

import java.util.Optional;

public interface CityManagementUseCase {

    City createCity(String name, State state);

    Optional<City> retrieveCity(Id id);

}
