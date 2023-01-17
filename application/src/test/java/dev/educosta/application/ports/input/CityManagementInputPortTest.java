package dev.educosta.application.ports.input;

import dev.educosta.application.ports.output.CityManagementOutputPort;
import dev.educosta.domain.vo.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CityManagementInputPortTest {

    @Mock
    CityManagementOutputPort cityManagementOutputPort;

    @InjectMocks
    CityManagementInputPort cityManagementInputPort;


    @Test
    void shouldCreateCityWithSuccess(){

        var cityName = "Rio de Janeiro";
        var state = State.RJ;
        when(cityManagementOutputPort.saveCity(Mockito.any())).thenAnswer(input -> {
            return input.getArgument(0);
        });

        var cityCreated = cityManagementInputPort.createCity(cityName, state);

        Assertions.assertEquals(cityName,cityCreated.getName());
        Assertions.assertEquals(state,cityCreated.getState());
    }
}