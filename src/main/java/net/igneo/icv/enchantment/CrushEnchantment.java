package net.igneo.icv.enchantment;

import com.mojang.datafixers.util.Either;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CrushC2SPacket;
import net.igneo.icv.networking.packet.CrushSoundC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CrushEnchantment extends Enchantment {
    private static long crushTime = 0;
    private static long floatTime = 0;
    private static double fallDistance;
    public CrushEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(1)).containsKey(ModEnchantments.CRUSH.get())) {
                if (pPlayer.onGround()) {
                    crushTime = System.currentTimeMillis();
                    floatTime = 0;
                }
                if (!pPlayer.onGround() && System.currentTimeMillis() > crushTime + 600 && Minecraft.getInstance().options.keyShift.isDown() && !pPlayer.isPassenger() && !pPlayer.isInFluidType()) {
                    if (floatTime == 0) {
                        floatTime = System.currentTimeMillis();
                    }
                    if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.STONE_CALLER.get()) || EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.SKY_CHARGE.get())) {
                        System.out.println("wearing stone caller or sky charge");
                        if (pPlayer.getDeltaMovement().y < 0) {
                            if (System.currentTimeMillis() < floatTime + 300) {
                                pPlayer.setDeltaMovement(0, 0, 0);
                            } else {
                                if (fallDistance == 0) {
                                    ModMessages.sendToServer(new CrushSoundC2SPacket());
                                }
                                fallDistance = pPlayer.fallDistance;
                                pPlayer.setDeltaMovement(0, -1.5, 0);
                            }
                        }
                    } else {
                        if (System.currentTimeMillis() < floatTime + 300) {
                            pPlayer.setDeltaMovement(0, 0, 0);
                        } else {
                            if (fallDistance == 0) {
                                ModMessages.sendToServer(new CrushSoundC2SPacket());
                            }
                            fallDistance = pPlayer.fallDistance;
                            pPlayer.setDeltaMovement(0, -1.5, 0);
                        }
                    }
                } else {
                    if (Minecraft.getInstance().options.keyShift.isDown()){
                        if (fallDistance > 3) {
                            ModMessages.sendToServer(new CrushC2SPacket());
                        }
                        fallDistance = 0;
                        floatTime = 0;
                    }
                }
            }
        }
    }
}
