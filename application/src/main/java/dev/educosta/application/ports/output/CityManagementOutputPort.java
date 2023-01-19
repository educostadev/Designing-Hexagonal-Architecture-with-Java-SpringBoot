package dev.educosta.application.ports.output;

import dev.educosta.domain.entity.City;
import dev.educosta.domain.vo.Id;

import java.util.Optional;

public interface CityManagementOutputPort {

    Optional<City> retrieveCity(Id id);

    Optional<City> removeCity(Id id);

    City saveCity(City city);

}
