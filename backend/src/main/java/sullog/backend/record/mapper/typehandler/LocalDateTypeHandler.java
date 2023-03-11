package sullog.backend.record.mapper.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedTypes(LocalDate.class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {


    @Override
    public void setParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDate getResult(ResultSet rs, String columnName) throws SQLException {
        LocalDateTime localDateTime = rs.getTimestamp(columnName).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    @Override
    public LocalDate getResult(ResultSet rs, int columnIndex) throws SQLException {
        LocalDateTime localDateTime = rs.getTimestamp(columnIndex).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    @Override
    public LocalDate getResult(CallableStatement cs, int columnIndex) throws SQLException {
        LocalDateTime localDateTime = cs.getTimestamp(columnIndex).toLocalDateTime();
        return localDateTime.toLocalDate();
    }
}
