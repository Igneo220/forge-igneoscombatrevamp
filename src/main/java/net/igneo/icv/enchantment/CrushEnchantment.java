package net.igneo.icv.enchantment;

import com.mojang.datafixers.util.Either;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CrushC2SPacket;
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

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CrushEnchantment extends Enchantment {
    public static boolean invincible;
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public CrushEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onKeyInputEvent() {
        pPlayer = Minecraft.getInstance().player;
        if (ModEnchantments.checkLegEnchantments().contains("net.igneo.icv.enchantment.CrushEnchantment")) {
            if (!pPlayer.onGround()) {
                if (ModEnchantments.checkBootEnchantments().contains("net.igneo.icv.enchantment.StoneCallerEnchantment")) {
                    System.out.println("wearing stone caller");
                    if (StoneCallerEnchantment.finishcall) {
                        pPlayer.setDeltaMovement(0, -1.5, 0);
                        invincible = true;
                    }
                } else {
                    pPlayer.setDeltaMovement(0, -1.5, 0);
                    invincible = true;
                }
            } else {
                if (invincible) {

                }
                invincible = false;
            }
        }
    }*/
}
