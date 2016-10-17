package redis;

import java.io.Serializable;

public interface DomainObject extends Serializable {

	String getKey();
	 
	 String getObjectKey();
}
