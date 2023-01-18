package dev.educosta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

@Slf4j
@UtilityClass
public class JsonUtils {

  private final ObjectMapper objectMapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  /**
   * Method responsible to convert an object into String. This method is useful to create a json
   * from object to use as stubs when is necessary use wiremock.
   */
  public String toJson(Object object) {
    Assert.notNull(object, "parameter must not be null");
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("error trying to convert object into json", e);
      throw new IllegalArgumentException(e);
    }
  }

  /** Method responsible to convert json into Object. All parameters are required */
  public <T> T toObject(String json, Class<T> clazz) {
    Assert.hasText(json, "parameter must not be null or empty");
    Assert.notNull(clazz, "parameter clazz must not be null");
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("error trying to convert json into Object " + clazz.getName(), e);
      throw new IllegalArgumentException(e);
    }
  }

  public <T> List<T> toListOfObject(String json, Class<T> clazz) {
    Assert.hasText(json, "parameter must not be null or empty");
    Assert.notNull(clazz, "parameter clazz must not be null");
    try {
      return objectMapper.readValue(
          json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
