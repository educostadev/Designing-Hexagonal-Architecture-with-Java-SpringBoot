package dev.educosta.framework.adapters.input.rest;

import dev.educosta.JsonUtils;
import dev.educosta.application.usecases.CityManagementUseCase;
import dev.educosta.domain.entity.factory.CityFactory;
import dev.educosta.domain.vo.State;
import dev.educosta.framework.adapters.input.CityManagementInAdapter;
import dev.educosta.framework.adapters.input.request.CityRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
@Import(CityManagementInAdapter.class)
class CityManagementInAdapterTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CityManagementUseCase cityManagementUseCase;

    @SneakyThrows
    @Test
    void shouldReturnCityWhenCreateCityWithSuccess(){
        var request = new CityRequest("São Paulo", "SP");
        var city = CityFactory.createCity(request.name(), State.valueOf(request.state()));
        doReturn(city).when(cityManagementUseCase).createCity(anyString(), any(State.class));

        mockMvc.perform(
                        post("/api")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtils.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(city.getName())));

    }

}