package ru.sema1ary.teleports.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import ru.sema1ary.teleports.model.TeleportUser;
import ru.sema1ary.teleports.service.TeleportUserService;

@RequiredArgsConstructor
public class PreJoinListener implements Listener {
    private final TeleportUserService userService;

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        String username = event.getName();

        if(username.isEmpty()) {
            return;
        }

        if(userService.findByUsername(username).isEmpty()) {
            userService.save(TeleportUser.builder()
                    .username(username)
                    .isTeleportsEnabled(true)
                    .build());
        }
    }
}
