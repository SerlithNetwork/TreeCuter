package pl.norbit.treecuter.utils;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public class KeyUtils {

    public static final Key KEY_LUMBERJACK = Key.key("treecuter:lumberjack");
    public static final TypedKey<@NotNull Enchantment> ENCHANTMENT_KEY_LUMBERJACK = TypedKey.create(RegistryKey.ENCHANTMENT, KEY_LUMBERJACK);

}
