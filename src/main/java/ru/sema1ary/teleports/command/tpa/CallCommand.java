package ru.sema1ary.teleports.command.tpa;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import ru.sema1ary.teleports.service.TeleportUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
@Command(name = "call", aliases = {"tpa"})
public class CallCommand {
    private final ConfigService configService;
    private final TeleportUserService userService;

    @Async
    @Execute
    void execute(@Context Player sender, @Arg("игрок") Player target) {
        if(sender.equals(target)) {
            PlayerUtil.sendMessage(sender, (String) configService.get("self-teleport-error"));
            return;
        }

        userService.sendRequest(sender, target);
    }
}
