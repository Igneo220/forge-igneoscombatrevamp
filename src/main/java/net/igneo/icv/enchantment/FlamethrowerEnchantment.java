package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlizzardC2SPacket;
import net.igneo.icv.networking.packet.FlameC2SPacket;
import net.igneo.icv.networking.packet.MakeMeGlowC2SPacket;
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
    public static int flameDelay;

    private static boolean glowed = true;
    public FlamethrowerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (!glowed && System.currentTimeMillis() >= flameTime + 15000 && !flameo) {
            ModMessages.sendToServer(new MakeMeGlowC2SPacket());
            glowed = true;
        }

        if (Keybindings.flamethrower.isDown() && System.currentTimeMillis() >= flameTime + 15000 && !flameo) {
            flameo = true;
            flameTime = System.currentTimeMillis();
            flameDelay = 1000;
            glowed = false;
        } else if (flameo && System.currentTimeMillis() <= flameTime + 4000) {
            if (System.currentTimeMillis() >= flameTime + flameDelay) {
                ModMessages.sendToServer(new FlameC2SPacket(true));
                flameDelay += 75;
            } else if (flameDelay == 1000) {
                ModMessages.sendToServer(new FlameC2SPacket(false));
            }
        } else if (flameo) {
            EnchantmentHudOverlay.flameFrames = 0;
            flameTime = System.currentTimeMillis();
            flameo = false;
        }
    }
}
