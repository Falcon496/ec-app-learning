package taka.example.spring_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.r2dbc.url=r2dbc:h2:mem:///spring_project_test;DB_CLOSE_DELAY=-1",
		"spring.r2dbc.username=sa",
		"spring.r2dbc.password=",
		"spring.flyway.enabled=false"
})
class SpringProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
