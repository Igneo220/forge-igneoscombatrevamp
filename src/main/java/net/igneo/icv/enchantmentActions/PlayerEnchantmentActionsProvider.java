package net.igneo.icv.enchantmentActions;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEnchantmentActionsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerEnchantmentActions> PLAYER_ENCHANTMENT_ACTIONS = CapabilityManager.get(new CapabilityToken<PlayerEnchantmentActions>() {});

    private PlayerEnchantmentActions enchNBT = null;
    private final LazyOptional<PlayerEnchantmentActions> optional = LazyOptional.of(this::createEnchantmentActions);

    private PlayerEnchantmentActions createEnchantmentActions() {
        if (this.enchNBT == null) {
            this.enchNBT = new PlayerEnchantmentActions();
        }
        return this.enchNBT;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_ENCHANTMENT_ACTIONS) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createEnchantmentActions().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createEnchantmentActions().loadNBTData(nbt);
    }
}
