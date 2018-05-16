
package dat.prac3.model.dao;

import static dat.base.Types.*;

import java.util.ArrayList;

/**
 * <p>DAO de usuarios.</p>
 */
public interface UserDAO
{

    /**
     * Obtiene usuario por su identificador.
     * @param  id Identificador del usuario.
     * @return El usuario con el identificador indicado,
     *         ó <code>null</code> si no existe usuario con el identificador indicado.
     */
    public Maybe<User> get(int id) throws Exception;
    
    /**
     * Obtiene todos los usuarios de la base de datos
     * @return Lista con todos los usuarios
     */
    
    public ArrayList<Entity<User>> getUsers() throws Exception;


    /**
     * Obtiene usuario por nombre.
     * @param  name Nombre del usuario.
     * @return El usuario con el nombre indicado,
     *         ó <code>null</code> si no existe usuario con el nombre indicado.
     */
    public Maybe<Entity<User>> selectByName(String name) throws Exception;

    /**
     * Inserta un nuevo usuario en la tabla de usuarios.
     * @param  msg Usuario a insertar.
     * @return Identificador del usuario insertado.
     */
    public int insert(User msg) throws Exception;


    /**
     * Elimina el usuario con el identificador indicado.
     * Si no existe usuario con el identificador indicado, no altera la tabla de usuarios.
     * @param id Identificador del usuario.
     */
    public void delete(int id) throws Exception;

}

