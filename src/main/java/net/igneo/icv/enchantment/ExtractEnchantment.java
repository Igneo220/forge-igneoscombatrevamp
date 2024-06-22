package net.igneo.icv.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExtractEnchantment extends Enchantment {
    public ExtractEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentCategory.TRIDENT, pApplicableSlots);
    }
}
