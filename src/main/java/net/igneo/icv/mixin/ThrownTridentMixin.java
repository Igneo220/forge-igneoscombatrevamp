package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.ExtractUpdateS2CPacket;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ThrownTrident.class,priority = 999999999)
public class ThrownTridentMixin extends AbstractArrow{
    @Shadow
    static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BYTE);
    @Shadow
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> ID_EXTRACT = SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BOOLEAN);
    @Shadow
    private ItemStack tridentItem;
    @Unique
    boolean extract = false;
    @Unique
    public int idk;

    //public ThrownTridentMixin(EntityType<? extends ThrownTrident> pEntityType, Level pLevel) {super(pEntityType, pLevel);}

    public ThrownTridentMixin(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(EntityType.TRIDENT, pShooter, pLevel);
        if (EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.EXTRACT.get())) {
            extract = true;
        }
        idk = pStack.getEnchantmentLevel(Enchantments.LOYALTY);
        this.entityData.set(ID_EXTRACT, EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.EXTRACT.get()));
        this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(pStack));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)0);
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(ID_EXTRACT,false);
    }

    @Inject(method = "tick" ,at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (EnchantmentHelper.getEnchantments(this.getPickupItem()).containsKey(ModEnchantments.EXTRACT.get())) {
            if (!this.getTags().contains("EXTRACT")) {
                if (!this.level().isClientSide) {
                    ServerLevel level = (ServerLevel) this.level();
                    for (ServerPlayer player: level.players()) {
                        ModMessages.sendToPlayer(new ExtractUpdateS2CPacket(this.getId()),player);
                    }
                }
                this.getTags().add("EXTRACT");
            }
        }
    }
    @Inject(method = "onHitEntity" ,at = @At("TAIL"))
    protected void onHitEntity(EntityHitResult pResult,CallbackInfo ci) {
        Entity entity = pResult.getEntity();
        if (entity.getType() != EntityType.ENDERMAN) {
            pullEntity(entity);
        }
    }
    protected void pullEntity(Entity pEntity) {
        if (this.getTags().contains("EXTRACT")) {
            if (this.level() instanceof ServerLevel) {
                ServerLevel level = (ServerLevel) this.level();
                level.playSound(null,this.blockPosition(), ModSounds.EXTRACT.get(), SoundSource.PLAYERS);
            }
            Entity entity = this.getOwner();
            if (entity != null) {
                double scale = 0.1;
                double d0 = entity.getX() - this.getX();
                double d1 = entity.getY() - this.getY();
                double d2 = entity.getZ() - this.getZ();

                if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) <= 20) {
                    scale = 0.25;
                }
                if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) >= 35) {
                    scale = 0.05;
                }

                pEntity.setDeltaMovement(0,1,0);
                Vec3 vec3 = (new Vec3(d0, pEntity.getDeltaMovement().y, d2)).scale(scale);
                pEntity.setDeltaMovement(vec3);
            }
        }
    }

    @Override
    public ItemStack getPickupItem() {
        return this.tridentItem.copy();
    }

}
