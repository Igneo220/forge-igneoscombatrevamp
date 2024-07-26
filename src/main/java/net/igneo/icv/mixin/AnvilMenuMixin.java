package net.igneo.icv.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AnvilMenu.class,priority = 999999999)
public class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow private final DataSlot cost = DataSlot.standalone();
    public AnvilMenuMixin(@Nullable MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(pType, pContainerId, pPlayerInventory, pAccess);
    }

    @Inject(method = "createResult",at = @At("RETURN"))
    public void createResult(CallbackInfo ci) {
        if(this.inputSlots != null) {
            ItemStack checkItemStack = this.inputSlots.getItem(1);
            if ((checkItemStack != null && checkItemStack.isEnchanted()) || checkItemStack.getItem() == Items.ENCHANTED_BOOK) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
            } else {
                this.cost.set(2);
            }
        }
    }

    @Override @Shadow
    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return false;
    }

    @Override @Shadow
    protected void onTake(Player pPlayer, ItemStack pStack) {

    }

    @Override @Shadow
    protected boolean isValidBlock(BlockState pState) {
        return false;
    }

    @Override @Shadow
    public void createResult() {

    }

    @Override @Shadow
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return null;
    }
}
