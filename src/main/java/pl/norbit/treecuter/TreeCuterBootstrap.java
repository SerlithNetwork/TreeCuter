package pl.norbit.treecuter;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import pl.norbit.treecuter.commands.brigadier.TreeCuterCommand;
import pl.norbit.treecuter.config.SettingsExtra;
import pl.norbit.treecuter.utils.KeyUtils;

import java.util.List;

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
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT), e -> {
            var registrar = e.registrar();
            if (SettingsExtra.ENCHANTMENT.LOOT_POOLS.TREASURE) {
                registrar.addToTag(EnchantmentTagKeys.TREASURE, List.of(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK));
            }
            if (SettingsExtra.ENCHANTMENT.LOOT_POOLS.NON_TREASURE) {
                registrar.addToTag(EnchantmentTagKeys.NON_TREASURE, List.of(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK));
            }
            if (SettingsExtra.ENCHANTMENT.LOOT_POOLS.TRADEABLE) {
                registrar.addToTag(EnchantmentTagKeys.TRADEABLE, List.of(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK));
            }
            if (SettingsExtra.ENCHANTMENT.LOOT_POOLS.ENCHANTING_TABLE) {
                registrar.addToTag(EnchantmentTagKeys.IN_ENCHANTING_TABLE, List.of(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK));
            }
            if (SettingsExtra.ENCHANTMENT.LOOT_POOLS.RANDOM_LOOT) {
                registrar.addToTag(EnchantmentTagKeys.ON_RANDOM_LOOT, List.of(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK));
            }
            if (SettingsExtra.ENCHANTMENT.LOOT_POOLS.MOB_EQUIPMENT) {
                registrar.addToTag(EnchantmentTagKeys.ON_MOB_SPAWN_EQUIPMENT, List.of(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK));
            }
        });
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(TreeCuterCommand.build(), "Main command for TreeCuter");
        });
    }

}
