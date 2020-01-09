package com.devlucca.homes.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Storage
{
    private String user;
    private String password;
    private String host;
    private String database;
    private int port;
    private Connection c;
    private StorageType type;
    private Plugin plugin;
    
    public Storage(final Plugin plugin) {
        this.user = "";
        this.password = "";
        this.host = "";
        this.database = "";
        this.port = 3306;
        this.c = null;
        this.plugin = plugin;
    }
    
    public void initMySQL(final String user, final String password, final String host, final String database, final int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.database = database;
        this.port = port;
        this.type = StorageType.MySQL;
        try {
            this.debug("Conectando ao servidor MySQL...");
            this.debug("Host: " + host);
            this.c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
            this.debug("Conexao estabelecida com sucesso!");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void initMySQL(final String user, final String password, final String host, final String database) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.database = database;
        this.type = StorageType.MySQL;
        try {
            this.debug("Conectando ao servidor MySQL...");
            this.debug("Host: " + host);
            this.c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + this.port + "/" + database, user, password);
            this.debug("Conexao estabelecida com sucesso!");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void execute(final Query query) throws SQLException {
        if (query.isAsync()) {
            new BukkitRunnable() {
                public void run() {
                    try {
                        if (!query.getUpdates().isEmpty()) {
                            for (final String a : query.getUpdates()) {
                                final PreparedStatement pstmt = Storage.this.c.prepareStatement(a);
                                pstmt.executeUpdate();
                                pstmt.close();
                            }
                        }
                        if (!query.getNormal().isEmpty()) {
                            for (final String a : query.getNormal()) {
                                final PreparedStatement pstmt = Storage.this.c.prepareStatement(a);
                                pstmt.execute();
                                pstmt.close();
                            }
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(this.plugin);
        }
        else {
            if (!query.getUpdates().isEmpty()) {
                for (final String a : query.getUpdates()) {
                    final PreparedStatement pstmt = this.c.prepareStatement(a);
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
            if (!query.getNormal().isEmpty()) {
                for (final String a : query.getNormal()) {
                    final PreparedStatement pstmt = this.c.prepareStatement(a);
                    pstmt.execute();
                    pstmt.close();
                }
            }
        }
    }
    
    public void initSQLite(final String database) {
        this.database = database;
        this.type = StorageType.SQLite;
        final File path = new File("plugins/" + this.plugin.getName());
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:plugins/" + this.plugin.getName() + "/" + database);
            this.debug("SQLite inicializado com sucesso.");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e2) {
            e2.printStackTrace();
        }
    }
    
    private void debug(final String msg) {
        this.plugin.getLogger().log(Level.INFO, msg);
    }
    
    public Connection getConnection() throws SQLException {
        if (!this.c.isClosed() && this.c != null) {
            return this.c;
        }
        if (this.type == StorageType.SQLite) {
            try {
                Class.forName("org.sqlite.JDBC");
                this.c = DriverManager.getConnection("jdbc:sqlite:plugins/" + this.plugin.getName() + "/" + this.database);
                this.debug("SQLite conected.");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            this.debug("Conectando ao servidor MySQL...");
            this.debug("Host: " + this.host);
            this.c = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
            this.debug("Conexao estabelecida com sucesso!");
        }
        if (this.c.isClosed() || this.c == null) {
            return null;
        }
        return this.c;
    }
}
