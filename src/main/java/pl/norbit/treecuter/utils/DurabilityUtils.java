package pl.norbit.treecuter.utils;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.bukkit.enchantments.Enchantment;
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

        if (meta instanceof Damageable damageable) {
            int maxDurability = item.getType().getMaxDurability();
            int currentDamage = damageable.getDamage();
            int remainingDurability = maxDurability - currentDamage;

            //Check for durability enchantment
            int afterUnbreakingDurability = remainingDurability;
            int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
            if (unbreakingLevel > 0) {
                afterUnbreakingDurability = remainingDurability * (unbreakingLevel + 1);
            }

            int afterLumberjackDurability = afterUnbreakingDurability;
            if (SettingsExtra.GENERAL.USE_ENCHANTMENT) {
                int level = item.getEnchantmentLevel(ENCHANTMENT_LUMBERJACK) - 1;
                int maxEnchantment = SettingsExtra.ENCHANTMENT.BEHAVIOUR.BASE_BLOCKS_BREAK + (SettingsExtra.ENCHANTMENT.BEHAVIOUR.EXTRA_BLOCKS_BREAK_PER_LEVEL * level);
                afterLumberjackDurability = Math.min(afterUnbreakingDurability, maxEnchantment);
            }

            return afterLumberjackDurability;
        }
        return 0;
    }

    public static ItemStack updateDurability(ItemStack item, int dmg){
        ItemMeta meta = item.getItemMeta();

        if(meta.isUnbreakable()){
            return item;
        }

        if (meta instanceof Damageable damageable){
            int maxDurability = item.getType().getMaxDurability();

            //Check for durability enchantment
            int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
            int actualDamage = dmg;
            if (unbreakingLevel > 0) actualDamage = dmg / (unbreakingLevel + 1);

            if(damageable.getDamage() + actualDamage >= maxDurability){
                return null;
            }
            damageable.setDamage((damageable.getDamage() + actualDamage));
        }
        item.setItemMeta(meta);
        return item;
    }
}
