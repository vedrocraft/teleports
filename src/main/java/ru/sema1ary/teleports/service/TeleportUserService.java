package ru.sema1ary.teleports.service;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.sema1ary.teleports.model.TeleportUser;
import ru.sema1ary.vedrocraftapi.service.UserService;

public interface TeleportUserService extends UserService<TeleportUser> {
    void acceptRequest(@NonNull Player sender);

    void denyRequest(@NonNull Player sender);

    void sendRequest(@NonNull Player sender, @NonNull Player target);

    void toggle(@NonNull Player sender);
}
