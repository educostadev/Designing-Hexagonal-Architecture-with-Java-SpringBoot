package dev.educosta.framework.adapters.input;

import dev.educosta.application.usecases.CityManagementUseCase;
import dev.educosta.domain.vo.Id;
import dev.educosta.domain.vo.State;
import dev.educosta.framework.adapters.input.request.CityRequest;
import dev.educosta.framework.adapters.input.response.CityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class CityManagementInAdapter {

    @Autowired
    CityManagementUseCase cityManagementUseCase;

    @PostMapping
    public CityResponse create(@RequestBody CityRequest cityRequest) {
        var city = cityManagementUseCase.createCity(cityRequest.name(), State.valueOf(cityRequest.state()));
        return new CityResponse(city.getId().getUuid(), city.getName(), city.getState().name());
    }

    @GetMapping("/{id}")
    public CityResponse retrieve(@PathVariable String id) {
        return cityManagementUseCase.retrieveCity(Id.withId(id))
                .map((city) -> new CityResponse(city.getId().getUuid(), city.getName(), city.getState().name()))
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public CityResponse delete(@PathVariable String id) {
        return cityManagementUseCase.removeCity(Id.withId(id))
                .map((city) -> new CityResponse(city.getId().getUuid(), city.getName(), city.getState().name()))
                .orElse(null);
    }

}
