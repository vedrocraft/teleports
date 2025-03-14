package ru.sema1ary.teleports.dao;

import ru.sema1ary.teleports.model.TeleportUser;
import ru.sema1ary.vedrocraftapi.ormlite.dao.UserDao;

public interface TeleportUserDao extends UserDao<TeleportUser, Long> {
}
