package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event;

import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.constant.ItemChangeType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.ParametersAreNonnullByDefault;

public interface SidedInventoryComponentEventApi extends InventoryComponentEventApi {
    SidedInventoryComponent<?> getSidedInventoryComponent();

    @ParametersAreNonnullByDefault
    static Event of(ItemChangeType type, SidedInventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
        Event event;
        switch (type) {
            case REPLACED -> event = new SidedInventoryComponentItemReplacedEvent(inventory, slot, before, after);
            case RESIZED -> event = new SidedInventoryComponentItemResizedEvent(inventory, slot, before, after);
            case REMOVED -> event = new SidedInventoryComponentItemRemovedEvent(inventory, slot, before);
            case ADDED -> event = new SidedInventoryComponentItemAddedEvent(inventory, slot, after);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return event;
    }

    @ParametersAreNonnullByDefault
    static void post(ItemChangeType type, SidedInventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
        NeoForge.EVENT_BUS.post(SidedInventoryComponentEventApi.of(type, inventory, slot, before, after));
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class SidedInventoryComponentItemReplacedEvent extends InventoryComponentItemReplacedEvent implements SidedInventoryComponentEventApi {
        private final SidedInventoryComponent<?> sidedInventoryComponent;

        public SidedInventoryComponentItemReplacedEvent(SidedInventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
            super(inventory, slot, before, after);
            this.sidedInventoryComponent = inventory;
        }
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class SidedInventoryComponentItemResizedEvent extends InventoryComponentItemResizedEvent implements SidedInventoryComponentEventApi {
        private final SidedInventoryComponent<?> sidedInventoryComponent;

        public SidedInventoryComponentItemResizedEvent(SidedInventoryComponent<?> inventory, Slot slot, ItemStack before, ItemStack after) {
            super(inventory, slot, before, after);
            this.sidedInventoryComponent = inventory;
        }
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class SidedInventoryComponentItemRemovedEvent extends InventoryComponentItemRemovedEvent implements SidedInventoryComponentEventApi {
        private final SidedInventoryComponent<?> sidedInventoryComponent;

        public SidedInventoryComponentItemRemovedEvent(SidedInventoryComponent<?> inventory, Slot slot, ItemStack before) {
            super(inventory, slot, before);
            this.sidedInventoryComponent = inventory;
        }
    }

    @ToString(callSuper = true)
    @Getter(AccessLevel.PUBLIC)
    class SidedInventoryComponentItemAddedEvent extends InventoryComponentItemAddedEvent implements SidedInventoryComponentEventApi {
        private final SidedInventoryComponent<?> sidedInventoryComponent;

        public SidedInventoryComponentItemAddedEvent(SidedInventoryComponent<?> inventory, Slot slot, ItemStack after) {
            super(inventory, slot, after);
            this.sidedInventoryComponent = inventory;
        }
    }
}
