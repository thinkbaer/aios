package de.thinkbaer.aios.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public enum SqlDataType {

	BIT(Types.BIT, new Wrapper<Boolean>() {
		@Override
		public Boolean getValueById(ResultSet rs, int i) throws SQLException {
			return Boolean.valueOf(rs.getBoolean(i));
		}
	}),

	TINYINT(Types.TINYINT, null), 
	SMALLINT(Types.SMALLINT, null), INTEGER(Types.INTEGER, null), BIGINT(Types.BIGINT,
			null), FLOAT(Types.FLOAT, null), REAL(Types.REAL, null), DOUBLE(Types.DOUBLE, null), NUMERIC(Types.NUMERIC,
					null),

	DECIMAL(Types.DECIMAL, null), CHAR(Types.CHAR, null), VARCHAR(Types.VARCHAR, null), LONGVARCHAR(Types.LONGVARCHAR,
			null), DATE(Types.DATE, null), TIME(Types.TIME, null), TIMESTAMP(Types.TIMESTAMP, null), BINARY(
					Types.BINARY, null), VARBINARY(Types.VARBINARY, null), LONGVARBINARY(Types.LONGVARBINARY,
							null), NULL(Types.NULL, null), OTHER(Types.OTHER, null), JAVA_OBJECT(Types.JAVA_OBJECT,
									null), DISTINCT(Types.DISTINCT, null), STRUCT(Types.STRUCT, null), ARRAY(
											Types.ARRAY,
											null), BLOB(Types.BLOB, null), CLOB(Types.CLOB, null), REF(Types.REF,
													null), DATALINK(Types.DATALINK, null), BOOLEAN(Types.BOOLEAN,
															null), ROWID(Types.ROWID, null), NCHAR(Types.NCHAR,
																	null), NVARCHAR(Types.NVARCHAR, null), LONGNVARCHAR(
																			Types.LONGNVARCHAR,
																			null), NCLOB(Types.NCLOB, null), SQLXML(
																					Types.SQLXML,
																					null), REF_CURSOR(Types.REF_CURSOR,
																							null), TIME_WITH_TIMEZONE(
																									Types.TIME_WITH_TIMEZONE,
																									null), TIMESTAMP_WITH_TIMEZONE(
																											Types.TIMESTAMP_WITH_TIMEZONE,
																											null);

	private final int code;

	private final Wrapper wrapper;

	SqlDataType(int s, Wrapper<?> wrapper) {
		this.code = s;
		this.wrapper = wrapper;
	}

	public int getCode() {
		return code;
	}

	public static SqlDataType forCode(int code) {
		SqlDataType[] dts = values();
		for (int i = 0; i < dts.length; i++) {
			SqlDataType dt = dts[i];
			if (dt.getCode() == code) {
				return dt;
			}
		}
		return null;
	}

	interface Wrapper<X> {

		X getValueById(ResultSet rs, int i) throws SQLException;

	}

}
