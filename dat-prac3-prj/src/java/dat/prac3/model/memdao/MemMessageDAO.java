
package dat.prac3.model.memdao;

import dat.prac3.model.dao.MessageDAO;
import dat.prac3.model.dao.Entity;
import dat.prac3.model.dao.Message;

import static dat.base.Types.*;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;


public class MemMessageDAO implements MessageDAO
{

    private HashMap<Integer,Message> map;
    private int lastKey;

    public MemMessageDAO()
    {
	map = new HashMap<Integer,Message>();
	lastKey = 0;
    }

    @Override
    public Maybe<Message> get(int id)
    {
	Message v = map.get(id);
	if (v == null) {
	    return Nothing();
	} else {
	    return Just(v);
	}
    }

    @Override
    public List<Entity<Message>> selectByTo(int toId)
    {
	ArrayList<Entity<Message>> list = new ArrayList<Entity<Message>>();
	for (Entry<Integer,Message> kv : map.entrySet()) {
            if (kv.getValue().toId == toId) {
                list.add(new Entity<Message>(kv.getKey(), kv.getValue()));
            }
	}
	return list;
    }

    @Override
    public int insert(Message m)
    {
	int id = ++lastKey;
	map.put(id, m);
	return id;
    }

    @Override
    public void delete(int id)
    {
	map.remove(id);
    }

}
