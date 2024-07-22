package net.igneo.icv.enchantment;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.KineticC2SPacket;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class KineticEnchantment extends Enchantment{
    private static boolean kin;
    public KineticEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getMainHandItem()).containsKey(ModEnchantments.KINETIC.get())) {
                if (Minecraft.getInstance().mouseHandler.isLeftPressed()) {
                    ModMessages.sendToServer(new KineticC2SPacket(pPlayer.getDeltaMovement().x, pPlayer.getDeltaMovement().z));
                    kin = true;
                } else if (kin){
                    //ModMessages.sendToServer(new KineticC2SPacket(0,0));
                    kin = false;
                }
            }
        }
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if (pAttacker.level() instanceof ServerLevel) {
            pAttacker.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (pAttacker instanceof ServerPlayer) {
                    ServerLevel level = ((ServerPlayer) pAttacker).serverLevel();
                    level.sendParticles(ModParticles.KINETIC_HIT_PARTICLE.get(), pTarget.getX(), pTarget.getEyeY(), pTarget.getZ(), 15, 1, 1, 1, 1);
                    ServerPlayer player = (ServerPlayer) pAttacker;
                    double f = ((Math.abs(enchVar.getKinX() + (Math.abs(enchVar.getKinZ())))));
                    level.playSound(null, pAttacker.blockPosition(), ModSounds.KINETIC_HIT.get(), SoundSource.PLAYERS, 0.5F, (float) f);
                }
            });
        }
        super.doPostAttack(pAttacker, pTarget, pLevel);
    }
}
