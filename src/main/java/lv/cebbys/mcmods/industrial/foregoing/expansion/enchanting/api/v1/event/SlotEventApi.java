package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.constant.ItemChangeType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public interface SlotEventApi {
    static void post(ItemChangeType type, Slot slot, ItemStack before, ItemStack after) {

    }

    Slot getSlot();

    interface SlotItemChangeEventApi extends SlotEventApi {
        ItemChangeType getType();

        ItemStack getBefore();

        ItemStack getAfter();
    }

    @ToString
    @Getter(AccessLevel.PUBLIC)
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    class SlotItemReplacedEvent extends Event implements SlotItemChangeEventApi {
        private final ItemChangeType type = ItemChangeType.REPLACED;
        private final Slot slot;
        private final ItemStack before;
        private final ItemStack after;
    }

    @ToString
    @Getter(AccessLevel.PUBLIC)
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    class SlotItemResizedEvent extends Event implements SlotItemChangeEventApi {
        private final ItemChangeType type = ItemChangeType.RESIZED;
        private final Slot slot;
        private final ItemStack before;
        private final ItemStack after;
    }

    @ToString
    @Getter(AccessLevel.PUBLIC)
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    class SlotItemRemovedEvent extends Event implements SlotItemChangeEventApi {
        private final ItemChangeType type = ItemChangeType.REMOVED;
        private final Slot slot;
        private final ItemStack before;
        private final ItemStack after = ItemStack.EMPTY;
    }

    @ToString
    @Getter(AccessLevel.PUBLIC)
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    class SlotItemAddedEvent extends Event implements SlotItemChangeEventApi {
        private final ItemChangeType type = ItemChangeType.ADDED;
        private final Slot slot;
        private final ItemStack before = ItemStack.EMPTY;
        private final ItemStack after;
    }
}
