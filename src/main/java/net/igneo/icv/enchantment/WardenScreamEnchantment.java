package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.WardenScreamC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WardenScreamEnchantment extends Enchantment {
    public static long wardenTime;
    public static Vec3 look;
    public static Vec3 playerpos;
    public static List<net.minecraft.world.entity.Entity> wardenhit;
    public WardenScreamEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Keybindings.wardenscream.isDown() && System.currentTimeMillis() >= wardenTime + 10000) {
            EnchantmentHudOverlay.screamFrames = 0;
            look = Minecraft.getInstance().player.getLookAngle();
            wardenTime = System.currentTimeMillis();
            //wardenhit.add(raycastEntities(Minecraft.getInstance().uniPlayer.getEyePosition(),Minecraft.getInstance().uniPlayer.getEyePosition().add(look)));
            playerpos = Minecraft.getInstance().player.getEyePosition();
            Minecraft.getInstance().player.addDeltaMovement(new Vec3(Minecraft.getInstance().player.getLookAngle().reverse().scale(0.5).x,0,Minecraft.getInstance().player.getLookAngle().reverse().scale(0.5).z));
            ModMessages.sendToServer(new WardenScreamC2SPacket());
        }
    }

}
