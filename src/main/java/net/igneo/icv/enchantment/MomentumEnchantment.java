package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MomentumC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class MomentumEnchantment extends Enchantment {
    public static long delay;
    public static int loopCount = 0;
    public static boolean shouldCheck = true;
    public static boolean spedUp;
    public MomentumEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostHurt(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if(FMLEnvironment.dist.isClient()) {
            if (pAttacker != Minecraft.getInstance().player) {
                loopCount = 0;
                ModMessages.sendToServer(new MomentumC2SPacket(0));
            }
        }
        super.doPostHurt(pTarget, pAttacker, pLevel);
    }

    public static void onClientTick() {
        if (uniPlayer.isSprinting()) {
            if (shouldCheck) {
                delay = System.currentTimeMillis();
                ++loopCount;
                shouldCheck = false;
            }
            if (System.currentTimeMillis() >= delay + 3000 && loopCount <= 3) {
                spedUp = true;
                if (loopCount != 0) {
                    ModMessages.sendToServer(new MomentumC2SPacket(loopCount));
                }
                shouldCheck = true;
            }
        } else if (spedUp){
            loopCount = 0;
            spedUp = false;
            ModMessages.sendToServer(new MomentumC2SPacket(0));
        }
    }
}
