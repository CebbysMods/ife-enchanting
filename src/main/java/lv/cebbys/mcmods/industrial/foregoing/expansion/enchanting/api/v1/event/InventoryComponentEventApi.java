package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.constant.ItemChangeType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.ParametersAreNonnullByDefault;

public interface InventoryComponentEventApi extends SlotEventApi {
    InventoryComponent<?> getInventoryComponent();

    @ParametersAreNonnullByDefault
    static Event of(ItemChangeType type, InventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
        Event event;
        switch (type) {
            case REPLACED -> event = new InventoryComponentItemReplacedEvent(inventory, slot, before, after);
            case RESIZED -> event = new InventoryComponentItemResizedEvent(inventory, slot, before, after);
            case REMOVED -> event = new InventoryComponentItemRemovedEvent(inventory, slot, before);
            case ADDED -> event = new InventoryComponentItemAddedEvent(inventory, slot, after);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return event;
    }

    @ParametersAreNonnullByDefault
    static void post(ItemChangeType type, InventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
        NeoForge.EVENT_BUS.post(InventoryComponentEventApi.of(type, inventory, slot, before, after));
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class InventoryComponentItemReplacedEvent extends SlotItemReplacedEvent implements InventoryComponentEventApi {
        private final InventoryComponent<?> inventoryComponent;

        public InventoryComponentItemReplacedEvent(InventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
            super(slot, before, after);
            this.inventoryComponent = inventory;
        }
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class InventoryComponentItemResizedEvent extends SlotItemResizedEvent implements InventoryComponentEventApi {
        private final InventoryComponent<?> inventoryComponent;

        public InventoryComponentItemResizedEvent(InventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
            super(slot, before, after);
            this.inventoryComponent = inventory;
        }
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class InventoryComponentItemRemovedEvent extends SlotItemRemovedEvent implements InventoryComponentEventApi {
        private final InventoryComponent<?> inventoryComponent;

        public InventoryComponentItemRemovedEvent(InventoryComponent<?> inventory, Slot slot, ItemStack before) {
            super(slot, before);
            this.inventoryComponent = inventory;
        }
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class InventoryComponentItemAddedEvent extends SlotItemAddedEvent implements InventoryComponentEventApi {
        private final InventoryComponent<?> inventoryComponent;

        public InventoryComponentItemAddedEvent(InventoryComponent<?> inventory, Slot slot, ItemStack after) {
            super(slot, after);
            this.inventoryComponent = inventory;
        }
    }
}
