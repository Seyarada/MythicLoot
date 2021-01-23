package net.seyarada.mythicloot.events;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.seyarada.mythicloot.Data;

public class DamagedEvent implements Listener {

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		double finalDamage = event.getFinalDamage();
		Entity player = event.getDamager();

		if (player instanceof Projectile) {
			if (((Projectile) player).getShooter() instanceof Player) {
				player = (Player) (((Projectile) player).getShooter());
				register(entity, player, finalDamage);
				return;
			}
		}

		register(entity, player, finalDamage);
	}

	public void register(Entity target, Entity player, double finalDamage) {
		Data data = new Data(target.getUniqueId());

		if (data.isRegistered() && player instanceof Player) {

			String playerName = player.getName();
			double health = ((LivingEntity)target).getHealth();
			if(finalDamage>health) finalDamage = health;

			if (data.exists())
			{
				// Gets the damage map of the entity
				HashMap<String, Double> damageMap = data.get();

				// Checks if the entity already has that player mapped
				if (damageMap.containsKey(playerName)) {
					// Updates the map to add the new damage
					damageMap.put(playerName, damageMap.get(playerName) + finalDamage);
				} else {
					// Creates a new entry in the hashmap with the player and damage
					damageMap.put(playerName, finalDamage);}
				// Pushes the data to the main hashmap
				data.put(damageMap);

			} else {
				// Stores who damaged the mob and the amount
				HashMap<String, Double> damage = new HashMap<>();
				damage.put(playerName, finalDamage );
				// Creates a new entry in the hashmap with the player and damage
				data.put(damage);
			}


		}
	}

	
}
