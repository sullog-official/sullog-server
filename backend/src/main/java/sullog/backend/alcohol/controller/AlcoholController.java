package sullog.backend.alcohol.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.entity.Alcohol;
import sullog.backend.alcohol.service.AlcoholService;

import java.util.List;

@RestController
@RequestMapping("/api/alcohols")
public class AlcoholController {

    private final AlcoholService alcoholService;

    public AlcoholController(AlcoholService alcoholService) {
        this.alcoholService = alcoholService;
    }

    @GetMapping
    public ResponseEntity<AlcoholInfoDto> getAlcohol(
            @RequestParam int alcoholId) {
        return new ResponseEntity<>(alcoholService.getAlcoholById(alcoholId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<AlcoholInfoWithPagingDto> getAlcoholIdListWithKeywordAndCursor(
            @RequestParam(required = false) String keyword,
            @RequestParam int cursor,
            @RequestParam int limit) {
        return new ResponseEntity<>(alcoholService.getAlcoholInfo(keyword, cursor, limit), HttpStatus.OK);
    }

}