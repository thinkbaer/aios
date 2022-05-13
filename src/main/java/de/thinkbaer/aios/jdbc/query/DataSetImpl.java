package de.thinkbaer.aios.jdbc.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.SimpleDateFormat;

import de.thinkbaer.aios.api.exception.Todo;

public class DataSetImpl extends LinkedHashMap<String, Object> {

  /**
   *
   */
  private static final long serialVersionUID = 7896880151014783925L;

  private static final Logger L = LogManager.getLogger(DataSetImpl.class);

  public static SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

  @JsonIgnore
  private HashMap<Integer, String> colMap = new HashMap<>();

  public DataSetImpl() {

  }

  public DataSetImpl(ResultSet set) throws Exception {
    init(set);
  }

  // http://db.apache.org/ojb/docu/guides/jdbc-types.html
  public void init(ResultSet resultSet) throws Exception {
    try {
      int columnsize = resultSet.getMetaData().getColumnCount();
      // L.info("=== Column " + columnsize);
      for (int i = 1; i <= columnsize; i++) {
        String columnName = resultSet.getMetaData().getColumnName(i);
        if (colMap.containsValue(columnName)) {
          String tableName = resultSet.getMetaData().getTableName(i);
          String newColumnName = tableName + "_" + columnName;
          L.warn("column value " + columnName + " already exists, renaming to " + newColumnName
            + " (this can happen on join queries without aliasing)");
          columnName = newColumnName;
        }
        colMap.put(i, columnName);


        int columnSqlType = resultSet.getMetaData().getColumnType(i);
        // L.info("=== Column " + columnName + " => " + columnSqlType);
        switch (columnSqlType) {
          case Types.ARRAY:
            throw new Todo(columnName + ": ARRAY");
            // break;
          case Types.BIT:
            put(columnName, resultSet.getBoolean(i));
            break;
          case Types.TINYINT:
            put(columnName, resultSet.getByte(i));
            break;
          case Types.SMALLINT:
            put(columnName, resultSet.getShort(i));
            break;
          case Types.INTEGER:
            put(columnName, resultSet.getInt(i));
            break;
          case Types.BIGINT:
            put(columnName, resultSet.getLong(i));
            break;
          case Types.FLOAT:
            put(columnName, resultSet.getDouble(i));
            break;
          case Types.REAL:
            put(columnName, resultSet.getFloat(i));
            break;
          case Types.DOUBLE:
            put(columnName, resultSet.getDouble(i));
            break;
          case Types.NUMERIC:
            put(columnName, resultSet.getInt(i));
            break;
          case Types.DECIMAL:
            put(columnName, resultSet.getBigDecimal(i));
            break;
          case Types.CHAR:
            put(columnName, resultSet.getString(i));
            break;
          case Types.VARCHAR:
            put(columnName, resultSet.getString(i));
            break;
          case Types.LONGVARCHAR:
            put(columnName, resultSet.getString(i));
            break;
          case Types.DATE:
            java.sql.Date ts2 = resultSet.getDate(i);
            if (ts2 != null) {
              // Date d = new Date(ts2.getTime());
              // Calendar cal = Calendar.getInstance();
              // cal.setTime(ts2);
              // cal.set(Calendar.MILLISECOND, 0);
              put(columnName, buildDate(ts2.getTime()));
            } else {
              put(columnName, null);
            }
            break;
          case Types.TIME:
            Time ts3 = resultSet.getTime(i);
            if (ts3 != null) {

              Date d = buildDate(ts3.getTime());
              String _time = time.format(d);
              /*
               * // Calendar start3 = Calendar.getInstance(); // start3.setTime(ts3); //
               * put(columnName, start3.getTime()); put(columnName, d);
               */
              put(columnName, _time);

            } else {
              put(columnName, null);
            }

            break;
          case Types.TIMESTAMP:
            Timestamp ts = resultSet.getTimestamp(i);
            if (ts != null) {
              // Date d = new Date(ts.getTime());
              // Calendar start = Calendar.getInstance();
              // start.setTime(ts);
              put(columnName, buildDate(ts.getTime()));
            } else {
              put(columnName, null);
            }
            break;
          // TODO: New Types in JDBC 4.2
          // - TIMESTAMP_WITH_TIMEZONE
          // - TIME_WITH_TIMEZONE
          // - REF_CURSOR
          /*
           * case Types.TIMESTAMP_WITH_TIMEZONE: Timestamp ts = resultSet.getTimestamp(i);
           * if (ts != null) { // Date d = new Date(ts.getTime()); // Calendar start =
           * Calendar.getInstance(); // start.setTime(ts); put(columnName,
           * buildDate(ts.getTime())); } else { put(columnName, null); } break;
           */
          case Types.BINARY:
            // throw new Todo(columnName +":BINARY");
            // break;
          case Types.VARBINARY:
            // throw new Todo(columnName +":VARBINARY");
            // break;
          case Types.LONGVARBINARY:
            byte[] bytes = resultSet.getBytes(i);

            // FIXME cache this info!!!
            CharsetDetector detector = new CharsetDetector();
            detector.setText(bytes);
            CharsetMatch match = detector.detect();
            if (match == null) {
              put(columnName, bytes);
            } else {
              put(columnName, match.getString());
            }
            break;
          case Types.NULL:
            put(columnName, null);
            break;
          case Types.OTHER:
            String data = resultSet.getString(i);
            put(columnName, data);
            L.warn("Column " + columnName + " of type OTHER in dataset");
            // throw new Todo(columnName +":OTHER");
            break;
          case Types.JAVA_OBJECT:
            throw new Todo(columnName + ":JAVA_OBJECT");
            // break;
          case Types.DISTINCT:
            throw new Todo(columnName + ":DISTINCT");
            // break;
          case Types.STRUCT:
            throw new Todo(columnName + ":STRUCT");
            // break;
          case Types.BLOB:
            java.sql.Blob ablob = resultSet.getBlob(i);
            String str = new String(ablob.getBytes(1l, (int) ablob.length()));
            put(columnName, str);
            break;
          case Types.CLOB:
            String clob = clobStringConversion(resultSet.getClob(i));
            put(columnName, clob);
            break;
          case Types.REF:
            throw new Todo(columnName + ":REF");
            // break;
          case Types.DATALINK:
            throw new Todo(columnName + ":DATALINK");
            // break;
          case Types.BOOLEAN:
            put(columnName, resultSet.getBoolean(i));
            break;
          case Types.ROWID:
            throw new Todo(columnName + ":ROWID");
            // break;
          case Types.NCHAR:
            throw new Todo(columnName + ":NCHAR");
            // break;
          case Types.NVARCHAR:
            throw new Todo(columnName + ":NCHARVAR");
            // break;
          case Types.LONGNVARCHAR:
            throw new Todo(columnName + ":LONGNVARCHAR");
            // break;
          case Types.NCLOB:
            throw new Todo(columnName + ":NCLOB");
            // break;
          case Types.SQLXML:
            throw new Todo(columnName + ":SQLXML");
            // break;
          default:
            throw new Todo(columnName + " => " + columnSqlType);
        }
        if (resultSet.wasNull()) {
          put(columnName, null);
        }
      }
    } catch (Exception e) {
      L.error(e);
      throw e;
    }
  }

  public static Date buildDate(long date) {
    Date d = new Date(date);
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  @Override
  public Object get(Object key) {
    if (key instanceof Integer) {
      key = colMap.get((int) key);
    }
    return super.get(key);
  }

  public void clear() {
    colMap.clear();
    this.clear();
  }

  public static String clobStringConversion(Clob clb) throws IOException, SQLException {
    if (clb == null)
      return "";

    StringBuffer str = new StringBuffer();
    String strng;

    BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());

    while ((strng = bufferRead.readLine()) != null)
      str.append(strng);

    return str.toString();
  }

}
