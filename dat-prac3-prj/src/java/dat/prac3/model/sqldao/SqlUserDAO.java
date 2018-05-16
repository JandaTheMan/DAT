
package dat.prac3.model.sqldao;

import dat.prac3.model.dao.*;

import static dat.base.Types.*;

import java.sql.*;
import java.util.ArrayList;


public class SqlUserDAO extends SqlDAO implements UserDAO
{

    public SqlUserDAO(SqlConnectionFactory cfact)
    {
	super(cfact);
    }

    protected static User makeUserVO(ResultSet cursor) throws SQLException
    {
	return new User(
		cursor.getString(DBNames.USER_NAME),
		cursor.getString(DBNames.USER_PASSWORD)
	);
    }

	public ArrayList<Entity<User>> getUsers() throws Exception{
		ArrayList<Entity<User>> list = new ArrayList<Entity<User>>();
		return list;
	}

    @Override
    public Maybe<User> get(int id) throws SQLException
    {
	Maybe<User> vo = Nothing();
	Connection dbc = getConnection();
	Statement order = dbc.createStatement();
	ResultSet cursor = order.executeQuery(
		"SELECT * FROM " + sqlName(DBNames.USERS_TABLE) +
		" WHERE " + sqlName(DBNames.USER_ID) + "=" + id
	);
	if (cursor.next()) {
	    vo = Just(makeUserVO(cursor));
	}
	order.close();
	putConnection(dbc);
	return vo;
    }

    @Override
    public Maybe<Entity<User>> selectByName(String name) throws SQLException
    {
	Maybe<Entity<User>> e = Nothing();
	Connection dbc = getConnection();
	Statement order = dbc.createStatement();
	ResultSet cursor = order.executeQuery(
		"SELECT * FROM " + sqlName(DBNames.USERS_TABLE) +
		" WHERE " + sqlName(DBNames.USER_NAME) + "=" + sqlString(name)
	);
	if (cursor.next()) {
            int i = cursor.getInt(DBNames.USER_ID);
	    User vo = makeUserVO(cursor);
            e = Just(new Entity<User>(i, vo));
	}
	order.close();
	putConnection(dbc);
	return e;
    }

    @Override
    public int insert(User m) throws SQLException
    {
	Connection dbc = getConnection();
	Statement order = dbc.createStatement();
	// Get primary key
	ResultSet cursor = order.executeQuery(
		"SELECT nextval(" + sqlString(DBNames.ID_SEQUENCE) + ")"
	);
	cursor.next();
	int id = cursor.getInt(1);
	// Construct command
	order.executeUpdate(
		"INSERT INTO " + sqlName(DBNames.USERS_TABLE) + " (" +
		sqlNameList(DBNames.USER_ID, DBNames.USER_NAME, DBNames.USER_PASSWORD) +
		") VALUES (" +
		id + "," + sqlString(m.getName()) + "," + sqlString(m.getPassword()) + ")"
	);
	order.close();
	putConnection(dbc);
	return id;
    }

    @Override
    public void delete(int id) throws SQLException
    {
	Connection dbc = getConnection();
	Statement order = dbc.createStatement();
	order.executeUpdate(
		"DELETE FROM " + sqlName(DBNames.USERS_TABLE) + " WHERE " +
		sqlName(DBNames.USER_ID) + "=" + id
	);
	order.close();
	putConnection(dbc);
    }

}

