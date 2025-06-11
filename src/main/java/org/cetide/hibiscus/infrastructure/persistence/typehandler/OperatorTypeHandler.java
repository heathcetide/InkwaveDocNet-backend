package org.cetide.hibiscus.infrastructure.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.cetide.hibiscus.domain.model.valueobject.Description;
import org.cetide.hibiscus.domain.model.valueobject.Operator;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OperatorTypeHandler extends BaseTypeHandler<Operator> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Operator parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public Operator getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return new Operator(value);
    }

    @Override
    public Operator getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return new Operator(value);
    }

    @Override
    public Operator getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return new Operator(value);
    }
}