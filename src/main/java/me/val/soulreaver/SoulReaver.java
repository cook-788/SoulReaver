package me.val.soulreaver;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SoulReaver extends JavaPlugin implements Listener {

    private Player holder = null;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private ItemStack createSoulReaver() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Soul Reaver");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Holder enters Hardcore Mode"));
        sword.setItemMeta(meta);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        return sword;
    }

    private boolean isSoulReaver(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasDisplayName()) return false;
        return item.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Soul Reaver");
    }

    @EventHandler
    public void onHold(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (isSoulReaver(item)) {
            holder = player;
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has entered HARDCORE MODE!");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.equals(holder)) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " has fallen and is permanently banned!");
            player.setBanned(true);
            holder = null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("soulreaver")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getInventory().addItem(createSoulReaver());
                player.sendMessage(ChatColor.GOLD + "You have received the Soul Reaver.");
                return true;
            }
        }
        return false;
    }
}
