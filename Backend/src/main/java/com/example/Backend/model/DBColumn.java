package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Column of a database.
 * This Class contains Spring Boot Tags in order to specify the column Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "dbcolumn")
public class DBColumn implements Serializable
{
    private static final long serialVersionUID = 223456789L;

    /**
     * Long ID -> primary key identified by tag @Id
     * The strategy specifies the auto increment strategy that is set to auto. So, spring boot will determine the best
     * strategy for primary key for this entity and use it.
     * <p>
     * This column is called id_dbcolumn on the database and cannot be updated or be null.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_dbcolumn",updatable = false, nullable = false)
    private Long id;

    /**
     * String name -> Name of the column.
     * <p>
     * Cannot be null
     */
    @Column(nullable = false)
    private String name;

    /**
     * String datatype -> Data type of the column
     * <p>
     * Cannot be null
     */
    @Column(nullable = false)
    private String datatype;

    /**
     * DBObject -> Name of the Object that contains this column. It's identified by its ID.
     * <p>
     * ManyToOne tag specifies that Many Columns have only one DBObject. FetchType is set to lazy.
     * <p>
     * The tag JoinColumn specifies the foreign key and it cant be null. Note that the name "dbobject_id"
     * must be identified on DBObject with the same name
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dbobject_id", nullable = false)
    private DBObject dbobject;

    /**
     * Description of the column. This object on the database is a Text type.
     */
    @Column(columnDefinition = "text")
    private String description;

    private String nullable;

    public DBColumn(Long id, String name, String datatype, DBObject dbobject, String description, String nullable) {
        this.id = id;
        this.name = name;
        this.dbobject = dbobject;
        this.datatype = datatype;
        this.description = description;
        this.nullable = nullable;
    }

    public DBColumn() {
        this.name = "";
        this.dbobject = new DBObject();
        this.datatype = "";
        this.description = "";
        this.nullable = "";
    }

    public DBColumn(DBColumn c)
    {
        this.id = c.getId();
        this.name = c.getName();
        this.dbobject = c.getDbobject();
        this.datatype = c.getDatatype();
        this.description = c.getDescription();
        this.nullable = c.getNullable();
    }

    /**
     * This tag is added in order to stop possible nested object. If this tag is removed, there might occur infinite cycles
     * because when the DBColumn is fetched, since it has associated an DBObject, spring boot tries to laad DBObject as well
     * and then, since DBObject also has a Set of DBColumns it starts an infinite cycle.
     */
    @JsonBackReference
    public DBObject getDbobject() {
        return dbobject;
    }

    public void setDBObject(DBObject dbObject) {
        this.dbobject = dbObject;
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

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String dataType) {
        this.datatype = dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    @Override
    public DBColumn clone()
    {
        return new DBColumn(this);
    }
}
