package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.entity.Player;

class ItemCooldownKey {

	public final Player p;
	public final UseableItem i;

	public ItemCooldownKey(final Player p, final UseableItem i) {
		this.p = p;
		this.i = i;
	}

	public boolean equals(final Object O) {
		if (!(O instanceof ItemCooldownKey))
			return false;
		if (!((ItemCooldownKey) O).p.equals(p))
			return false;
		if (!((ItemCooldownKey) O).i.equals(i))
			return false;
		return true;
	}

	public int hashCode() {
		return p.hashCode() << 16 + i.hashCode();
	}

}