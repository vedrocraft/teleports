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
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Teleports extends JavaPlugin implements BaseCommons {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        initConnectionSource();

        ServiceManager.registerService(TeleportUserService.class, new TeleportUserServiceImpl(
                getDao(TeleportUser.class),
                ServiceManager.getService(ConfigService.class)
        ));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                ServiceManager.getService(TeleportUserService.class)), this);
        getServer().getPluginManager().registerEvents(new QuitListener(ServiceManager.getService(ConfigService.class),
                ServiceManager.getService(TeleportUserService.class)), this);

        LiteCommandBuilder.builder()
                .commands(
                        new TeleportCommand(
                                ServiceManager.getService(ConfigService.class),
                                ServiceManager.getService(TeleportUserService.class)
                        ),

                        new TeleportsCommand(
                                ServiceManager.getService(ConfigService.class),
                                ServiceManager.getService(TeleportUserService.class)
                        ),

                        new CallCommand(
                                ServiceManager.getService(ConfigService.class),
                                ServiceManager.getService(TeleportUserService.class)
                        ),

                        new AcceptCommand(
                                ServiceManager.getService(TeleportUserService.class)
                        ),

                        new DenyCommand(
                                ServiceManager.getService(TeleportUserService.class)
                        )
                )
                .build();
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);
    }

    @SneakyThrows
    private void initConnectionSource() {
        Path databaseFilePath = Paths.get("plugins/teleports/database.sqlite");
        if(!Files.exists(databaseFilePath) && !databaseFilePath.toFile().createNewFile()) {
            return;
        }

        ConnectionSourceUtil.connectNoSQLDatabase(databaseFilePath.toString(),
                TeleportUser.class);
    }
}
