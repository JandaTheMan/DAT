
package dat.prac3.model.dao;


/**
 * <p>Objeto valor usado en las transferencias con el {@link UserDAO}.</p>
 */
public class User {

    public final String name, password;

    /**
     * @param name Nombre del usuario.
     * @param password Password del usuario.
     */
    public User(String name, String password)
    {
	this.name = name;
	this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     */
    public String getName() {
	return name;
    }

    /**
     * Obtiene el password del usuario.
     */
    public String getPassword() {
	return password;
    }

    public String toString() {
	return "User('" + name + "','" + password + "')";
    }

}

