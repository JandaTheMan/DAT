
package dat.prac3.model.dao;

import java.util.Date;


/**
 * <p>Objeto valor usado en las transferencias con el {@link MessageDAO}.</p>
 */
public class Message {

    public final int fromId, toId;
    public final Date date;
    public final String subject, text;

    /**
     * @param fromId Identificador del usuario remitente.
     * @param toId Identificador del usuario destinatario.
     * @param date Fecha del mensaje.
     * @param subject Asunto del mensaje.
     * @param text Texto del mensaje.
     */
    public Message(int fromId, int toId, Date date, String subject, String text)
    {
	this.fromId = fromId;
	this.toId = toId;
	this.date = date;
	this.subject = subject;
	this.text = text;
    }

    /**
     * Obtiene el identificador del usuario remitente.
     */
    public int getFromId() {
	return fromId;
    }

    /**
     * Obtiene el identificador del usuario destinatario.
     */
    public int getToId() {
	return toId;
    }

    /**
     * Obtiene la fecha del mensaje.
     */
    public Date getDate() {
	return date;
    }

    /**
     * Obtiene el asunto del mensaje.
     */
    public String getSubject() {
	return subject;
    }

    /**
     * Obtiene el texto del mensaje.
     */
    public String getText() {
	return text;
    }

    public String toString() {
	return "Message(" + fromId + "," + toId + "," + date + ",'" + subject + "','" + text + "')";
    }

}

