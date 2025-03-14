package ru.sema1ary.teleports.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.sema1ary.teleports.dao.TeleportUserDao;
import ru.sema1ary.teleports.model.TeleportUser;
import ru.sema1ary.teleports.service.TeleportUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TeleportUserServiceImpl implements TeleportUserService {
    private final TeleportUserDao userDao;
    private final ConfigService configService;

    @Override
    public void disable() {
        findAll().forEach(user -> {
            user.setRequestPlayer(null);
            save(user);
        });
    }

    @Override
    public TeleportUser save(@NonNull TeleportUser teleportUser) {
        try {
            return userDao.save(teleportUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(@NonNull List<TeleportUser> list) {
        try {
            userDao.saveAll(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<TeleportUser> findById(Long aLong) {
        try {
            return userDao.findById(aLong);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<TeleportUser> findByUsername(@NonNull String s) {
        try {
            return userDao.findByUsername(s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TeleportUser> findAll() {
        try {
            return userDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TeleportUser getUser(@NonNull String s) {
        return findByUsername(s).orElseGet(() -> save(TeleportUser.builder()
                .username(s)
                .isTeleportsEnabled(true)
                .build()));
    }

    @Override
    public void acceptRequest(@NonNull Player sender) {
        TeleportUser user = getUser(sender.getName());

        if(user.getRequestPlayer() == null) {
            PlayerUtil.sendMessage(sender, configService.get("no-requests-error-message"));
            return;
        }

        Player player = Bukkit.getPlayer(user.getRequestPlayer());
        if(player == null || !player.isOnline()) {
            PlayerUtil.sendMessage(sender, configService.get("teleport-request-expired-message"));
            return;
        }

        user.setRequestPlayer(null);
        save(user);

        player.teleportAsync(sender.getLocation());
        PlayerUtil.sendMessage(sender, configService.get("teleport-accept-message"));
        PlayerUtil.sendMessage(player, configService.get("teleport-accept-request-player-message"));

        if(configService.get("should-play-sound-on-teleport-accept")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1L, 0L);
        }
    }

    @Override
    public void denyRequest(@NonNull Player sender) {
        TeleportUser user = getUser(sender.getName());

        if(user.getRequestPlayer() == null) {
            PlayerUtil.sendMessage(sender, configService.get("no-requests-error-message"));
            return;
        }

        Player player = Bukkit.getPlayer(user.getRequestPlayer());
        PlayerUtil.sendMessage(sender, configService.get("teleport-deny-message"));
        PlayerUtil.sendMessage(player, configService.get("teleport-deny-request-player-message"));

        if(player != null && (boolean) configService.get("should-play-sound-on-teleport-deny")) {
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_DEATH, 1L, 0L);
        }

        user.setRequestPlayer(null);
        save(user);
    }

    @Override
    public void sendRequest(@NonNull Player sender, @NonNull Player target) {
        TeleportUser user = getUser(target.getName());

        if(!user.isTeleportsEnabled() && !sender.hasPermission("teleport.bypass")) {
            PlayerUtil.sendMessage(sender, configService.get("teleport-request-tp-disabled-message"));
            return;
        }

        user.setRequestPlayer(sender.getName());
        save(user);

        PlayerUtil.sendMessage(sender,
                ((String) configService.get("teleport-request-send-sender-message"))
                        .replace("{target}", target.getName())
        );

        PlayerUtil.sendMessage(target,
                ((String) configService.get("teleport-request-send-target-message"))
                        .replace("{sender}", sender.getName())
        );

        if(configService.get("should-play-sound-on-request")) {
            target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1L, 0L);
        }
    }

    @Override
    public void toggle(@NonNull Player sender) {
        TeleportUser user = getUser(sender.getName());

        if(user.isTeleportsEnabled()) {
            user.setTeleportsEnabled(false);
            PlayerUtil.sendMessage(sender, configService.get("teleport-toggle-disabled-message"));
        } else {
            PlayerUtil.sendMessage(sender, configService.get("teleport-toggle-enabled-message"));
            user.setTeleportsEnabled(true);
        }

        save(user);
    }
}
