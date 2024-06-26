package net.igneo.icv.enchantment;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static java.lang.Math.abs;

public class PhantomPainEnchantment extends Enchantment {

    public PhantomPainEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        System.out.println(pAttacker + " just got hit by " + pTarget);
        pTarget.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            //if (!FMLEnvironment.dist.isClient()) {
                ServerPlayer player = (ServerPlayer) pTarget;
                ServerLevel level = player.serverLevel();
                 //}

            if (enchVar.getPhantomVictim() !=null) {
                if (enchVar.getPhantomVictim() != pAttacker && enchVar.getPhantomVictim().isAlive()) {
                    level.sendParticles(player, ModParticles.PHANTOM_HEAL_PARTICLE.get(), true, enchVar.getPhantomVictim().getX(), enchVar.getPhantomVictim().getY() + 1.5, enchVar.getPhantomVictim().getZ(), 10, Math.random(), Math.random(), Math.random(), 0.5);
                    level.playSound(null, enchVar.getPhantomVictim().blockPosition(), ModSounds.PHANTOM_HEAL.get(), SoundSource.PLAYERS, 0.25F, (float) 0.3 + (float) abs(Math.random() + 0.5));
                    enchVar.getPhantomVictim().heal(enchVar.getPhantomHurt());
                    enchVar.resetPhantomHurt();
                    enchVar.deletePhantomVictim();
                    System.out.println("healing previous entity");
                    //if (!FMLEnvironment.dist.isClient()) {
                        //ServerPlayer player = (ServerPlayer) pTarget;
                        //ServerLevel level = player.serverLevel();
                        //}
                }
            }
            enchVar.setPhantomVictim((LivingEntity) pAttacker);
            enchVar.addPhantomHurt(4);
            enchVar.setPhantomDelay(System.currentTimeMillis());

            level.sendParticles(player, ModParticles.PHANTOM_HURT_PARTICLE.get(), true, pAttacker.getX(), pAttacker.getY() + 1.5, pAttacker.getZ(), 10, Math.random(), Math.random(), Math.random(), 0.5);
            level.playSound(null, pAttacker.blockPosition(), ModSounds.PHANTOM_HURT.get(), SoundSource.PLAYERS, 0.5F, (float) 0.3 + (float) abs(Math.random() + 0.5));

            System.out.println(enchVar.getPhantomVictim());
            System.out.println(enchVar.getPhantomHurt());
        });
        super.doPostAttack(pTarget, pAttacker, pLevel);
    }

    @Override
    public float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
        return 4;
    }
}
