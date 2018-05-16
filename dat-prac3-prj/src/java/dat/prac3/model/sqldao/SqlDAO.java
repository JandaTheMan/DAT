
package dat.prac3.model.sqldao;

import java.sql.Connection;
import java.sql.SQLException;


public class SqlDAO
{

    private SqlConnectionFactory connFactory;


    protected SqlDAO(SqlConnectionFactory cfact)
    {
	connFactory = cfact;
    }


    protected Connection getConnection() throws SQLException
    {
	return connFactory.getConnection();
    }

    protected void putConnection(Connection conn) throws SQLException
    {
	connFactory.putConnection(conn);
    }


    protected static String sqlName(String s)
    {
	StringBuffer buf = new StringBuffer();
	buf.append('"');
	for(int i = 0; i < s.length(); i ++) {
	    char c = s.charAt(i);
	    if(c == '"') {
		buf.append('"');
	    }
	    buf.append(c);
	}
	buf.append('"');
	return buf.toString();
    }

    protected static String sqlNameList(String first, String... next)
    {
	StringBuffer buf = new StringBuffer();
	buf.append(sqlName(first));
	for (String s : next) {
	    buf.append(",").append(sqlName(s));
	}
	return buf.toString();
    }

    protected static String sqlString(String s)
    {
	if (s == null) {
	    return "NULL";
	}
	StringBuffer buf = new StringBuffer();
	buf.append('\'');
	for(int i = 0; i < s.length(); i ++) {
	    char c = s.charAt(i);
	    if(c == '\'') {
		buf.append('\'');
	    }
	    buf.append(c);
	}
	buf.append('\'');
	return buf.toString();
    }


}

