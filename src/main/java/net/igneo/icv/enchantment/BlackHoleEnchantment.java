package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlockHoleC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class BlackHoleEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer;
    public static long blockHoleTime;
    public BlackHoleEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(3)).containsKey(ModEnchantments.BLACK_HOLE.get()) && System.currentTimeMillis() >= blockHoleTime + 29000 && Keybindings.INSTANCE.black_hole.isDown()) {
                ModMessages.sendToServer(new BlockHoleC2SPacket());
                blockHoleTime = System.currentTimeMillis();
            }
        }
    }*/
}
