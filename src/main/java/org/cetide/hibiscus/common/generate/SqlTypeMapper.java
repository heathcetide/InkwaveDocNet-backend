package org.cetide.hibiscus.common.generate;

import java.sql.Types;

public class SqlTypeMapper {
    /**
     * 映射 java.sql.Types 到 Java 类型
     */
    public static String mapSqlType(int type) {
        switch (type) {
            // 字符串类型
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                return SqlTypeConstants.TYPE_STRING;

            // 数值类型
            case Types.INTEGER:
                return SqlTypeConstants.TYPE_INTEGER;
            case Types.TINYINT:
                return SqlTypeConstants.TYPE_BYTE;
            case Types.SMALLINT:
                return SqlTypeConstants.TYPE_SHORT;
            case Types.BIGINT:
                return SqlTypeConstants.TYPE_LONG;
            case Types.FLOAT:
            case Types.REAL:
                return SqlTypeConstants.TYPE_FLOAT;
            case Types.DOUBLE:
                return SqlTypeConstants.TYPE_DOUBLE;
            case Types.NUMERIC:
            case Types.DECIMAL:
                return SqlTypeConstants.TYPE_BIGDECIMAL;

            // 布尔类型
            case Types.BIT:
            case Types.BOOLEAN:
                return SqlTypeConstants.TYPE_BOOLEAN;

            // 日期时间类型
            case Types.DATE:
                return SqlTypeConstants.TYPE_LOCALDATE;
            case Types.TIME:
                return SqlTypeConstants.TYPE_LOCALTIME;
            case Types.TIME_WITH_TIMEZONE:
                return SqlTypeConstants.TYPE_OFFSETTIME;
            case Types.TIMESTAMP:
                return SqlTypeConstants.TYPE_LOCALDATETIME;
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return SqlTypeConstants.TYPE_OFFSETDATETIME;

            // 二进制类型
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return SqlTypeConstants.TYPE_BYTES;

            // LOB 类型
            case Types.BLOB:
                return SqlTypeConstants.TYPE_BLOB;
            case Types.CLOB:
                return SqlTypeConstants.TYPE_CLOB;
            case Types.NCLOB:
                return SqlTypeConstants.TYPE_NCLOB;

            // 结构化类型
            case Types.STRUCT:
                return SqlTypeConstants.TYPE_STRUCT;
            case Types.ARRAY:
                return SqlTypeConstants.TYPE_ARRAY;
            case Types.REF:
                return SqlTypeConstants.TYPE_REF;
            case Types.ROWID:
                return SqlTypeConstants.TYPE_ROWID;
            case Types.SQLXML:
                return SqlTypeConstants.TYPE_SQLXML;

            // 其他类型
            case Types.JAVA_OBJECT:
                return SqlTypeConstants.TYPE_OBJECT;
            case Types.DATALINK:
                return SqlTypeConstants.TYPE_URL;
            case Types.DISTINCT:
            case Types.OTHER:
            case Types.NULL:
            default:
                return SqlTypeConstants.TYPE_OBJECT;
        }
    }
}
