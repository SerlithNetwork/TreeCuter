package pl.norbit.treecuter.commands.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.norbit.treecuter.config.Settings;
import pl.norbit.treecuter.config.SettingsExtra;
import pl.norbit.treecuter.service.ToggleService;
import pl.norbit.treecuter.utils.ChatUtils;

public class TreeCuterCommand {

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("treecuter")
                .requires(s -> s.getSender().hasPermission("treecuter.help"))
                .then(Commands.literal("help")
                        .executes(context -> {
                            TreeCuterCommand.sendInfo(context.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("reload")
                        .requires(s -> s.getSender().hasPermission("treecuter.reload"))
                        .executes(context -> {
                            TreeCuterCommand.reload(context.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("get")
                        .requires(s -> s.getSender().hasPermission("treecuter.get"))
                        .then(Commands.argument("key", StringArgumentType.word())
                                .executes(context -> {
                                    String key = StringArgumentType.getString(context, "key");
                                    TreeCuterCommand.get(context.getSource().getSender(), key);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("toggle")
                        .requires(s -> s.getSender().hasPermission("treecuter.toggle"))
                        .executes(context -> {
                            TreeCuterCommand.toggle(context.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    private static void reload(CommandSender sender){
        sender.sendMessage(ChatUtils.format(Settings.getReloadStart()));
        Settings.loadConfig(true);
        SettingsExtra.INSTANCE.load();
        sender.sendMessage(ChatUtils.format(Settings.getReloadEnd()));
    }

    private static void get(CommandSender sender, String key){
        var p = getPlayer(sender);
        if(p == null){
            return;
        }
        Settings.getCustomToolForKey(key)
                .ifPresentOrElse(customTool -> {
                    p.getInventory().addItem(customTool);
                    p.sendMessage(ChatUtils.format(Settings.getToolGet()));
                }, () -> p.sendMessage(ChatUtils.format(Settings.getToolNotFound())));

    }

    private static void toggle(CommandSender sender){
        var p = getPlayer(sender);
        if(p == null){
            return;
        }
        boolean status = ToggleService.changeToggle(p.getUniqueId());
        String message = status ? Settings.getToggleMessageOn() : Settings.getToggleMessageOff();
        p.sendMessage(ChatUtils.format(message));
    }

    private static Player getPlayer(CommandSender sender){
        if((sender instanceof Player player)) {
            return player;
        }
        sender.sendMessage(ChatUtils.format(Settings.getConsoleMessage()));
        return null;
    }

    private static void sendInfo(CommandSender sender){
        if(!sender.hasPermission("treecuter.help")){
            sender.sendMessage(ChatUtils.format(Settings.getPermissionMessage()));
            return;
        }

        Settings.getHelpMessage()
                .stream()
                .map(ChatUtils::format)
                .forEach(sender::sendMessage);
    }

}
