package io.abhithube.enrollmentservice;

import io.abhithube.enrollmentservice.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class EnrollmentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
