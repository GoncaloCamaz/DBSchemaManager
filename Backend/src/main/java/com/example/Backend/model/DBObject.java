package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Object (TABLE/VIEW/PROCEDURE) of a database.
 * This Class contains Spring Boot Tags in order to specify the dbobject Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "dbobject")
public class DBObject implements Serializable
{
    private static final long serialVersionUID = 323456789L;

    /**
     * Long id -> Primary key of table DBObject.
     * Note that name inside @Column is the same as the foreign key on DBColumn. This names must match in order to
     * create relations between entities on the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_dbobject",updatable = false, nullable = false)
    private Long id;

    /**
     * Many DBObjects have only one dbschema
     */
    @ManyToOne
    @JoinColumn(name="dbschema_id", referencedColumnName = "id_dbschema", nullable = false)
    @JsonBackReference
    private DBSchema dbschema;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    /**
     * This attribute is used to define if a View or Procedure is deleted from the target database.
     * If status == DELETED that means that the object was deleted. This status is changed by fetcher since
     * fetcher cannot delete DBObjects that are views or procedures in order to keep history of view/procedures definition
     */
    private String status;

    @Column(columnDefinition = "text")
    private String description;

    /**
     * One DBObject has many dbcolumns.
     * JsonIdentityReference alwaysAsId is used to prevent infinite cycles. This tag makes spring boot load objects showing
     * only IDs on the nested objects. So, dbColumnSet would assume: [1,3,4,5,222,33] identifying ids of dbcolumns
     */
    @OneToMany(mappedBy = "dbobject",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private Set<DBColumn> dbColumnSet;

    @OneToMany(mappedBy = "dbobject",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private Set<DBScript> dbScriptSet;



    public DBObject(Long id, String name, String description, String type, String status,Set<DBColumn> dbColumnSet, Set<DBScript> dbScriptSet, DBSchema dbschema) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.dbColumnSet = dbColumnSet;
        this.dbScriptSet = dbScriptSet;
        this.dbschema = dbschema;
    }

    public DBObject() {
        this.dbschema = new DBSchema();
        this.name = "";
        this.type = "";
        this.status = "";
        this.description = "";
        this.dbColumnSet = new HashSet<>();
        this.dbScriptSet = new HashSet<>();
    }

    public DBObject(DBObject o)
    {
        this.id = o.getId();
        this.name = o.getName();
        this.description = o.getDescription();
        this.type = o.getType();
        this.status = o.getStatus();
        this.dbColumnSet = o.getDbColumnSet();
        this.dbScriptSet = o.getDbScriptSet();
        this.dbschema = o.getDbschema();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonBackReference
    public DBSchema getDbschema() {
        return dbschema;
    }

    public void setDbschema(DBSchema dbschema) {
        this.dbschema = dbschema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonManagedReference
    public Set<DBColumn> getDbColumnSet() {
        return dbColumnSet;
    }

    public void setDbColumnSet(Set<DBColumn> dbColumnSet) {
        this.dbColumnSet = dbColumnSet;
    }

    @JsonManagedReference
    public Set<DBScript> getDbScriptSet() {
        return dbScriptSet;
    }

    public void setDbScriptSet(Set<DBScript> dbScriptSet) {
        this.dbScriptSet = dbScriptSet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public DBObject clone()
    {
        return new DBObject(this);
    }
}
