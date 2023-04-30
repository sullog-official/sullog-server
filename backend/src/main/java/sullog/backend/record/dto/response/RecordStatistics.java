package sullog.backend.record.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public class RecordStatistics {

    private Integer memberId;

    private String email;

    private String nickname;

    private Map<String, Integer> recordStatisticsMap;

    public Integer getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Map<String, Integer> getRecordStatisticsMap() {
        return recordStatisticsMap;
    }
}
