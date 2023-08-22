package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a Script of an VIEW/Procedure.
 * This Class contains Spring Boot Tags in order to specify the dbscript Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "dbscript")
public class DBScript implements Serializable
{
    private static final long serialVersionUID = 723456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_dbscript",updatable = false, nullable = false)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime date;

    private String definer;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dbobject_id")
    private DBObject dbobject;

    public DBScript(Long id, String code, DBObject dbobject, String definer,LocalDateTime date, String description) {
        this.id = id;
        this.dbobject = dbobject;
        this.code = code;
        this.definer = definer;
        this.date = date;
        this.description = description;
    }

    public DBScript() {
        this.code = "";
        this.dbobject = new DBObject();
        this.date = LocalDateTime.now();
        this.description = "";
        this.definer = "";
    }

    public DBScript(DBScript scr)
    {
        this.id = scr.getId();
        this.code = scr.getCode();
        this.date = scr.getDate();
        this.definer = scr.getDefiner();
        this.description = scr.getDescription();
        this.dbobject = scr.getDbobject();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonBackReference
    public DBObject getDbobject() {
        return dbobject;
    }

    public void setDbobject(DBObject dbObject) {
        this.dbobject = dbObject;
    }


    public String getDefiner() {
        return definer;
    }

    public void setDefiner(String definer) {
        this.definer = definer;
    }

    @Override
    public DBScript clone()
    {
        return new DBScript(this);
    }
}
