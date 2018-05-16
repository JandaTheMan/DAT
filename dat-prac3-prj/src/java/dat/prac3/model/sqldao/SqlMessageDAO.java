
package dat.prac3.model.sqldao;

import dat.prac3.model.dao.MessageDAO;
import dat.prac3.model.dao.Entity;
import dat.prac3.model.dao.Message;

import static dat.base.Types.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.sql.*;


public class SqlMessageDAO extends SqlDAO implements MessageDAO
{

    public SqlMessageDAO(SqlConnectionFactory cfact)
    {
	super(cfact);
    }

    protected static Message makeMessageVO(ResultSet cursor) throws SQLException
    {
	return new Message(
		cursor.getInt(DBNames.MESSAGE_FROM),
		cursor.getInt(DBNames.MESSAGE_TO),
		new Date(cursor.getLong(DBNames.MESSAGE_DATE) * 1000),
		cursor.getString(DBNames.MESSAGE_SUBJECT),
		cursor.getString(DBNames.MESSAGE_TEXT)
	);
    }

    @Override
    public Maybe<Message> get(int id) throws SQLException
    {
	Maybe<Message> vo = Nothing();
	Connection dbc = getConnection();
	Statement order = dbc.createStatement();
	ResultSet cursor = order.executeQuery(
		"SELECT * FROM " + sqlName(DBNames.MESSAGES_TABLE) +
		" WHERE " + sqlName(DBNames.MESSAGE_ID) + "=" + id
	);
	if (cursor.next()) {
	    vo = Just(makeMessageVO(cursor));
	}
	order.close();
	putConnection(dbc);
	return vo;
    }

    @Override
    public List<Entity<Message>> selectByTo(int toId) throws SQLException
    {
	ArrayList<Entity<Message>> list = new ArrayList<Entity<Message>>();
	Connection dbc = getConnection();
	Statement order = dbc.createStatement();
	ResultSet cursor = order.executeQuery(
		"SELECT * FROM " + sqlName(DBNames.MESSAGES_TABLE) + 
		" WHERE " + sqlName(DBNames.MESSAGE_TO) + "=" + Integer.toString(toId)
	);
	while (cursor.next()) {
            int i = cursor.getInt(DBNames.MESSAGE_ID);
	    Message vo = makeMessageVO(cursor);
            list.add(new Entity<Message>(i, vo));
	}
	order.close();
	putConnection(dbc);
	return list;
    }

    @Override
    public int insert(Message m) throws SQLException
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
		"INSERT INTO " + sqlName(DBNames.MESSAGES_TABLE) + " (" +
		sqlNameList(DBNames.MESSAGE_ID, DBNames.MESSAGE_FROM, DBNames.MESSAGE_TO,
			DBNames.MESSAGE_SUBJECT, DBNames.MESSAGE_TEXT, DBNames.MESSAGE_DATE) +
		") VALUES (" +
		id + "," + Integer.toString(m.getFromId()) + "," + Integer.toString(m.getToId()) + "," +
		sqlString(m.getSubject()) + "," + sqlString(m.getText()) + "," + Long.toString(m.getDate().getTime() / 1000) + ")"
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
		"DELETE FROM " + sqlName(DBNames.MESSAGES_TABLE) + " WHERE " +
		sqlName(DBNames.MESSAGE_ID) + "=" + id
	);
	order.close();
	putConnection(dbc);
    }

}
