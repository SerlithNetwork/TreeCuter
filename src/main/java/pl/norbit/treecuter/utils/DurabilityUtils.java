package pl.norbit.treecuter.utils;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.norbit.treecuter.config.Settings;
import pl.norbit.treecuter.config.SettingsExtra;

public class DurabilityUtils {

    public static final Enchantment ENCHANTMENT_LUMBERJACK;

    static {
        final Registry<@NotNull Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        ENCHANTMENT_LUMBERJACK = registry.getOrThrow(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK);
    }

    private DurabilityUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static int checkRemainingUses(ItemStack item){
        ItemMeta meta = item.getItemMeta();

        if(!SettingsExtra.GENERAL.USE_ENCHANTMENT && meta.isUnbreakable()){
            return Settings.getMaxBlocks();
        }

//        if(Settings.isItemsAdderEnabled()){
//            int uses = ItemsAdderUtils.checkRemainUses(item);
//
//            if(uses != -1){
//                return uses;
//            }
//        }

        if (meta instanceof Damageable damageable) {
            int maxPossible = item.getType().getMaxDurability() - damageable.getDamage();
            if (SettingsExtra.GENERAL.USE_ENCHANTMENT){
                int level = item.getEnchantmentLevel(ENCHANTMENT_LUMBERJACK) - 1;
                int maxEnchantment = SettingsExtra.ENCHANTMENT.BEHAVIOUR.BASE_BLOCKS_BREAK + (SettingsExtra.ENCHANTMENT.BEHAVIOUR.EXTRA_BLOCKS_BREAK_PER_LEVEL * level);
                return Math.min(maxPossible, maxEnchantment);
            }
            return maxPossible;
        }
        return 0;
    }

    public static ItemStack updateDurability(ItemStack item, int dmg){
        ItemMeta meta = item.getItemMeta();

        if(meta.isUnbreakable()){
            return item;
        }

//        if(Settings.isItemsAdderEnabled()){
//            ItemStack itemStack = ItemsAdderUtils.updateDurability(item, dmg);
//
//            if(itemStack != null){
//                return item;
//            }
//        }

        if (meta instanceof Damageable damageable){
            int maxDurability = item.getType().getMaxDurability();

            if(damageable.getDamage() + dmg >= maxDurability){
                return null;
            }
            damageable.setDamage((damageable.getDamage() + dmg));
        }

        item.setItemMeta(meta);
        return item;
    }
}
