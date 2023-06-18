package sullog.backend.alcohol.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sullog.backend.alcohol.dto.request.AlcoholSearchRequestDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.service.AlcoholService;
import sullog.backend.auth.service.TokenService;

@RestController
@RequestMapping("/alcohols")
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
    public ResponseEntity<AlcoholInfoWithPagingDto> getAlcoholIdListWithKeywordAndCursor(@RequestAttribute Integer memberId,
                                                                                         AlcoholSearchRequestDto alcoholSearchRequestDto) {
        return new ResponseEntity<>(alcoholService.getAlcoholInfo(alcoholSearchRequestDto), HttpStatus.OK);
    }

}