package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MomentumC2SPacket;
import net.igneo.icv.networking.packet.TrainDashC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class TrainDashEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long hitCooldown;
    public static long trainDelay = -5000;
    public static boolean dashing = false;
    static double lookX = 0;
    static double lookZ = 0;
    public static LivingEntity unluckyGirlfriend;
    static Vec3 look;
    public TrainDashEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(1)).containsKey(ModEnchantments.TRAIN_DASH.get()) && Keybindings.train_dash.isDown() && System.currentTimeMillis() >= trainDelay + 5000 && !dashing) {
                look = pPlayer.getLookAngle();
                dashing = true;
                lookX = look.x * 0.5;
                lookZ = look.z * 0.5;
                pPlayer.setDeltaMovement(lookX, pPlayer.getDeltaMovement().y, lookZ);
                trainDelay = System.currentTimeMillis();
                ModMessages.sendToServer(new TrainDashC2SPacket(3));
            } else if (dashing) {
                if (dashing) {
                    double d0 = Minecraft.getInstance().player.getDeltaMovement().x;
                    double d1 = Minecraft.getInstance().player.getDeltaMovement().y;
                    double d2 = Minecraft.getInstance().player.getDeltaMovement().z;

                    if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) <= 0.15) {
                        System.out.println((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)));
                        dashing = false;
                        ModMessages.sendToServer(new TrainDashC2SPacket(0));
                        trainDelay = System.currentTimeMillis();
                    }
                    pPlayer.setDeltaMovement(lookX, pPlayer.getDeltaMovement().y, lookZ);
                    for (LivingEntity entity : pPlayer.level().getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox())) {
                        if (entity != pPlayer) {
                            if (dashing && System.currentTimeMillis() >= hitCooldown + 500) {
                                hitCooldown = System.currentTimeMillis();
                                trainDelay = System.currentTimeMillis();
                                unluckyGirlfriend = pPlayer.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), pPlayer.getBoundingBox());
                                ModMessages.sendToServer(new TrainDashC2SPacket(1));
                            }
                        }
                    }
                    if (System.currentTimeMillis() >= trainDelay + 1500) {
                        dashing = false;
                        trainDelay = System.currentTimeMillis();
                    }
                }
            }
        }
    }
}
