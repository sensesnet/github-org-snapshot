package org.snapshot.github.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static org.snapshot.github.model.rest.RestApiConstants.GITHUB_BASE_PATH;
import static org.snapshot.github.model.rest.RestApiConstants.GITHUB_HEALTH_SUBPATH;

@RestController
@RequestMapping(path = GITHUB_BASE_PATH)
public class HealthCheckController {

    @Operation(summary = "Health check endpoint")
    @GetMapping(GITHUB_HEALTH_SUBPATH)
    public Map<String, String> health() {
        return Collections.singletonMap("status", "OK");
    }
}
