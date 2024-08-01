package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SmiteC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class SmiteEnchantment extends Enchantment {
    public static boolean smiting;
    public static long smiteTime;
    public static int boltsShot;
    public SmiteEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Keybindings.smite.isDown() && System.currentTimeMillis() >= smiteTime + 15000 && !smiting) {
            uniPlayer.setDeltaMovement(0,1,0);
            smiteTime = System.currentTimeMillis();
            boltsShot = 0;
            ModMessages.sendToServer(new SmiteC2SPacket(boltsShot));
            smiting = true;
        } else if (Keybindings.smite.isDown() && smiting && boltsShot <= 2 && !uniPlayer.onGround() && System.currentTimeMillis() >= smiteTime + 500) {
            smiteTime = System.currentTimeMillis();
            ++boltsShot;
            uniPlayer.addDeltaMovement(new Vec3(uniPlayer.getLookAngle().x/10,-0.2,uniPlayer.getLookAngle().z/10).reverse());
            ModMessages.sendToServer(new SmiteC2SPacket(boltsShot));
        } else if (boltsShot > 2 && smiting) {
            EnchantmentHudOverlay.smiteFrames = 0;
            smiteTime = System.currentTimeMillis();
            smiting = false;
        }
        if (uniPlayer.onGround() && System.currentTimeMillis() > smiteTime + 200 && smiting) {
            EnchantmentHudOverlay.smiteFrames = 0;
            smiting = false;
            smiteTime = System.currentTimeMillis();
        }
    }
}
