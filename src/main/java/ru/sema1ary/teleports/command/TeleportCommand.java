package ru.sema1ary.teleports.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sema1ary.teleports.service.TeleportUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
@Command(name = "teleport", aliases = {"tp"})
public class TeleportCommand {
    private final ConfigService configService;
    private final TeleportUserService userService;

    @Async
    @Execute(name = "toggle")
    void toggle(@Context Player sender) {
        userService.toggle(sender);
    }

    @Async
    @Execute
    @Permission("teleports.teleport")
    void execute(@Context Player sender, @Arg("игрок") Player target) {
        if(sender.equals(target)) {
            PlayerUtil.sendMessage(sender, configService.get("self-teleport-error"));
            return;
        }

        if(!userService.getUser(target.getName()).isTeleportsEnabled()
                && !sender.hasPermission("teleport.bypass")) {
            PlayerUtil.sendMessage(sender, configService.get("teleport-request-tp-disabled-message"));
            return;
        }

        sender.teleportAsync(target.getLocation());
        PlayerUtil.sendMessage(sender,
                ((String) configService.get("teleport-successful-message")).replace("{target}", target.getName())
        );
    }

    @Async
    @Execute
    @Permission("teleports.teleport.other")
    void execute(@Context CommandSender sender, @Arg("игрок_1") Player targetOne, @Arg("игрок_2") Player targetTwo) {
        targetOne.teleportAsync(targetTwo.getLocation());

        PlayerUtil.sendMessage(sender,
                ((String) configService.get("teleport-target-successful-message"))
                        .replace("{targetOne}", targetOne.getName())
                        .replace("{targetTwo}", targetTwo.getName())
        );
    }
}
