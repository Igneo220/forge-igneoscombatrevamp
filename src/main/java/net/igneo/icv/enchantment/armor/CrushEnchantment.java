package net.igneo.icv.enchantment.armor;

import com.mojang.datafixers.util.Either;
import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CrushC2SPacket;
import net.igneo.icv.networking.packet.CrushSoundC2SPacket;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class CrushEnchantment extends Enchantment {
    private static long crushTime = 0;
    private static long floatTime = 0;
    private static double fallDistance;

    private static boolean pressed = false;
    public CrushEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().options.keyShift.isDown()) {
            pressed = true;
        } else if (!uniPlayer.onGround() && pressed) {
            uniPlayer.addDeltaMovement(new Vec3(0, -3, 0));
            pressed = false;
        } else {
            pressed = false;
        }
        if (uniPlayer.onGround()) {
            if (Minecraft.getInstance().options.keyShift.isDown()) {
                if (fallDistance > 3) {
                    ModMessages.sendToServer(new CrushC2SPacket());
                }
            }
            crushTime = System.currentTimeMillis();
            fallDistance = 0;
            floatTime = 0;
        } else if (System.currentTimeMillis() > crushTime + 600 && Minecraft.getInstance().options.keyShift.isDown() && !uniPlayer.isPassenger() && !uniPlayer.isInFluidType()) {
            if (floatTime == 0) {
                floatTime = System.currentTimeMillis();
            }
            if (EnchantmentHelper.getEnchantments(uniPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.STONE_CALLER.get())) {
                if (uniPlayer.getDeltaMovement().y < 0) {
                    if (System.currentTimeMillis() < floatTime + 150) {
                        uniPlayer.setDeltaMovement(0, 0, 0);
                    } else {
                        if (fallDistance == 0) {
                            ModMessages.sendToServer(new CrushSoundC2SPacket());
                        }
                        fallDistance = uniPlayer.fallDistance;
                        uniPlayer.addDeltaMovement(new Vec3(0, -1.5, 0));
                    }
                }
            } else {
                if (System.currentTimeMillis() < floatTime + 150) {
                    uniPlayer.setDeltaMovement(0, 0, 0);
                } else {
                    if (fallDistance == 0) {
                        ModMessages.sendToServer(new CrushSoundC2SPacket());
                    }
                    fallDistance = uniPlayer.fallDistance;
                    uniPlayer.addDeltaMovement(new Vec3(0, -1.5, 0));
                }
            }
        }
    }
}
