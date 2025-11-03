package org.snapshot.github.api.rest;

import org.junit.jupiter.api.Test;
import org.snapshot.github.model.rest.RestApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthCheckController.class)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnOkOnHealthCheck() throws Exception {
        mockMvc.perform(get(RestApiConstants.GITHUB_BASE_PATH + RestApiConstants.GITHUB_HEALTH_SUBPATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));
    }
}