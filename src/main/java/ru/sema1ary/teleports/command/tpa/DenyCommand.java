package ru.sema1ary.teleports.command.tpa;

import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import ru.sema1ary.teleports.service.TeleportUserService;

@RequiredArgsConstructor
@Command(name = "deny", aliases = {"tpdeny", "decline"})
public class DenyCommand {
    private final TeleportUserService userService;

    @Async
    @Execute
    void execute(@Context Player sender) {
        userService.denyRequest(sender);
    }
}
