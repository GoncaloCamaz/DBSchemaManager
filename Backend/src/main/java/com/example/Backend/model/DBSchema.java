package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Schema of a database.
 * This Class contains Spring Boot Tags in order to specify the dbschema Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "dbschema")
public class DBSchema implements Serializable
{
    private static final long serialVersionUID = 823456789L;

    /**
     * Long id -> primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_dbschema", updatable = false, nullable = false)
    private Long id;

    /**
     * unique specifies that the schema name must be unique
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * unique specifies that the connectionstring must be unique
     */
    @Column(columnDefinition = "text",nullable = false, unique = true)
    private String connectionstring;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String sqlservername;

    @Column(nullable = false)
    private String updateperiod;

    private LocalDateTime lastupdate;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "dbschema",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private Set<DBObject> dbObjectSet;

    public DBSchema(Long id, String name, String description, String servername, String updatePeriod,
                    String conString, String user, String password, LocalDateTime update, Set<DBObject> dbObjectSet) {
        this.id = id;
        this.name = name;
        this.connectionstring = conString;
        this.updateperiod = updatePeriod;
        this.username = user;
        this.lastupdate = update;
        this.password = password;
        this.sqlservername = servername;
        this.description = description;
        this.dbObjectSet = dbObjectSet;
    }

    public DBSchema() {
        this.name = "";
        this.description = "";
        this.updateperiod = "";
        this.connectionstring = "";
        this.username = "";
        this.password = "";
        this.lastupdate = LocalDateTime.now();
        this.sqlservername = "";
        this.dbObjectSet = new HashSet<>();
    }

    public DBSchema(DBSchema sch)
    {
        this.id = sch.getId();
        this.name = sch.getName();
        this.connectionstring = sch.getConnectionstring();
        this.username = sch.getUsername();
        this.updateperiod = sch.getUpdateperiod();
        this.password = sch.getPassword();
        this.lastupdate = sch.getLastupdate();
        this.sqlservername = sch.getSqlservername();
        this.description = sch.getDescription();
        this.dbObjectSet = sch.getDbObjectSet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonManagedReference
    public Set<DBObject> getDbObjectSet() {
        return dbObjectSet;
    }

    public void setDbObjectSet(Set<DBObject> dbObjectSet) {
        this.dbObjectSet = dbObjectSet;
    }

    public String getSqlservername() {
        return sqlservername;
    }

    public void setSqlservername(String sqlservername) {
        this.sqlservername = sqlservername;
    }

    public String getConnectionstring() {
        return connectionstring;
    }

    public void setConnectionstring(String connectionString) {
        this.connectionstring = connectionString;
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

    public String getUpdateperiod() {
        return updateperiod;
    }

    public void setUpdateperiod(String updateperiod) {
        this.updateperiod = updateperiod;
    }

    public LocalDateTime getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(LocalDateTime lastupdate) {
        this.lastupdate = lastupdate;
    }

    @Override
    public DBSchema clone()
    {
        return new DBSchema(this);
    }
}

