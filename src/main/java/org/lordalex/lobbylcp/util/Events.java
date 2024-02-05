package org.lordalex.lobbylcp.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.lordalex.lobbylcp.LobbyLCP;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;
import static org.lordalex.lobbylcp.LobbyLCP.disabledSet;

public class Events implements Listener {
//    @EventHandler
//    public void piston(final BlockPlaceEvent e) {
//        Player player = e.getPlayer();
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        out.writeUTF("Connect");
//        out.writeUTF("bedwars");
//        player.sendPluginMessage(LobbyLCP.getInstance(), "BungeeCord", out.toByteArray());
//    }
//    public void onPlayerInteractEvent(PlayerInteractEvent e) {
//        Player player = e.getPlayer();
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        out.writeUTF("Connect");
//        out.writeUTF("bedwars");
//        player.sendPluginMessage(LobbyLCP.getInstance(), "BungeeCord", out.toByteArray());
//    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.getInventory().clear();
        e.setJoinMessage("Игрок " + p.getName() + " вошел в лобби!");

        Location loc = new Location(p.getWorld(), 0.5, 48.5, 0.5);
        p.teleport(loc);

        ItemStack compassStack = new ItemStack(Material.COMPASS, 1);
        ItemMeta compassMeta = compassStack.getItemMeta();
        compassMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Навигатор&f <<"));
        compassStack.setItemMeta(compassMeta);
        p.getInventory().setItem(0, compassStack);
        for(UUID uuid : disabledSet){
            Player playerWithHidenPlayers = Bukkit.getPlayer(uuid);
            if (p != playerWithHidenPlayers) {
                playerWithHidenPlayers.hidePlayer(p);
            }
        }

        if(disabledSet.contains(p.getUniqueId())) {
            ItemStack hideStack = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            ItemMeta hideMeta = hideStack.getItemMeta();
            hideMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Видимость игроков&f <<"));
            hideStack.setItemMeta(hideMeta);
            p.getInventory().setItem(8, hideStack);

            for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
                if (p != toHide) {
                    p.hidePlayer(toHide);
                }
            }
        }
        else {
            ItemStack showStack = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            ItemMeta showMeta = showStack.getItemMeta();
            showMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Видимость игроков&f <<"));
            showStack.setItemMeta(showMeta);
            p.getInventory().setItem(8, showStack);
        }

//        for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
//            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//                if (player != toHide) {
//                    player.hidePlayer(toHide);
//                }
//            }
//        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
            Player p = (Player) e.getEntity();
            Location loc = new Location(p.getWorld(), 0.5, 48.5, 0.5);
            p.teleport(loc);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem().getType() == Material.INK_SACK) {
            //HIDE
            if (!disabledSet.contains(p.getUniqueId())) {
                disabledSet.add(p.getUniqueId());
                for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
                    if (p != toHide) {
                        p.hidePlayer(toHide);
                    }
                }
                ItemStack hideStack = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                ItemMeta hideMeta = hideStack.getItemMeta();
                hideMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Видимость игроков&f <<"));
                hideStack.setItemMeta(hideMeta);
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), hideStack);
                p.sendMessage(ColorUtil.getMessage("&fВидимость игроков:&c выключена"));
            }

            //SHOW
            else {
                disabledSet.remove(p.getUniqueId());
                for (Player toShow : Bukkit.getServer().getOnlinePlayers()) {
                    if (p != toShow) {
                        p.showPlayer(toShow);
                    }
                }
                ItemStack showStack = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                ItemMeta showMeta = showStack.getItemMeta();
                showMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Видимость игроков&f <<"));
                showStack.setItemMeta(showMeta);
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), showStack);
                p.sendMessage(ColorUtil.getMessage("&fВидимость игроков:&a включена"));
            }
        }
        else if(e.getItem().getType() == Material.COMPASS){
            GameCompass.openMiniGamesMenu(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName() != null) {
            if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ColorUtil.getMessage("&f >>&e&l Навигатор&f <<"))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(InventoryClickEvent e){
        if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null){
            if(isEqualsItem(e, ColorUtil.getMessage("&f >>&e&l Навигатор&f <<")) ||
                    isEqualsItem(e, ColorUtil.getMessage("&f >>&e&l Видимость игроков&f <<"))){
                e.setCancelled(true);
            }
        }
        if(e.getHotbarButton() == 0 || e.getHotbarButton() == 8 || e.getSlot() == 0 || e.getSlot() == 8){
            e.setCancelled(true);
        }
        Player p = (Player) e.getWhoClicked();
        p.sendMessage(String.valueOf(e.getHotbarButton()));
        p.sendMessage(String.valueOf(e.getSlot()));
        p.sendMessage(String.valueOf(e.getRawSlot()));
        p.sendMessage(String.valueOf(e.getAction().toString()));
        p.sendMessage(String.valueOf(e.getCursor().toString()));
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
