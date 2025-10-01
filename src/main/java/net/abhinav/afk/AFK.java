package net.abhinav.afk;

import org.bukkit.Bukkit;
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

    private static final String AFK_META = "afk";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Nothing needed here for now
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("afk")) {
            Player player = (Player) sender;
            boolean nowAfk = !isAfk(player);
            setAfk(player, nowAfk);
            player.sendMessage(nowAfk ? "You are now AFK." : "You are no longer AFK.");
            return true;
        }
        return false;
    }

    private void setAfk(Player player, boolean afk) {
        player.setMetadata(AFK_META, new FixedMetadataValue(this, afk));
    }

    private boolean isAfk(Player player) {
        return player.hasMetadata(AFK_META) &&
               Boolean.TRUE.equals(player.getMetadata(AFK_META).get(0).asBoolean());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && isAfk(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isAfk(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isAfk(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
