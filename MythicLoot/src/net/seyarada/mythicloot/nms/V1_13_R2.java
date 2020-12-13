package net.seyarada.mythicloot.nms;

import net.minecraft.server.v1_13_R2.*;
import net.seyarada.mythicloot.MythicLoot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class V1_13_R2 {
	
	public void removeNBT(EntityPickupItemEvent e) {
		ItemStack removeNBT = CraftItemStack.asNMSCopy(e.getItem().getItemStack());
	    removeNBT.c("mythicloot");
	    e.getItem().remove();
	    e.setCancelled(true);
	    ((Player)e.getEntity()).getInventory().addItem(CraftItemStack.asBukkitCopy(removeNBT));
	}
	
	public void destroyEntity(Item item, Entity entity) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(item.getEntityId());
		((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
	}

	public void spawnHologram(UUID uuid, Player player, List<String> messages) {

		Location location = Bukkit.getEntity(uuid).getLocation();
		WorldServer wS = ((CraftWorld)location.getWorld()).getHandle();
		double lX = location.getX();
		double lY = location.getY()+1.2;
		double lZ = location.getZ();

		for(String msg : messages) {
			lY += 0.2;
			if(!msg.isEmpty()) {
				final EntityArmorStand armorStand = new EntityArmorStand(wS);
				armorStand.setPosition(lX, lY, lZ);
				armorStand.setCustomName(CraftChatMessage.fromStringOrNull(msg));
				armorStand.setCustomNameVisible(true);
				armorStand.setInvisible(true);
				armorStand.setMarker(true);
				PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(armorStand, 1);
				PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);

				if(player!=null && player.isOnline() && ((CraftPlayer) player).getHandle()!=null) {
					final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
					connection.sendPacket(packetPlayOutSpawnEntity);
					connection.sendPacket(metadata);
				}

				new BukkitRunnable() {

					@Override
					public void run() {
						if(player.isOnline()) {
							PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(armorStand.getId());
							((CraftPlayer)player).getHandle().playerConnection.sendPacket(destroy);
						}
					}

				}.runTaskLater(JavaPlugin.getPlugin(MythicLoot.class), 300);
			}
		}

	}

}
