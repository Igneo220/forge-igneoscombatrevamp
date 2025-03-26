package net.igneo.icv.enchantmentActions;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public class PlayerEnchantmentActions {

    /*
            0=boots
            1=leggings
            2=chestplate
            3=helmet
            4=mainhand
            5=offhand
     */
    private EnchantmentManager[] managers = new EnchantmentManager[6];

    public EnchantmentManager getManager(int slot) {
        return managers[slot];
    }
    public EnchantmentManager[] getManagers() {
        return managers;
    }
    public boolean animated = false;

    public void setManager(EnchantmentManager manager, int slot) {
        if (manager == null || EnchantType.applicableSlot(manager.getType(),slot)) {
            if (this.managers[slot] != null) {
                this.managers[slot].onRemove();
            }
            this.managers[slot] = manager;
            if (manager != null) {
                manager.onEquip();
            }
            if (slot < 4 && FMLEnvironment.dist.isClient()) {
                if (manager instanceof ArmorEnchantManager aManager) {
                    indicators[slot] = aManager.getIndicator();
                } else {
                    indicators[slot] = null;
                }
            }
        } else {
            if (this.managers[slot] != null) {
                this.managers[slot].onRemove();
            }
            this.managers[slot] = null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public EnchantIndicator[] indicators = new EnchantIndicator[4];
    public void copyFrom(PlayerEnchantmentActions source) {
    }

    public void saveNBTData(CompoundTag nbt) {
    }

    public void loadNBTData(CompoundTag nbt) {
    }



}
