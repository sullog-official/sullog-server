package sullog.backend.alcohol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.alcohol.service.AlcoholService;

@RestController
@RequestMapping("/api/alcohol")
public class AlcoholController {

    private final AlcoholService alcoholService;

    public AlcoholController(AlcoholService alcoholService) {
        this.alcoholService = alcoholService;
    }
}