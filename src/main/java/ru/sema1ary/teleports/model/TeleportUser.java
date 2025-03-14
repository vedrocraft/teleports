package ru.sema1ary.teleports.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import ru.sema1ary.teleports.dao.impl.TeleportUserDaoImpl;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "players", daoClass = TeleportUserDaoImpl.class)
public class TeleportUser {
    @DatabaseField(unique = true, generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String username;

    @DatabaseField(canBeNull = false, columnName = "is_teleports_enabled")
    private boolean isTeleportsEnabled;

    @DatabaseField(columnName = "request_player")
    private String requestPlayer;
}
