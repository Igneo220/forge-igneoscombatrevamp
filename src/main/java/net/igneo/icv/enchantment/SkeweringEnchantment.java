package net.igneo.icv.enchantment;

import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static java.lang.Math.abs;

public class SkeweringEnchantment extends Enchantment {
    public SkeweringEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if(!pAttacker.onGround() && !pAttacker.isInFluidType() && !pAttacker.isPassenger()){
            ServerPlayer player = (ServerPlayer) pTarget;
            ServerLevel level = player.serverLevel();
            level.playSound(null,pAttacker.blockPosition(), ModSounds.SKEWERING_HIT.get(), SoundSource.PLAYERS,1.5F, (float) abs(Math.random() + 0.5));
            level.sendParticles(ModParticles.SKEWERING_PARTICLE.get(),
                    pAttacker.getX(),pAttacker.getY() + 1.5,pAttacker.getZ(),10,
                    Math.random(),Math.random(),Math.random(),0.5);
            //pAttacker.hurt(level.damageSources().fellOutOfWorld(),4);
        }
        super.doPostAttack(pTarget, pAttacker, pLevel);
    }
}
