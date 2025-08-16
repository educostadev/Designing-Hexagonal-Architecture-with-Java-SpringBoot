package dev.educosta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import dev.educosta.framework.adapters.jpa.CityRepository;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
	DataSourceAutoConfiguration.class
})
class ApplicationTests {

	@MockBean
	private CityRepository cityRepository;

	@Test
	void contextLoads() {
	}

}
