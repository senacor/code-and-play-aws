package com.senacor.tecco.ilms.common.context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContextTestApplication.class, webEnvironment = RANDOM_PORT)
public class ContextTestApplicationTest {

	@Test
	public void contextLoads() {
	}

}
