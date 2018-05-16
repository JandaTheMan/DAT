
package dat.prac3.model.sqldao;

import javax.sql.DataSource;
import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Stack;


/**
 * Factoría de conexiones JDBC.
 */
public abstract class SqlConnectionFactory
{

    protected Stack<Connection> stack;

    protected int capacity;


    public static SqlConnectionFactory newInstance(final DataSource dataSource)
    {
	return new SqlConnectionFactory() {
	    protected Connection makeConnection() throws SQLException
	    {
		return dataSource.getConnection();
	    }
	};
    }


    public static SqlConnectionFactory newInstance(
	final String db_url, final String db_user, final String db_password
    ) throws ClassNotFoundException
    {
	try {
	    Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException e) {
	    // Try old driver
	    Class.forName("postgresql.Driver");
	}
	return new SqlConnectionFactory() {
	    protected Connection makeConnection() throws SQLException
	    {
		return DriverManager.getConnection(db_url, db_user, db_password);
	    }
	};
    }


    protected SqlConnectionFactory()
    {
	stack = new Stack<Connection>();
	capacity = 10;
    }


    public synchronized Connection getConnection() throws SQLException
    {
	if (stack.empty()) {
	    return makeConnection();
	} else {
	    return stack.pop();
	}
    }

    protected abstract Connection makeConnection() throws SQLException;


    public synchronized void putConnection(Connection conn) throws SQLException
    {
	if (stack.size() == capacity) {
	    conn.close();
	} else {
	    stack.push(conn);
	}
    }


}


