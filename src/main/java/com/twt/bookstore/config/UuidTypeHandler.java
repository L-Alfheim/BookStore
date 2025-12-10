package com.twt.bookstore.config;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * 处理 java.util.UUID 类型与数据库 VARCHAR/CHAR 类型之间的转换。
 * UUID 对象通过 toString() 转换为标准的 36 位字符串进行存储。
 */
@MappedJdbcTypes(JdbcType.CHAR) // 数据库的 JDBC 类型，UUID 存储为VARCHAR(36)
@MappedTypes(UUID.class)       // Java 类型
public class UuidTypeHandler extends BaseTypeHandler<UUID> {

    // 1. 设置参数 (UUID -> String)
    // 当MyBatis执行INSERT/UPDATE时，调用此方法将Java对象的UUID转换为数据库可接受的类型
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        // 将 UUID 转换为标准的 36 位字符串
        ps.setString(i, parameter.toString());
    }

    // 2. 获取结果 (String -> UUID) - 通过列名
    // 当MyBatis执行SELECT时，调用此方法将数据库的CHAR(36)字符串转换为UUID对象
    @Override
    public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String uuidString = rs.getString(columnName);
        return uuidString == null ? null : UUID.fromString(uuidString);
    }

    // 3. 获取结果 (String -> UUID) - 通过列索引
    @Override
    public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String uuidString = rs.getString(columnIndex);
        return uuidString == null ? null : UUID.fromString(uuidString);
    }

    // 4. 获取结果 (String -> UUID) - 存储过程
    @Override
    public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String uuidString = cs.getString(columnIndex);
        return uuidString == null ? null : UUID.fromString(uuidString);
    }
}