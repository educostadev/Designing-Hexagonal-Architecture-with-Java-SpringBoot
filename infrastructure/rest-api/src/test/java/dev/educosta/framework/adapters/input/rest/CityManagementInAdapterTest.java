package dev.educosta.framework.adapters.input.rest;

import dev.educosta.JsonUtils;
import dev.educosta.application.usecases.CityManagementUseCase;
import dev.educosta.domain.entity.factory.CityFactory;
import dev.educosta.domain.vo.State;
import dev.educosta.framework.adapters.input.CityManagementInAdapter;
import dev.educosta.framework.adapters.input.request.CityRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CityManagementInAdapterTest {

    private MockMvc mockMvc;
    @Mock
    CityManagementUseCase cityManagementUseCase;

    @BeforeEach
    void setUp() throws Exception {
        CityManagementInAdapter adapter = new CityManagementInAdapter();
        
        // Use reflection to set the private field
        Field field = CityManagementInAdapter.class.getDeclaredField("cityManagementUseCase");
        field.setAccessible(true);
        field.set(adapter, cityManagementUseCase);
        
        mockMvc = MockMvcBuilders.standaloneSetup(adapter).build();
    }

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
