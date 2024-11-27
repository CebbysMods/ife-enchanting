package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.utility;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.InventoryComponentEventApi.InventoryComponentItemAddedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.InventoryComponentEventApi.InventoryComponentItemRemovedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.InventoryComponentEventApi.InventoryComponentItemReplacedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.InventoryComponentEventApi.InventoryComponentItemResizedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemAddedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemRemovedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemReplacedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemResizedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SlotEventApi.SlotItemAddedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SlotEventApi.SlotItemRemovedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SlotEventApi.SlotItemReplacedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SlotEventApi.SlotItemResizedEvent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public final class EventUtility {
    public static void postAdded(Slot slot, ItemStack added) {
        postChanged(slot,
                simple(slot, added, SidedInventoryComponentItemAddedEvent::new),
                simple(slot, added, InventoryComponentItemAddedEvent::new),
                simple(slot, added, SlotItemAddedEvent::new)
        );
    }

    public static void postRemoved(Slot slot, ItemStack before) {
        postChanged(slot,
                simple(slot, before, SidedInventoryComponentItemRemovedEvent::new),
                simple(slot, before, InventoryComponentItemRemovedEvent::new),
                simple(slot, before, SlotItemRemovedEvent::new)
        );
    }

    public static void postResized(Slot slot, ItemStack before, ItemStack after) {
        postChanged(slot,
                advanced(slot, before, after, SidedInventoryComponentItemResizedEvent::new),
                advanced(slot, before, after, InventoryComponentItemResizedEvent::new),
                advanced(slot, before, after, SlotItemResizedEvent::new)
        );
    }

    public static void postReplaced(Slot slot, ItemStack before, ItemStack after) {
        postChanged(slot,
                advanced(slot, before, after, SidedInventoryComponentItemReplacedEvent::new),
                advanced(slot, before, after, InventoryComponentItemReplacedEvent::new),
                advanced(slot, before, after, SlotItemReplacedEvent::new)
        );
    }

    private static void postChanged(
            Slot slot,
            InventoryEventFactory<SidedInventoryComponent<?>> sided,
            InventoryEventFactory<InventoryComponent<?>> basic,
            Supplier<Event> supplier
    ) {
        var event = withInventoryLookup(slot, sided::create, basic::create, supplier);
        log.debug("Posting event '{}' to event bus", event);
        NeoForge.EVENT_BUS.post(event);
    }

    private static Event withInventoryLookup(
            Slot slot,
            Function<SidedInventoryComponent<?>, Event> sidedConsumer,
            Function<InventoryComponent<?>, Event> invConsumer,
            Supplier<Event> supplier
    ) {
        if (slot instanceof SlotItemHandler sh) {
            if (sh.getItemHandler() instanceof SidedInventoryComponent<?> sided) {
                return sidedConsumer.apply(sided);
            } else if (sh.getItemHandler() instanceof InventoryComponent<?> inventory) {
                return invConsumer.apply(inventory);
            }
        }
        return supplier.get();
    }

    private interface AdvancedInventoryEventFactory<I extends InventoryComponent<?>> {
        Event create(I inventory, Slot slot, ItemStack before, ItemStack after);
    }

    private interface SimpleInventoryEventFactory<I extends InventoryComponent<?>> {
        Event create(I inventory, Slot slot, ItemStack stack);
    }

    private interface AdvancedSlotEventFactory {
        Event create(Slot slot, ItemStack before, ItemStack after);
    }

    private interface SimpleSlotEventFactory {
        Event create(Slot slot, ItemStack stack);
    }

    private interface InventoryEventFactory<I extends InventoryComponent<?>> {
        Event create(I inventory);
    }

    private static <I extends InventoryComponent<?>> InventoryEventFactory<I> advanced(
            Slot slot,
            ItemStack before,
            ItemStack after,
            AdvancedInventoryEventFactory<I> factory
    ) {
        return (var inventory) -> factory.create(inventory, slot, before, after);
    }

    private static <I extends InventoryComponent<?>> InventoryEventFactory<I> simple(
            Slot slot,
            ItemStack stack,
            SimpleInventoryEventFactory<I> factory
    ) {
        return (var inventory) -> factory.create(inventory, slot, stack);
    }

    private static Supplier<Event> advanced(
            Slot slot,
            ItemStack before,
            ItemStack after,
            AdvancedSlotEventFactory factory
    ) {
        return () -> factory.create(slot, before, after);
    }

    private static Supplier<Event> simple(
            Slot slot,
            ItemStack stack,
            SimpleSlotEventFactory factory
    ) {
        return () -> factory.create(slot, stack);
    }
}
