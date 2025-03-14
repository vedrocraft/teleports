package ru.sema1ary.teleports.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.sema1ary.teleports.model.TeleportUser;
import ru.sema1ary.teleports.service.TeleportUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
public class QuitListener implements Listener {
    private final ConfigService configService;
    private final TeleportUserService userService;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TeleportUser user = userService.getUser(event.getPlayer().getName());

        if(user.getRequestPlayer() != null) {
            PlayerUtil.sendMessage(user.getRequestPlayer(),
                    configService.get("teleport-request-expired-message"));

            user.setRequestPlayer(null);
            userService.save(user);
        }
    }
}
