package dev.educosta;

import org.springframework.boot.SpringBootConfiguration;

/**
 * Sprint boot test need to find a class annotated with @SpringBootConfiguration which is
 * into @SpringBootApplication from the bootstrap Application class. However the framework
 * module does not depend on bootstrap module, so we fake the app here with the configuration
 * annotation.
 */
@SpringBootConfiguration
public class FakeApp {
}
