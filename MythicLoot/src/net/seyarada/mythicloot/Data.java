package net.seyarada.mythicloot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Data {

	private static final List<UUID> loadedMobs = new ArrayList<>();
	private static final HashMap<UUID, HashMap<String, Double>> mythicLoot = new HashMap<>();
	private final UUID uuid;
	
	public Data(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void register() {
		loadedMobs.add(uuid);
	}
	
	public Iterator<UUID> getIterator() {
		return loadedMobs.iterator();
	}
	
	public void forget() {
		loadedMobs.remove(uuid);
	}
	
	public boolean isRegistered() {
		return loadedMobs.contains(uuid);
	}
	
	public HashMap<String, Double> get() {
		return mythicLoot.get(uuid);
	}
	
	public boolean exists() {
		return mythicLoot.containsKey(uuid);
	}
	
	public void put(HashMap<String, Double> i) {
		mythicLoot.put(uuid, i);
	}
	
	public Set<String> keys() {
		return mythicLoot.get(uuid).keySet();
	}
	
	public void remove() {
		mythicLoot.remove(uuid);
	}
	
}
