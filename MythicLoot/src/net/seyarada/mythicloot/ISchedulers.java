package net.seyarada.mythicloot;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.utils.Schedulers;
import io.lumine.xikage.mythicmobs.utils.terminable.Terminable;
import net.seyarada.mythicloot.nms.V1_12_R1;
import net.seyarada.mythicloot.nms.V1_13_R1;
import net.seyarada.mythicloot.nms.V1_13_R2;
import net.seyarada.mythicloot.nms.V1_14_R1;
import net.seyarada.mythicloot.nms.V1_15_R1;
import net.seyarada.mythicloot.nms.V1_16_R1;
import net.seyarada.mythicloot.nms.V1_16_R3;
import net.seyarada.mythicloot.rank.Util;

public class ISchedulers implements Terminable {

	Util u = new Util();
	Color rgb;
	
	public void explodeParticles(Item item, Player p, String color, double expheight, double expoffset) {

		String fColor = u.getColor(item, color);
		rgb = u.getRGB(fColor);
		
		Random random = new Random();
		Vector velocity = new Vector(Math.cos(random.nextDouble() * 3.141592653589793D * 2.0D) * expoffset, expheight, Math.sin(random.nextDouble() * 3.141592653589793D * 2.0D) * expoffset);

		item.setVelocity(velocity);
		
		Schedulers.sync().runRepeating(() -> {
	         this.ePSchedueler(item, p);
	      }, 0, 1);
			
	}
	
	public void ePSchedueler(Item item, Player p) {
		
		if (!item.isOnGround()&&item.isValid()) {
			DustOptions dustOptions = new DustOptions(rgb, 1);
			p.spawnParticle(Particle.REDSTONE, item.getLocation(), 1, dustOptions);
            }
            else {
            	this.terminate();
            }
		
	}
	
	public void keepInvisible(Item item, String player) {

		Schedulers.sync().runRepeating(() -> {
	         this.kISchedueler(item, player);
	      }, 0, 40);
	}
	
	public void kISchedueler(Item item, String player) {
		if (item.isValid()) {
        	for (Entity entity : item.getNearbyEntities(24, 24, 24)){
        		if (entity instanceof Player && !((Player)entity).getName().equals(player)){
        			
        			String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
					
					if (version.equals("v1_16_R3")) {
						new V1_16_R3().destroyEntity(item, entity);

				    } else if (version.equals("v1_16_R2")) {
				    	new V1_16_R3().destroyEntity(item, entity);
				    	
				    } else if (version.equals("v1_16_R1")) {
				    	new V1_16_R1().destroyEntity(item, entity);
				    	
				    } else if (version.equals("v1_15_R1")) {
				    	new V1_15_R1().destroyEntity(item, entity);
				    	
				    } else if (version.equals("v1_14_R1")) {
				    	new V1_14_R1().destroyEntity(item, entity);
				    	
				    } else if (version.equals("v1_13_R2")) {
				    	new V1_13_R2().destroyEntity(item, entity);
				    	
				    } else if (version.equals("v1_13_R1")) {
				    	new V1_13_R1().destroyEntity(item, entity);
				    	
				    } else if (version.equals("v1_12_R1")) {
				    	new V1_12_R1().destroyEntity(item, entity);
				    }
        			
		         }
		    }
        }
        else {
        	this.terminate();
        }
	}

	@Override
	public void close() throws Exception {
	}

	
}
