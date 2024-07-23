package me.ryan.simplepluginhome.database.connection;

import java.sql.Connection;

public interface ConnectionFactory {
    
    Connection getConnection();

    void close();

}
