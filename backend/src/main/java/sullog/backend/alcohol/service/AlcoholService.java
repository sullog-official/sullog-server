package sullog.backend.alcohol.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import sullog.backend.alcohol.mapper.AlcoholMapper;
import sullog.backend.member.entity.Member;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Service
public class AlcoholService {

    private final AlcoholMapper alcoholMapper;

    public AlcoholService(AlcoholMapper alcoholMapper) {
        this.alcoholMapper = alcoholMapper;
    }

}
