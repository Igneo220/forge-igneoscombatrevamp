package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.FlameC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class FlamethrowerEnchantment extends Enchantment {
    public static long flameTime;
    public static boolean flameo;
    private static int flameDelay;
    private static Vec3 look;
    public FlamethrowerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(3)).containsKey(ModEnchantments.FLAMETHROWER.get())) {
                if (Keybindings.flamethrower.isDown() && System.currentTimeMillis() >= flameTime + 15000 && !flameo) {
                    look = Minecraft.getInstance().player.getLookAngle();
                    flameo = true;
                    flameTime = System.currentTimeMillis();
                    flameDelay = 0;
                } else if (flameo && System.currentTimeMillis() <= flameTime + 3000) {
                    if (System.currentTimeMillis() >= flameTime + flameDelay) {
                        look = Minecraft.getInstance().player.getLookAngle();
                        ModMessages.sendToServer(new FlameC2SPacket());
                        flameDelay += 75;
                    }
                } else if (flameo) {
                    EnchantmentHudOverlay.flameFrames = 0;
                    flameTime = System.currentTimeMillis();
                    flameo = false;
                }
            } else {
                EnchantmentHudOverlay.flameFrames = 0;
                flameTime = System.currentTimeMillis();
            }
        }
    }
}
