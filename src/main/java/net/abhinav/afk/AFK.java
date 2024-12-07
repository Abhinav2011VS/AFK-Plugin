package net.abhinav.afk;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class AFK extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Command to toggle AFK state
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("afk") && sender instanceof Player) {
            Player player = (Player) sender;
            if (isAfk(player)) {
                setAfk(player, false);
                player.sendMessage("You are no longer AFK.");
            } else {
                setAfk(player, true);
                player.sendMessage("You are now AFK.");
            }
            return true;
        }
        return false;
    }

    // Track AFK status in memory (you could also save it to a file or database if needed)
    private void setAfk(Player player, boolean afk) {
        player.setMetadata("afk", new FixedMetadataValue(this, afk));
    }

    private boolean isAfk(Player player) {
        return player.hasMetadata("afk") && player.getMetadata("afk").get(0).asBoolean();
    }

    // Prevent player from being hit while AFK
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isAfk(player)) {
                event.setCancelled(true); // Cancel damage if the player is AFK
            }
        }
    }

    // Prevent player from moving while AFK
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isAfk(player)) {
            // Cancel movement if the player is AFK and attempting to move
            event.setCancelled(true);
        }
    }

    // Prevent interaction if AFK
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isAfk(player)) {
            event.setCancelled(true); // Cancel interaction if the player is AFK
        }
    }
}
