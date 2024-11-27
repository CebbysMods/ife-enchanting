package lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.content.event;

import lombok.extern.slf4j.Slf4j;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemAddedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemRemovedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemReplacedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.api.v1.event.SidedInventoryComponentEventApi.SidedInventoryComponentItemResizedEvent;
import lv.cebbys.mcmods.industrial.foregoing.expansion.enchanting.constant.IFEEnchantingConstants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@Slf4j
@EventBusSubscriber(modid = IFEEnchantingConstants.MODID, bus = EventBusSubscriber.Bus.GAME)
public class IFEEnchantingSidedInventoryEventHandler {
    @SubscribeEvent
    public static void handleAddedItem(SidedInventoryComponentItemAddedEvent event) {
        log.debug("handleAddedItem({})", event);
    }

    @SubscribeEvent
    public static void handleRemovedItem(SidedInventoryComponentItemRemovedEvent event) {
        log.debug("handleRemovedItem({})", event);
    }

    @SubscribeEvent
    public static void handleReplacedItem(SidedInventoryComponentItemReplacedEvent event) {
        log.debug("handleReplacedItem({})", event);
    }

    @SubscribeEvent
    public static void handleResizedItem(SidedInventoryComponentItemResizedEvent event) {
        log.debug("handleResizedItem({})", event);
    }
}
