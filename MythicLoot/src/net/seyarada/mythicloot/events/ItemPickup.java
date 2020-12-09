package net.seyarada.mythicloot.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.util.jnbt.CompoundTag;
import net.seyarada.mythicloot.nms.V1_12_R1;
import net.seyarada.mythicloot.nms.V1_13_R1;
import net.seyarada.mythicloot.nms.V1_13_R2;
import net.seyarada.mythicloot.nms.V1_14_R1;
import net.seyarada.mythicloot.nms.V1_15_R1;
import net.seyarada.mythicloot.nms.V1_16_R1;
import net.seyarada.mythicloot.nms.V1_16_R2;
import net.seyarada.mythicloot.nms.V1_16_R3;

public class ItemPickup implements Listener {

	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player) {
			
			String p = ((Player)e.getEntity()).getName();
			CompoundTag a;
			try {
				a = MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(e.getItem().getItemStack());
			} catch (Exception k){
				return;
			}
			if(a.containsKey("mythicloot")) {
				if(a.getString("mythicloot").equals(p)) {
					
					String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
					
					if (version.equals("v1_16_R3")) {
						new V1_16_R3().removeNBT(e);

				    } else if (version.equals("v1_16_R2")) {
				    	new V1_16_R2().removeNBT(e);
				    	
				    } else if (version.equals("v1_16_R1")) {
				    	new V1_16_R1().removeNBT(e);
				    	
				    } else if (version.equals("v1_15_R1")) {
				    	new V1_15_R1().removeNBT(e);
				    	
				    } else if (version.equals("v1_14_R1")) {
				    	new V1_14_R1().removeNBT(e);
				    	
				    } else if (version.equals("v1_13_R2")) {
				    	new V1_13_R2().removeNBT(e);
				    	
				    } else if (version.equals("v1_13_R1")) {
				    	new V1_13_R1().removeNBT(e);
				    	
				    } else if (version.equals("v1_12_R1")) {
				    	new V1_12_R1().removeNBT(e);
				    }
				}
				else e.setCancelled(true);
			}
		}
	}
	
}
