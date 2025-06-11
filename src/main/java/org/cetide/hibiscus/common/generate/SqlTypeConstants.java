package org.cetide.hibiscus.common.generate;

/**
 * Java 类型名称常量定义，避免魔法值
 */
public interface SqlTypeConstants {

    String TYPE_STRING = "String";
    String TYPE_INTEGER = "Integer";
    String TYPE_LONG = "Long";
    String TYPE_SHORT = "Short";
    String TYPE_FLOAT = "Float";
    String TYPE_DOUBLE = "Double";
    String TYPE_BYTE = "Byte";
    String TYPE_BOOLEAN = "Boolean";

    String TYPE_BIGDECIMAL = "java.math.BigDecimal";

    String TYPE_LOCALDATE = "java.time.LocalDate";
    String TYPE_LOCALTIME = "java.time.LocalTime";
    String TYPE_LOCALDATETIME = "java.time.LocalDateTime";
    String TYPE_OFFSETTIME = "java.time.OffsetTime";
    String TYPE_OFFSETDATETIME = "java.time.OffsetDateTime";

    String TYPE_BYTES = "byte[]";

    String TYPE_BLOB = "java.sql.Blob";
    String TYPE_CLOB = "java.sql.Clob";
    String TYPE_NCLOB = "java.sql.NClob";

    String TYPE_STRUCT = "java.sql.Struct";
    String TYPE_ARRAY = "java.sql.Array";
    String TYPE_REF = "java.sql.Ref";
    String TYPE_ROWID = "java.sql.RowId";
    String TYPE_SQLXML = "java.sql.SQLXML";
    String TYPE_URL = "java.net.URL";
    String TYPE_OBJECT = "Object";
}