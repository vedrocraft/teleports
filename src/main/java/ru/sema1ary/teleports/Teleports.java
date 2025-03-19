package ru.sema1ary.teleports;

import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sema1ary.teleports.command.TeleportCommand;
import ru.sema1ary.teleports.command.TeleportsCommand;
import ru.sema1ary.teleports.command.tpa.AcceptCommand;
import ru.sema1ary.teleports.command.tpa.CallCommand;
import ru.sema1ary.teleports.command.tpa.DenyCommand;
import ru.sema1ary.teleports.listener.PreJoinListener;
import ru.sema1ary.teleports.listener.QuitListener;
import ru.sema1ary.teleports.model.TeleportUser;
import ru.sema1ary.teleports.service.TeleportUserService;
import ru.sema1ary.teleports.service.impl.TeleportUserServiceImpl;
import ru.sema1ary.vedrocraftapi.BaseCommons;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.ormlite.DatabaseUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Teleports extends JavaPlugin implements BaseCommons {
    @Override
    public void onEnable() {
        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        DatabaseUtil.initConnectionSource(this, TeleportUser.class);

        ServiceManager.registerService(TeleportUserService.class, new TeleportUserServiceImpl(
                getDao(TeleportUser.class),
                getService(ConfigService.class)
        ));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                getService(TeleportUserService.class)
        ), this);
        getServer().getPluginManager().registerEvents(new QuitListener(
                getService(ConfigService.class),
                getService(TeleportUserService.class)
        ), this);

        LiteCommandBuilder.builder()
                .commands(
                        new TeleportCommand(
                                getService(ConfigService.class),
                                getService(TeleportUserService.class)
                        ),

                        new TeleportsCommand(
                                getService(ConfigService.class),
                                getService(TeleportUserService.class)
                        ),

                        new CallCommand(
                                getService(ConfigService.class),
                                getService(TeleportUserService.class)
                        ),

                        new AcceptCommand(
                                getService(TeleportUserService.class)
                        ),

                        new DenyCommand(
                                getService(TeleportUserService.class)
                        )
                )
                .build();
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);
    }
}
