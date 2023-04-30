package sullog.backend.record.service;

import org.springframework.stereotype.Service;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;
import sullog.backend.record.dto.response.RecordStatistics;
import sullog.backend.record.mapper.RecordMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecordStatisticService {

    private RecordMapper recordMapper;

    private MemberMapper memberMapper;

    public RecordStatisticService(RecordMapper recordMapper, MemberMapper memberMapper) {
        this.recordMapper = recordMapper;
        this.memberMapper = memberMapper;
    }

    public RecordStatistics getRecordStatistics(Integer memberId) {
        Member member = memberMapper.selectMemberById(memberId);

        Map<String, Integer> alcoholTypeStatisticMap = new HashMap<>();
        recordMapper.selectAlcoholTypeList(memberId)
                .forEach(alcoholType -> {
                    Integer count = alcoholTypeStatisticMap.getOrDefault(alcoholType, 0);
                    alcoholTypeStatisticMap.put(alcoholType, count + 1);
                });

        return RecordStatistics.builder()
                .memberId(memberId)
                .email(member.getEmail())
                .nickname(member.getNickName())
                .recordStatisticsMap(alcoholTypeStatisticMap)
                .build();
    }
}
