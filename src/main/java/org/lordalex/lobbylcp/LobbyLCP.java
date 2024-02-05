package org.lordalex.lobbylcp;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;
import org.lordalex.lobbylcp.util.ColorUtil;
import org.lordalex.lobbylcp.util.Events;

import java.util.HashSet;
import java.util.UUID;

public final class LobbyLCP extends JavaPlugin implements PluginMessageListener, Listener {
    private static Plugin instance;
    public static HashSet<UUID> disabledSet = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(World world : Bukkit.getServer().getWorlds()) {
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setTime(3000);
                }
            }
        }, 0L, 20L);
    }
    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
    @EventHandler
    public void onGameClick(InventoryClickEvent e) {
        if (e == null) return;
        Player p = (Player) e.getView().getPlayer();

        if (e.getView().getTitle().equals("LordWorld")) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
                if (isEqualsItem(e, "&l&bBedWars")) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF("bedwars");
                    p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
                }
            }
            e.setCancelled(true);
        }
    }

//    @EventHandler
//    public void piston(final BlockPlaceEvent e) {
//        Player player = e.getPlayer();
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        out.writeUTF("Connect");
//        out.writeUTF("bedwars");
//        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
//    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    }
    public static Plugin getInstance(){
        return instance;
    }

    public boolean isEqualsItem(InventoryClickEvent e, String itemDisplayName){
        if(e.getCurrentItem().getItemMeta().getDisplayName() != null) {
            return e.getCurrentItem().getItemMeta().getDisplayName().equals(ColorUtil.getMessage(itemDisplayName));
        }
        else{
            return false;
        }
    }
}
