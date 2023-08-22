package com.example.Fetcher.Model;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Class that represents a schema
 */
public class Schema
{
    // name of the schema
    private String name;
    // connection string
    private String connectionstring;
    // user
    private String username;
    // password
    private String password;
    // used to send to frontend the notification that update was finished on a given schema
    private LocalDateTime lastUpdate;
    // Type of SQL server (MySQL, Oracle, SQL Server or PostgreSQL)
    private Enum server;
    // Associated objects
    private HashMap<String,DBObject> objects;

    public Schema() {
        this.name = "";
        this.connectionstring = "";
        this.username = "";
        this.lastUpdate = null;
        this.password = "";
        this.server = SQLServerType.MySQL;
        this.objects = new HashMap<>();
    }

    public Schema(String name, String connectionstring, String username, String password, String server) {
        this.name = name;
        this.connectionstring = connectionstring;
        this.username = username;
        this.lastUpdate = LocalDateTime.now();
        this.password = password;
        this.server = convertToEnum(server);
        this.objects = new HashMap<>();
    }

    public Schema(Schema s)
    {
        this.name = s.getName();
        this.connectionstring = s.getConnectionstring();
        this.username = s.getUsername();
        this.password = s.getPassword();
        this.server = s.getServer();
        this.objects = s.getObjects();
    }

    private Enum convertToEnum(String server)
    {
        String s = server.toLowerCase();
        switch (s){
            case "oracle":
                return SQLServerType.Oracle;
            case "sqlserver":
                return SQLServerType.SQLServer;
            case "postgresql":
                return SQLServerType.PostgreSQL;
            default:
                return SQLServerType.MySQL;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionstring() {
        return connectionstring;
    }

    public void setConnectionstring(String connectionstring) {
        this.connectionstring = connectionstring;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Enum getServer() {
        return server;
    }

    public void setServer(Enum server) {
        this.server = server;
    }

    public HashMap<String, DBObject> getObjects() {
        return objects;
    }

    public void setObjects( HashMap<String, DBObject> objects) {
        this.objects = objects;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public Schema clone()
    {
        return new Schema(this);
    }

    @Override
    public String toString() {
        return "Schema{" +
                "name='" + name + '\'' +
                ", connectionstring='" + connectionstring + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", server=" + server +
                ", objects=" + objects +
                '}' + '\n';
    }
}
