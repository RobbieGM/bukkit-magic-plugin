package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfUpgrade extends Scroll {

	public ScrollOfUpgrade(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		PlayerInventory inv = player.getInventory();
		inv.setHelmet(upgrade(inv.getHelmet(), Material.LEATHER_HELMET, Material.IRON_HELMET, Material.DIAMOND_HELMET));
		inv.setChestplate(upgrade(inv.getChestplate(), Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE));
		inv.setLeggings(upgrade(inv.getLeggings(), Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS));
		inv.setBoots(upgrade(inv.getBoots(), Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS));
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.ENCHANTED_BOOK;
	}
	
	ItemStack upgrade(ItemStack item, Material... upgradeTiers) {
		if (item == null) return null;
		List<Material> upgrades = Arrays.asList(upgradeTiers);
		int currentIndex = upgrades.indexOf(item.getType());
		if (currentIndex == -1) return item;
		if (currentIndex == upgrades.size() - 1) {
			return randomEnchantment(item);
		} else {
			item.setType(upgrades.get(currentIndex + 1));
			return item;
		}
	}
	
    ItemStack randomEnchantment(ItemStack item) {
        List<Enchantment> possible = new ArrayList<Enchantment>();
     
        for (Enchantment ench: Enchantment.values()) {
            if (ench.canEnchantItem(item)) {
                possible.add(ench);
            }
        }
        
        if (possible.size() >= 1) {
            Enchantment chosen = possible.get((int) (Math.random() * possible.size()));
            item.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
        }

        return item;
    }

	@Override
	public String getLore() {
		return "Upgrades all your armor";
	}

	@Override
	public String getName() {
		return "Scroll of Upgrade";
	}

}
