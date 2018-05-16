
package dat.prac3.model.dao;

import static dat.base.Types.*;

import java.util.List;
import java.util.Date;


/**
 * <p>DAO de mensajes.</p>
 */
public interface MessageDAO
{

    /**
     * Obtiene mensaje por su identificador.
     * @param  id Identificador del mensaje.
     * @return El mensaje con el identificador indicado,
     *         ó <code>null</code> si no existe mensaje con el identificador indicado.
     */
    public Maybe<Message> get(int id) throws Exception;


    /**
     * Obtiene mensajes por identificador del usuario destinatario.
     * @param  toId Identificador del usuario destinatario.
     * @return Lista con todos los mensajes con identificador de destinatario igual a <code>toId</code>
     *         (puede tener tamaño <code>0</code>).
     */
    public List<Entity<Message>> selectByTo(int toId) throws Exception;


    /**
     * Inserta un nuevo mensaje en la tabla de mensajes.
     * @param  msg Mensaje a insertar.
     * @return Identificador del mensaje insertado.
     */
    public int insert(Message msg) throws Exception;


    /**
     * Elimina el mensaje con el identificador indicado.
     * Si no existe mensaje con el identificador indicado, no altera la tabla de mensajes.
     * @param id Identificador del mensaje.
     */
    public void delete(int id) throws Exception;

}

