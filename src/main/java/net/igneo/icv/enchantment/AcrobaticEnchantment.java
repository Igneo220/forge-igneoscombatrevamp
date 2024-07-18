package net.igneo.icv.enchantment;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.AcrobaticC2SPacket;
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
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import static java.lang.Math.abs;

public class AcrobaticEnchantment extends Enchantment {
    private static boolean flipping = false;
    private static long windowTimeout;
    private static boolean timeout = false;
    private static boolean startCount = false;
    public AcrobaticEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if (pTarget.level() instanceof ServerLevel) {
            pAttacker.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (enchVar.getAcrobatBonus()) {
                    ServerPlayer player = (ServerPlayer) pAttacker;
                    ServerLevel level = player.serverLevel();
                    level.sendParticles(ModParticles.ACRO_HIT_PARTICLE.get(), pTarget.getX(), pTarget.getY() + 1.5, pTarget.getZ(), 10, Math.random(), Math.random(), Math.random(), 0.5);
                    level.playSound(null, pTarget.blockPosition(), ModSounds.ACRO_HIT.get(), SoundSource.PLAYERS, 20F, (float) 0.3 + (float) abs(Math.random() + 0.5));
                    enchVar.setAcrobatBonus(false);
                }
            });
        }
        super.doPostAttack(pAttacker, pTarget, pLevel);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(1)).containsKey(ModEnchantments.ACROBATIC.get())) {
                if (!Minecraft.getInstance().options.keyLeft.isDown() && !Minecraft.getInstance().options.keyRight.isDown()) {
                    startCount = false;
                    timeout = false;
                } else if ((Minecraft.getInstance().options.keyLeft.isDown() && !Minecraft.getInstance().options.keyRight.isDown()) || (!Minecraft.getInstance().options.keyLeft.isDown() && Minecraft.getInstance().options.keyRight.isDown())) {
                    if (!startCount) {
                        windowTimeout = System.currentTimeMillis();
                        startCount = true;
                    }
                    if (System.currentTimeMillis() >= windowTimeout + 250) {
                        startCount = false;
                        timeout = true;
                    }
                } else if (Minecraft.getInstance().options.keyLeft.isDown() && Minecraft.getInstance().options.keyRight.isDown() && pPlayer.onGround() && !flipping && !timeout) {
                    flipping = true;
                    pPlayer.addDeltaMovement(new Vec3(0,0.5,0));
                    ModMessages.sendToServer(new AcrobaticC2SPacket());
                }
                if (flipping && pPlayer.onGround() && !Minecraft.getInstance().options.keyRight.isDown() && !Minecraft.getInstance().options.keyLeft.isDown()) {
                    flipping = false;
                }
            }
        }
    }
}
