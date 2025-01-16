package net.igneo.icv.enchantmentActions.enchantManagers;

import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class EnchantmentManager {
    protected EnchantmentManager(EnchantType setSlot, Player player) {
        this.type = setSlot;
        this.player = player;
    }
    public Player player;
    private final EnchantType type;

    public EnchantType getType() {
        return type;
    }
    public void use() {
        this.activate();
    }

    public abstract void activate();
}
