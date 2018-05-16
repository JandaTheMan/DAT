
package dat.prac3.model.dao;


/**
 * <p>Factoría abstracta de DAOs para la aplicación de mensajes (práctica 3).</p>
 *
 * @see dat.prac3.model.sqldao.SqlDAOFactory
 */
public interface DAOFactory
{

    public abstract UserDAO getUserDAO();


    public abstract MessageDAO getMessageDAO();


}

