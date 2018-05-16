
package dat.prac3.model.memdao;

import dat.prac3.model.dao.DAOFactory;
import dat.prac3.model.dao.UserDAO;
import dat.prac3.model.dao.MessageDAO;
import dat.prac3.model.dao.Entity;
import dat.prac3.model.dao.User;
import dat.prac3.model.dao.Message;

/**
 * Realización de una factoría {@link dat.prac3.model.dao.DAOFactory} usando memoria local.
 * Usado en fase de pruebas.
 *
 * @see dat.prac3.model.dao.DAOFactory
 */
public class MemDAOFactory implements DAOFactory
{

    private MemUserDAO userDAO;
    private MemMessageDAO messageDAO;

    public MemDAOFactory()
    {
	userDAO = new MemUserDAO();
	messageDAO = new MemMessageDAO();
    }

    @Override
    public UserDAO getUserDAO()
    {
	return userDAO;
    }

    @Override
    public MessageDAO getMessageDAO()
    {
	return messageDAO;
    }


}

