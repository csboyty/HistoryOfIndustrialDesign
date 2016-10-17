package zhongyi.hid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.zhongyi.hid.util.JsonUtil;

public class HashMap2<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = -45127669982464219L;

	public HashMap2(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public HashMap2(int initialCapacity) {
		super(initialCapacity);
	}

	public HashMap2() {
		super();
	}

	public HashMap2(Map<? extends K, ? extends V> m) {
		super(m);
	}

	public HashMap2<K, V> put2(K key, V value) {
		super.put(key, value);
		return this;
	}

	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		Iterator<Entry<K, V>> i = entrySet().iterator();
		while (i.hasNext())
			hashCodeBuilder.append(i.next());
		return hashCodeBuilder.toHashCode();
	}
	
	public static void main(String[] args){
		 HashMap2<String,String> m1 = new HashMap2<String,String>();
		 m1.put("k-k1", "v-k1");
		 m1.put("k-k2", "v-k2");
		 m1.put("k-k3", "v-k3");
		 m1.put("k-a1", "v-a1");
		 System.out.println(m1.entrySet());
		 System.out.println(m1.hashCode());
		 
		 HashMap2<String,String> m2 = new HashMap2<String,String>();
		 m2.put("k-k2", "v-k2");
		 m2.put("k-k3", "v-k3");
		 m2.put("k-k1", "v-k1");
		 m2.put("k-a1", "v-a1");
		 System.out.println(m2.entrySet());
		 System.out.println(m2.hashCode());
		 
//		 System.out.println((byte)128);
		 String s= "{\"origin-data\":\"=sum(A1:A5)\",\"d\":\"=FBox[sum]({sheetIndex:undefined,ox:-5,oy:0,oex:-1,oey:0, ab:undefined})\",\"status\":\"dynamic\",\"result\":15,\"formula_version\":\"1.0\"}";
		System.out.println(JsonUtil.getJsonObj(s).get("d"));
		 
	}
}
