
package dat.prac3.model.memdao;

import dat.prac3.model.dao.UserDAO;
import dat.prac3.model.dao.Entity;
import dat.prac3.model.dao.User;

import static dat.base.Types.*;
import static dat.base.Lists.*;

import java.util.HashMap;
import java.util.Map.Entry;

import java.util.ArrayList;


public class MemUserDAO implements UserDAO
{

    private HashMap<Integer,User> map;
    private int lastKey;

    public MemUserDAO()
    {
	map = new HashMap<Integer,User>();
	lastKey = 0;
	/*Done in the initialization 
	insert(new User("usuari1", "clau1"));
	insert(new User("usuari2", "clau2"));
	insert(new User("usuari3", "clau3"));
	*/
    }

    @Override
    public Maybe<User> get(int id)
    {
	User v = map.get(id);
	if (v == null) {
	    return Nothing();
	} else {
	    return Just(v);
	}
    }
    
    @Override
    public ArrayList<Entity<User>> getUsers(){
    	ArrayList<Entity<User>> users = new ArrayList<Entity<User>>();
    	for (Entry<Integer,User> kv : map.entrySet()) {
    		users.add(new Entity<User>(kv.getKey(), kv.getValue()));
            }
    	return users;
    }

    @Override
    public Maybe<Entity<User>> selectByName(String name)
    {
	for (Entry<Integer,User> kv : map.entrySet()) {
            if (kv.getValue().name.equals(name)) {
                return Just(new Entity<User>(kv.getKey(), kv.getValue()));
            }
	}
	return Nothing();
    }

    @Override
    public int insert(User m)
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

