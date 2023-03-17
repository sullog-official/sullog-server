package sullog.backend.alcohol.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.alcohol.dto.request.AlcoholSearchRequestDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.entity.Alcohol;
import sullog.backend.alcohol.service.AlcoholService;
import sullog.backend.member.entity.Token;
import sullog.backend.member.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/alcohols")
public class AlcoholController {

    private final AlcoholService alcoholService;

    private final TokenService tokenService;

    public AlcoholController(AlcoholService alcoholService, TokenService tokenService) {
        this.alcoholService = alcoholService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<AlcoholInfoDto> getAlcohol(
            @RequestParam int alcoholId) {
        return new ResponseEntity<>(alcoholService.getAlcoholById(alcoholId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<AlcoholInfoWithPagingDto> getAlcoholIdListWithKeywordAndCursor(
            HttpServletRequest request,
            AlcoholSearchRequestDto alcoholSearchRequestDto) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        int memberId = tokenService.getMemberId(token);

        return new ResponseEntity<>(alcoholService.getAlcoholInfo(memberId, alcoholSearchRequestDto), HttpStatus.OK);
    }

}