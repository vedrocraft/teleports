package ru.sema1ary.teleports.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.sema1ary.teleports.dao.TeleportUserDao;
import ru.sema1ary.teleports.model.TeleportUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class TeleportUserDaoImpl extends BaseDaoImpl<TeleportUser, Long> implements TeleportUserDao {
    public TeleportUserDaoImpl(ConnectionSource connectionSource, Class<TeleportUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public TeleportUser save(@NonNull TeleportUser user) throws SQLException {
        createOrUpdate(user);
        return user;
    }

    @Override
    public void saveAll(@NonNull List<TeleportUser> users) throws SQLException {
        callBatchTasks((Callable<Void>) () -> {
            for (TeleportUser user : users) {
                createOrUpdate(user);
            }
            return null;
        });
    }

    @Override
    public Optional<TeleportUser> findById(Long id) throws SQLException {
        TeleportUser result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<TeleportUser> findByUsername(@NonNull String username) throws SQLException {
        QueryBuilder<TeleportUser, Long> queryBuilder = queryBuilder();
        Where<TeleportUser, Long> where = queryBuilder.where();
        String columnName = "username";

        SelectArg selectArg = new SelectArg(SqlType.STRING, username.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<TeleportUser> findAll() throws SQLException {
        return queryForAll();
    }
}
