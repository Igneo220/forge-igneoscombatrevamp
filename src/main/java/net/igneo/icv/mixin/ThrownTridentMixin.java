package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ThrownTrident.class,priority = 999999999)
public class ThrownTridentMixin extends AbstractArrow{
    @Shadow
    private boolean dealtDamage;
    @Shadow
    static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BYTE);
    @Shadow
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BOOLEAN);
    @Shadow
    public int clientSideReturnTridentTickCount;
    @Unique
    private static final EntityDataAccessor<Byte> ID_EXTRACT = SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BYTE);
    @Shadow
    private ItemStack tridentItem;
    @Unique
    boolean extract = false;
    @Unique
    public int idk;

    //public ThrownTridentMixin(EntityType<? extends ThrownTrident> pEntityType, Level pLevel) {super(pEntityType, pLevel);}

    public ThrownTridentMixin(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(EntityType.TRIDENT, pShooter, pLevel);
        //System.out.println("not here!");
        //tridentItem = new ItemStack(Items.TRIDENT);
        //this.tridentItem = pStack.copy();
        //for (Enchantment enchant: pStack.getAllEnchantments()) {
        //    this.tridentItem.enchant(enchant);
        //}
        //System.out.println(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.EXTRACT.get(),pShooter));
        //idk = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.EXTRACT.get(),pShooter);
        if (EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.EXTRACT.get())) {
            extract = true;
        }
        idk = pStack.getEnchantmentLevel(Enchantments.LOYALTY);
        //this.entityData.set(ID_EXTRACT, (byte)EnchantmentHelperMixin.getExtract(pStack));
        this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(pStack));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)0);
        this.entityData.define(ID_FOIL, false);
    }

    /**
     * @author Igneo
     * @reason tridents SUCK
     */
    @Overwrite
    public void tick() {
        if (this.entityData.get(ID_LOYALTY) > 0) {
            if (!this.getTags().contains("EXTRACT")) {
                this.getTags().add("EXTRACT");
            }
        }
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = 3;//this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    /**
     * @author Igneo
     * @reason specific spot
     */
    @Overwrite
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.tridentItem, livingentity.getMobType());
        }
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() != EntityType.ENDERMAN) {
                entity.hurt(damagesource,f);
                pullEntity(entity);
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling()) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer)entity1 : null);
                    this.level().addFreshEntity(lightningbolt);
                    soundevent = SoundEvents.TRIDENT_THUNDER;
                    f1 = 5.0F;
                }
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }
    protected void pullEntity(Entity pEntity) {
        System.out.println(this.entityData.get(ID_LOYALTY));
        if (this.entityData.get(ID_LOYALTY) > 0) {
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
                //System.out.println("x: " + d0);
                //System.out.println("y: " + d1);
                //System.out.println("z: " + d2);
                if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) <= 20) {
                    scale = 0.25;
                }
                if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) >= 35) {
                    scale = 0.05;
                }
                //System.out.println(scale);
                Vec3 vec3 = (new Vec3(d0, d1, d2)).scale(scale);
                pEntity.setDeltaMovement(vec3);
            }
        }
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.tridentItem);
    }

    protected boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.tridentItem.copy();
    }

}
