package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.igneo.icv.ICV;
import net.igneo.icv.client.animation.EnchantAnimationPlayer;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.loading.FMLEnvironment;

public abstract class WeaponEnchantManager extends EnchantmentManager {

    @OnlyIn(Dist.CLIENT)
    public KeyframeAnimationPlayer animation;


    protected WeaponEnchantManager(EnchantType setSlot, Player player, ResourceLocation animation) {
        super(setSlot, player);
        if (player instanceof LocalPlayer) {
            this.animator = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(ICV.MOD_ID, "enchant_animator"));
            this.animation = new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(animation));
        }
    }

    public void onAttack(Entity entity) {}

    @Override
    public boolean canUse() {
        return stableCheck();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void use() {
        if (this.canUse()) {
            activate();
        }
    }

    public void applyPassive() {
    }

    public void removePassive() {

    }

    @Override
    public void onEquip() {
        // this will play a sound later
    }

    @Override
    public void activate() {
        enchVar.animated = true;
        if (player.level().isClientSide) {
            System.out.println("should be animating...");
            this.animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "comet_strike"))));
        }
        active = true;
    }
}
