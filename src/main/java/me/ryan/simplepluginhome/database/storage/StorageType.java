package me.ryan.simplepluginhome.database.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum StorageType {

    MYSQL("MYSQL", "com.mysql.cj.jdbc.Driver", "MySQL.sql"),
    MARIADB("MARIADB", "org.mariadb.jdbc.Driver", "MySQL.sql"),
    SQLITE("SQLITE", "org.sqlite.JDBC", "sqlite.sql");

    private final String name;
    private final String driver;
    private final String schema;

    public static StorageType of(String name) {
        return Arrays.stream(values())
                .filter(storageType -> storageType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(SQLITE);
    }

}
