package dev.educosta.framework.adapters.input.rest;

import dev.educosta.application.usescases.CityManagementUseCase;
import dev.educosta.domain.vo.Id;
import dev.educosta.domain.vo.State;
import dev.educosta.framework.adapters.input.rest.request.CityRequest;
import dev.educosta.framework.adapters.input.rest.response.CityResponse;
import jakarta.websocket.server.PathParam;
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
    public CityResponse create(@RequestBody CityRequest cityRequest){
         var city = cityManagementUseCase.createCity(cityRequest.name(), State.valueOf(cityRequest.state()));
         return new CityResponse(city.getId().getUuid(),city.getName(),city.getState().name());
    }

    @GetMapping("/{id}")
    public CityResponse retrieve(@PathVariable String id){
       return cityManagementUseCase.retrieveCity(Id.withId(id))
               .map( (city)-> new CityResponse(city.getId().getUuid(),city.getName(),city.getState().name()))
               .orElseThrow();
    }

}
