package zhongyi.hid;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MySortMap<K, V> implements Map<K, V> {

	private TreeMap<K, V> internal = new TreeMap<K, V>();

	@Override
	public int size() {
		return internal.size();
	}

	@Override
	public boolean isEmpty() {
		return internal.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return internal.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return internal.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return internal.get(key);
	}

	@Override
	public V put(K key, V value) {
		return internal.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return internal.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		internal.putAll(m);
	}

	@Override
	public void clear() {
		internal.clear();
	}

	@Override
	public Set<K> keySet() {
		return internal.keySet();
	}

	@Override
	public Collection<V> values() {
		return internal.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return internal.entrySet();
	}

	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		Iterator<Entry<K, V>> i = entrySet().iterator();
		while (i.hasNext())
			hashCodeBuilder.append(i.next());
		return hashCodeBuilder.toHashCode();
	}

}
