package org.lordalex.lobbylcp.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Sandstone;

import java.util.ArrayList;
import java.util.List;

public class GameCompass {
    public static void openMiniGamesMenu(Player p){
        int size = 27;
        Inventory inv = Bukkit.createInventory(null, size, "LordWorld");

        ItemStack lobbyStack = new ItemStack(Material.IRON_BLOCK, 1);
        ItemMeta lobbyMeta = lobbyStack.getItemMeta();
        lobbyMeta.setDisplayName(ColorUtil.getMessage("&l&fВыбор лобби"));
        List<String> lobbyList = new ArrayList<>();
        lobbyList.add(ColorUtil.getMessage("&e Вы можете переключаться между лобби,"));
        lobbyList.add(ColorUtil.getMessage("&eчтобы быть вместе с друзьями."));
        lobbyMeta.setLore(lobbyList);
        lobbyStack.setItemMeta(lobbyMeta);

        ItemStack bedWarsStack = new ItemStack(Material.BED, 1);
        ItemMeta bedWarsMeta = bedWarsStack.getItemMeta();
        bedWarsMeta.setDisplayName(ColorUtil.getMessage("&l&bBedWars"));
        List<String> bedWarsList = new ArrayList<>();
        bedWarsList.add(ColorUtil.getMessage("&e Защитите свою кровать любой ценой!"));
        bedWarsList.add(ColorUtil.getMessage("&eНе дайте врагам осквернить ваше"));
        bedWarsList.add(ColorUtil.getMessage("&eпристанище!"));
        bedWarsList.add(ColorUtil.getMessage(" "));
        bedWarsList.add(ColorUtil.getMessage("&fДоступные варианты:"));
        bedWarsList.add(ColorUtil.getMessage("&5 - &bBedWars&a Hard"));
        bedWarsMeta.setLore(bedWarsList);
        bedWarsStack.setItemMeta(bedWarsMeta);


        inv.setItem(0, lobbyStack);
        inv.setItem(26, lobbyStack);
        inv.setItem(8, lobbyStack);
        inv.setItem(18, lobbyStack);
        inv.setItem(13, bedWarsStack);
        p.openInventory(inv);
    }
}
