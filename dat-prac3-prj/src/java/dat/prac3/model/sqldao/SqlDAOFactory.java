
package dat.prac3.model.sqldao;

import dat.prac3.model.dao.DAOFactory;
import dat.prac3.model.dao.UserDAO;
import dat.prac3.model.dao.MessageDAO;

import javax.sql.DataSource;
import java.sql.*;

import javax.naming.InitialContext;
import javax.naming.Context;


/**
 * Realización de la factoría {@link dat.prac3.model.dao.DAOFactory} usando JDBC.
 *
 * @see dat.prac3.model.dao.DAOFactory
 */
public class SqlDAOFactory implements DAOFactory
{

    protected SqlConnectionFactory connFactory;


    /**
     * <p>Construye una realización JDBC de la factoría de DAOs.</p>
     * @param jndiName	Nombre del recurso JDNI relativo al contexto {@code "java:comp/env"}.
     *//* Para el usuario {@code USER} el valor de {@code jndiName} debe ser {@code "jdbc/USER"}.
     */
    public SqlDAOFactory(String jndiName) throws Exception
    {
	Context initCtx = new InitialContext();
	Context envCtx = (Context) initCtx.lookup("java:comp/env");
	DataSource dataSource = (DataSource) envCtx.lookup(jndiName);
	connFactory = SqlConnectionFactory.newInstance(dataSource);
    }

    /**
     * <p>Construye una realización JDBC de la factoría de DAOs.</p>
     * @param dataSource Factoría de conexiones JDBC.
     */
    public SqlDAOFactory(DataSource dataSource)
    {
	connFactory = SqlConnectionFactory.newInstance(dataSource);
    }

    /**
     * <p>Construye una realización JDBC de la factoría de DAOs.</p>
     * @param db_url	URL (con esquema de JDBC) de la base de datos.
     * @param db_user	Usuario de la base de datos.
     * @param db_password Password para la autenticación del usuario de la base de datos.
     *
     */
    public SqlDAOFactory(String db_url, String db_user, String db_password) throws Exception
    {
	connFactory = SqlConnectionFactory.newInstance(db_url, db_user, db_password);
    }


    @Override
    public UserDAO getUserDAO()
    {
	return new SqlUserDAO(connFactory);
    }

    @Override
    public MessageDAO getMessageDAO()
    {
	return new SqlMessageDAO(connFactory);
    }


}

