package dev.educosta.application.ports.input;

import dev.educosta.application.ports.output.CityManagementOutputPort;
import dev.educosta.application.usescases.CityManagementUseCase;
import dev.educosta.domain.entity.City;
import dev.educosta.domain.entity.factory.CityFactory;
import dev.educosta.domain.vo.Id;
import dev.educosta.domain.vo.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CityManagementInputPort implements CityManagementUseCase {

    @Autowired
    CityManagementOutputPort cityManagementOutputPort;

    @Override
    public City createCity(String name, State state) {
        var city = CityFactory.createCity(name, state);
        return cityManagementOutputPort.saveCity(city);
    }

    @Override
    public Optional<City> retrieveCity(Id id) {
        return cityManagementOutputPort.retrieveCity(id);
    }
}
