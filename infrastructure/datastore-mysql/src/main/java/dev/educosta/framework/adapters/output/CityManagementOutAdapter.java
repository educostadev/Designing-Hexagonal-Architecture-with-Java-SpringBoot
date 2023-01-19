package dev.educosta.framework.adapters.output;

import dev.educosta.application.ports.output.CityManagementOutputPort;
import dev.educosta.domain.entity.City;
import dev.educosta.domain.entity.factory.CityFactory;
import dev.educosta.domain.vo.Id;
import dev.educosta.framework.adapters.jpa.CityDTO;
import dev.educosta.framework.adapters.jpa.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CityManagementOutAdapter implements CityManagementOutputPort {

    @Autowired
    CityRepository cityRepository;

    @Override
    public Optional<City> retrieveCity(Id id) {
        var cityDTO = cityRepository.findById(id.toString());
        return cityDTO.map(dto -> CityFactory.createCity(id.toString(), dto.getName(), dto.getState()));
    }

    @Override
    public Optional<City> removeCity(Id id) {
        var city = this.retrieveCity(id);
        cityRepository.deleteById(id.toString());
        return city;
    }


    @Override
    public City saveCity(City city) {
        CityDTO cityDTO = toDTO(city);
        cityRepository.save(cityDTO);
        return city;
    }

    private CityDTO toDTO(City city) {
        return CityDTO.builder()
                .id(city.getId().toString())
                .state(city.getState().name())
                .name(city.getName())
                .build();
    }
}
