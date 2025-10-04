package pl.norbit.treecuter.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.GameMode;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import pl.norbit.treecuter.config.Settings;
import pl.norbit.treecuter.config.SettingsExtra;
import pl.norbit.treecuter.config.model.CutShape;
import pl.norbit.treecuter.service.TreeCutService;
import pl.norbit.treecuter.utils.KeyUtils;
import pl.norbit.treecuter.utils.PermissionsUtils;
import pl.norbit.treecuter.utils.WorldGuardUtils;

public class BlockInteractListener implements Listener {

    private final Enchantment enchantmentLumberjack;

    public BlockInteractListener() {
        final Registry<@NotNull Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        this.enchantmentLumberjack = registry.getOrThrow(KeyUtils.ENCHANTMENT_KEY_LUMBERJACK);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockInteract(PlayerInteractEvent e) {
        var b = e.getClickedBlock();
        var action = e.getAction();
        var p = e.getPlayer();

        if(b == null){
            return;
        }

        if(action != Action.LEFT_CLICK_BLOCK){
            return;
        }

        if(p.getGameMode() == GameMode.CREATIVE){
            return;
        }

        if (Settings.isWorldGuardEnabled() && (!WorldGuardUtils.canBreak(b.getLocation(), p))){
            return;
        }

        String worldName = p.getWorld().getName();

        if(Settings.isBlockedWorld(worldName)){
            return;
        }

        if(Settings.isUsePermissions() && (!PermissionsUtils.hasPermission(p, Settings.getPermission()))){
            return;
        }

        var item = p.getInventory().getItemInMainHand();

        CutShape shape = Settings.getCutShape(b, item);

        if (shape == null) {
            return;
        }

        if(Settings.isShiftMining() && (!p.isSneaking())){
            return;
        }

        if (SettingsExtra.GENERAL.USE_ENCHANTMENT && !item.containsEnchantment(this.enchantmentLumberjack)) {
            return;
        }

        TreeCutService.selectTreeByBlock(b, p, shape, item);
    }
}
