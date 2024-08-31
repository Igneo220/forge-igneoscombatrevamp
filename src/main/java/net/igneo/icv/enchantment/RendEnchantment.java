package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.RendC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class RendEnchantment extends Enchantment {
    private static Entity rendEntity = null;
    private static float rendCount;
    public RendEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void rendHit(Entity hitEntity) {
        if (!(hitEntity.level() instanceof ServerLevel)) {
            if (rendEntity == null) {
                rendEntity = hitEntity;
                rendCount = 0;
            } else if (rendEntity != hitEntity) {
                rendEntity = hitEntity;
                rendCount = 0;
            }
            rendCount += 1;
        }
    }

    public static void onKeyInputEvent() {
        if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getMainHandItem()).containsKey(ModEnchantments.REND.get()) && rendEntity != null && rendCount > 0) {
            int i = (int) (((rendCount * rendCount)*2) + 6);
            i = i/10;
            Vec3 distVec = Minecraft.getInstance().player.position().subtract(rendEntity.position());
            double dist = Math.abs(distVec.x) + Math.abs(distVec.z);
            if (dist > 30) {
                dist = 30;
            }
            System.out.println(dist);
            i = (int) (i*(dist/2));
            if (i > uniPlayer.getHealth()) {
                i = (int) uniPlayer.getHealth();
            }
            ModMessages.sendToServer(new RendC2SPacket(rendEntity.getId(),i));
            System.out.println(i);
            rendEntity = null;
        }
    }
}
