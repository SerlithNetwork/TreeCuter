package pl.norbit.treecuter;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import pl.norbit.treecuter.config.SettingsExtra;
import pl.norbit.treecuter.utils.KeyUtils;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class TreeCuterBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        new SettingsExtra(context.getDataDirectory()).load();
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.compose().newHandler(event -> {
            event.registry().register(
                    EnchantmentKeys.create(KeyUtils.KEY_LUMBERJACK),
                    b -> b.description(SettingsExtra.ENCHANTMENT._DESCRIPTION)
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.AXES))
                            .anvilCost(SettingsExtra.ENCHANTMENT.ANVIL_COST)
                            .maxLevel(SettingsExtra.ENCHANTMENT.MAX_LEVEL)
                            .weight(SettingsExtra.ENCHANTMENT.WEIGHT)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(SettingsExtra.ENCHANTMENT.MINIMUM_COST.BASE_COST, SettingsExtra.ENCHANTMENT.MINIMUM_COST.ADDITIONAL_PER_LEVEL))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(SettingsExtra.ENCHANTMENT.MAXIMUM_COST.BASE_COST, SettingsExtra.ENCHANTMENT.MAXIMUM_COST.ADDITIONAL_PER_LEVEL))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );
        }));
    }

}
