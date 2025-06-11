package org.cetide.hibiscus.infrastructure.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.cetide.hibiscus.domain.model.valueobject.Description;

import java.sql.*;

public class DescriptionTypeHandler extends BaseTypeHandler<Description> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Description parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public Description getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return new Description(value);
    }

    @Override
    public Description getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return new Description(value);
    }

    @Override
    public Description getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return new Description(value);
    }
}