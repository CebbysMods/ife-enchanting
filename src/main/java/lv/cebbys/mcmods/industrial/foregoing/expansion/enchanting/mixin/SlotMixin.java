package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.mixin;

import lombok.extern.slf4j.Slf4j;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.utility.EventUtility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@Slf4j
@Mixin(Slot.class)
@ParametersAreNonnullByDefault
public abstract class SlotMixin {
    @Unique
    @Nullable
    private ItemStack stack;

    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    public abstract int getMaxStackSize(ItemStack stack);

    @Inject(
            method = "tryRemove",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;remove(I)Lnet/minecraft/world/item/ItemStack;")
    )
    private void beforeTake(int count, int decrement, Player player, CallbackInfoReturnable<Optional<ItemStack>> cir) {
        stack = this.getItem().copy();
    }

    @Inject(
            method = "tryRemove",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;of(Ljava/lang/Object;)Ljava/util/Optional;")
    )
    private void afterTake(int count, int decrement, Player player, CallbackInfoReturnable<Optional<ItemStack>> cir) {
        var oldStack = this.stack;
        var newStack = this.getItem();
        if (oldStack != null && !ItemStack.EMPTY.equals(newStack)) {
            postResized(oldStack, newStack);
        }
        this.stack = null;
    }

    @Inject(
            method = "setByPlayer(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD")
    )
    private void onChange(ItemStack newStack, ItemStack oldStack, CallbackInfo ci) {
        if (ItemStack.EMPTY.equals(newStack)) {
            postRemoved(oldStack);
        } else if (ItemStack.EMPTY.equals(oldStack)) {
            postAdded(newStack);
        } else if (!oldStack.getItem().equals(newStack.getItem())) {
            postReplaced(oldStack, newStack);
        }
    }

    @Inject(
            method = "safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V")
    )
    private void onChange(ItemStack newStack, int increment, CallbackInfoReturnable<ItemStack> cir) {
        var oldStack = this.getItem();
        if (!ItemStack.EMPTY.equals(newStack) && !ItemStack.EMPTY.equals(oldStack)) {
            if (!newStack.getItem().equals(oldStack.getItem())) {
                postReplaced(oldStack, newStack);
            } else if (!newStack.equals(oldStack)) {
                int i = Math.min(Math.min(increment, newStack.getCount()), this.getMaxStackSize(newStack) - oldStack.getCount());
                newStack = oldStack.copy();
                newStack.grow(i);
                if (newStack.getCount() != oldStack.getCount()) {
                    postResized(oldStack, newStack);
                } else {
                    postReplaced(oldStack, newStack);
                }
            }
        }
    }

    @Unique
    private Slot asSlot() {
        return (Slot) (Object) this;
    }

    @Unique
    private void postAdded(ItemStack added) {
        EventUtility.postAdded(this.asSlot(), added);
    }

    @Unique
    private void postRemoved(ItemStack before) {
        EventUtility.postRemoved(this.asSlot(), before);
    }

    @Unique
    private void postResized(ItemStack before, ItemStack after) {
        EventUtility.postResized(this.asSlot(), before, after);
    }

    @Unique
    private void postReplaced(ItemStack before, ItemStack after) {
        EventUtility.postReplaced(this.asSlot(), before, after);
    }
}
