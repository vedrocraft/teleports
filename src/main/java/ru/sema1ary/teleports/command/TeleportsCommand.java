package ru.sema1ary.teleports.command;

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
@Command(name = "teleports")
public class TeleportsCommand {
    private final ConfigService configService;
    private final TeleportUserService userService;

    @Async
    @Execute(name = "reload")
    @Permission("teleports.reload")
    void reload(@Context CommandSender sender) {
        configService.reload();
        PlayerUtil.sendMessage(sender, configService.get("reload-message"));
    }

    @Async
    @Execute(name = "toggle")
    void toggle(@Context Player sender) {
        userService.toggle(sender);
    }
}
