package dev.educosta.framework.adapters.output;

import dev.educosta.application.ports.output.CityManagementOutputPort;
import dev.educosta.domain.entity.City;
import dev.educosta.domain.vo.Id;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CityManagementOutAdapter implements CityManagementOutputPort {

    Map<Id, City> memory = new HashMap<>();

    @Override
    public Optional<City> retrieveCity(Id id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public Optional<City> removeCity(Id id) {
        return Optional.ofNullable(memory.remove(id));
    }

    @Override
    public City saveCity(City city) {
        memory.put(city.getId(), city);
        return city;
    }
}
