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

public class MomentumEnchantment extends Enchantment {
    private static long delay;
    private static int loopCount = 0;
    private static boolean shouldCheck = true;
    private static boolean spedUp;
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
        LocalPlayer pPlayer = Minecraft.getInstance().player;
        if (pPlayer != null) {
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.MOMENTUM.get())) {
                if (pPlayer.isSprinting()) {
                    if (shouldCheck) {
                        delay = System.currentTimeMillis();
                        ++loopCount;
                        shouldCheck = false;
                    }
                    if (System.currentTimeMillis() >= delay + 3000 && loopCount <= 3) {
                        spedUp = true;
                        System.out.println("increasing speed!!!");
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
                if (spedUp) {
                    double d0 = pPlayer.getDeltaMovement().x;
                    double d1 = pPlayer.getDeltaMovement().y;
                    double d2 = pPlayer.getDeltaMovement().z;

                    if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) <= 0.15) {
                        System.out.println((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)));
                        spedUp = false;
                        ModMessages.sendToServer(new MomentumC2SPacket(0));
                        loopCount = 0;
                    }
                }

            }
        }
    }
}
