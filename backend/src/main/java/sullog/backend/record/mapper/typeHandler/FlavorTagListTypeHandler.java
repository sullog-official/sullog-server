package sullog.backend.record.mapper.typeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import sullog.backend.record.entity.FlavorDetail;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FlavorTagListTypeHandler extends BaseTypeHandler<List<FlavorDetail>> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<FlavorDetail> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FlavorDetail> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return jsonToFlavorTagMap(rs.getString(columnName));
    }

    @Override
    public List<FlavorDetail> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return jsonToFlavorTagMap(rs.getString(columnIndex));
    }

    @Override
    public List<FlavorDetail> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return jsonToFlavorTagMap(cs.getString(columnIndex));
    }

    private List<FlavorDetail> jsonToFlavorTagMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<FlavorDetail>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
