package sullog.backend.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK, code = HttpStatus.OK)
    public void healthCheck() {
    }
}
