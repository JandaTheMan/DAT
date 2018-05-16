
package dat.prac3.model.dao;


public class Entity<A>
{

    public final int id;
    public final A value;


    public Entity(int id, A value) {
        this.id = id;
        this.value = value;
    }
    public static <A> Entity<A> Entity(int id, A value) {
        return new Entity<A>(id, value);
    }


    public String toString() {
	return "Entity(" + id + "," + value.toString() + ")";
    }

}


