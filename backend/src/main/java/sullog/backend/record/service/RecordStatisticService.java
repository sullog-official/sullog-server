package sullog.backend.record.service;

import org.springframework.stereotype.Service;
import sullog.backend.record.mapper.RecordMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordStatisticService {

    RecordMapper recordMapper;

    public RecordStatisticService(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    public  Map<String, Integer> getRecordStatistics(Integer memberId) {
        Map<String, Integer> alcoholTypeStatisticMap = new HashMap<>();
        recordMapper.selectAlcoholTypeList(memberId)
                .forEach(alcoholType -> {
                    Integer count = alcoholTypeStatisticMap.getOrDefault(alcoholType, 0);
                    alcoholTypeStatisticMap.put(alcoholType, count + 1);
                });
        return alcoholTypeStatisticMap;
    }
}
