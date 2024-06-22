package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.FlameC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class FlamethrowerEnchantment extends Enchantment {
    public static long flameTime;
    public static boolean flameo;
    public static Vec3 look;
    public FlamethrowerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (ModEnchantments.checkHelmEnchantments().contains("Flame")) {
                if (Keybindings.INSTANCE.flamethrower.isDown() && System.currentTimeMillis() >= flameTime + 15000 && !flameo) {
                    look = Minecraft.getInstance().player.getLookAngle();
                    flameo = true;
                    flameTime = System.currentTimeMillis();
                } else if (flameo && System.currentTimeMillis() <= flameTime + 3000) {
                    look = Minecraft.getInstance().player.getLookAngle();
                    ModMessages.sendToServer(new FlameC2SPacket());
                } else if (flameo) {
                    flameTime = System.currentTimeMillis();
                    flameo = false;
                }
            }
        }
    }*/
}
