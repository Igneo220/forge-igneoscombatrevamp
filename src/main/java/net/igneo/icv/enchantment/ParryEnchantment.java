package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ParryEnchantment extends Enchantment {
    public static long parryCooldown;
    public static boolean parrying;
    public ParryEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    /*
    public static void onClientTick() {
        if (Keybindings.INSTANCE.parry.isDown() && ModEnchantments.checkChestEnchantments().contains("Parry") && System.currentTimeMillis() >= parryCooldown + 5000) {
            parryCooldown = System.currentTimeMillis();
            parrying = true;
            System.out.println(parrying);
        } else if (System.currentTimeMillis() >= parryCooldown + 250 && parrying){
            parrying = false;
            System.out.println(parrying);
        }
    }*/
}
