//package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.mixin;
//
//import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
//import com.hrznstudio.titanium.component.IComponentHarness;
//import com.hrznstudio.titanium.component.inventory.InventoryComponent;
//import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
//import lombok.extern.slf4j.Slf4j;
//import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.InventoryComponentEvent;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.neoforge.common.NeoForge;
//import net.neoforged.neoforge.items.ItemStackHandler;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Slf4j
//@Mixin(InventoryComponent.class)
//public abstract class InventoryComponentMixin<T extends IComponentHarness> extends ItemStackHandler implements IScreenAddonProvider, IContainerAddonProvider {
//    @Inject(
//            method = "insertItem",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;")
//    )
//    private void onReplaceEmptyStack(
//            int slot,
//            ItemStack stack,
//            boolean simulate,
//            CallbackInfoReturnable<ItemStack> cir
//    ) {
//        var inventory = (InventoryComponent<? extends IComponentHarness>) (Object) this;
//        var limit = this.getStackLimit(slot, stack);
//        var exceeded = stack.getCount() > limit;
//        var event = new InventoryComponentEvent.ItemAdded(
//                inventory,
//                exceeded ? stack.copyWithCount(limit) : stack,
//                slot
//        );
//        log.debug("Posting item added event '{}'", event);
//        NeoForge.EVENT_BUS.post(event);
//    }
//
//    @Inject(
//            method = "insertItem",
//            at = @At(value = "RETURN", ordinal = 1)
//    )
//    private void onReplaceExistingStack(
//            int slot,
//            ItemStack stack,
//            boolean simulate,
//            CallbackInfoReturnable<ItemStack> cir
//    ) {
//        var inventory = (InventoryComponent<? extends IComponentHarness>) (Object) this;
//        var existing = inventory.getStackInSlot(slot);
//        var event = new InventoryComponentEvent.ItemUpdated(
//                inventory,
//                existing,
//                stack,
//                slot
//        );
//        log.debug("Posting existing item replaced event '{}'", event);
//        NeoForge.EVENT_BUS.post(event);
//    }
//
//    @Inject(
//            method = "insertItem",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V")
//    )
//    private void onUpdateExistingStack(
//            int slot,
//            ItemStack stack,
//            boolean simulate,
//            CallbackInfoReturnable<ItemStack> cir
//    ) {
//        var inventory = (InventoryComponent<? extends IComponentHarness>) (Object) this;
//        var limit = this.getStackLimit(slot, stack);
//        var before = inventory.getStackInSlot(slot);
//        var exceeded = stack.getCount() > limit;
//        var after = stack.copy();
//        after.grow(exceeded ? limit : stack.getCount());
//        var event = new InventoryComponentEvent.ItemUpdated(
//                inventory,
//                before,
//                after,
//                slot
//        );
//        log.debug("Posting item updated event '{}'", event);
//        NeoForge.EVENT_BUS.post(event);
//    }
//
////    @Inject(
////            method = "insertItem",
////            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;")
////    )
////    private void onRemoveStack(
////            int slot,
////            ItemStack stack,
////            boolean simulate,
////            CallbackInfoReturnable<ItemStack> cir
////    ) {
////        var inventory = (InventoryComponent<? extends IComponentHarness>) (Object) this;
////        var limit = this.getStackLimit(slot, stack);
////        var exceeded = stack.getCount() > limit;
////        var event = new InventoryComponentEvent.ItemAdded(
////                inventory,
////                exceeded ? stack.copyWithCount(limit) : stack,
////                slot
////        );
////        log.debug("Posting item removed event '{}'", event);
////        NeoForge.EVENT_BUS.post(event);
////    }
//}
