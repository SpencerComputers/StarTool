package com.HomeSMP.StarTool;
// Java
import java.util.ArrayList;
import java.util.List;

// Bukkit
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

// Bungee
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin  implements Listener {
	
	public List <String> list = new ArrayList<String>();
	
	@Override
		public void onEnable() {
			// startup
			// reloads
			// plugin reloads
			this.getServer().getPluginManager().registerEvents(this, this);
		}
		
		@Override
		public void onDisable() {
			// shutdown
			// reloads
			// plugin reloads
		}
		
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (label.equalsIgnoreCase("startool")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You cannot run this command");
					return true;
				}
				Player player = (Player) sender;
				if (player.getInventory().firstEmpty() == -1) {
					Location loc = player.getLocation();
					World world = player.getWorld();
					
					world.dropItemNaturally(loc, getItem());
					player.sendMessage(ChatColor.GOLD + "The Gods of Minecraft dropped a gift near you.");
					return true;
				}
				player.getInventory().addItem(getItem());
				player.sendMessage(ChatColor.GOLD + "The Gods of Minecraft gave you a gift.");
				return true;
			}
			return false;
		}
		
		public ItemStack getItem() {
			ItemStack item = new ItemStack(Material.TRIDENT);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Ancient Trident");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Right Click) &a&oSpawn minions"));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Left Click) &a&oShoot Explosives"));
			meta.setLore(lore);
			
			meta.addEnchant(Enchantment.LOYALTY, 10, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			
			item.setItemMeta(meta);
			
			return item;
		}
		
		@EventHandler()
		public void onClick(PlayerInteractEvent event) {
			if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TRIDENT))
				if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
					Player player = (Player) event.getPlayer();
					// Right Click
					if (event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (!list.contains(player.getName()))
							list.add(player.getName());
						return;
					}
					
					// Left Click
					if (event.getAction() == Action.LEFT_CLICK_AIR) {
						player.launchProjectile(Fireball.class);
					}
				}
			if (list.contains(event.getPlayer().getName())) {
				list.remove(event.getPlayer().getName());
			}
		}
		
		@EventHandler()
		public void onLand(ProjectileHitEvent event) {
			if (event.getEntityType() == EntityType.TRIDENT) {
				if (event.getEntity().getShooter() instanceof Player) {
					Player player = (Player) event.getEntity().getShooter();
					if (list.contains(player.getName())) {
						// Spawn Zombies
						Location loc = event.getEntity().getLocation();
						loc.setY(loc.getY() + 1);
						
						for (int i = 1; i < 4 ; i++) {
							loc.getWorld().spawnEntity(loc, EntityType.DROWNED);
							loc.setX(loc.getX() + i);
						}
					}
				}
			}
		}
}
