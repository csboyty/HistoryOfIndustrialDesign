package redis;

import java.util.List;

public interface DomainRepository<V extends DomainObject> {

	void put(V obj);

	V get(V key);

	void delete(V key);

	List<V> getObjects();
}