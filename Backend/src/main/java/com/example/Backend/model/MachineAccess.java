package com.example.Backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents access to a machine.
 * This Class contains Spring Boot Tags in order to specify the machineaccess Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "machineaccess")
public class MachineAccess implements Serializable {
    private static final long serialVersionUID = 555456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_machineaccess")
    private Long id;

    /**
     * Credentials to be stored
     */
    private String username;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="machine_id")
    private Machine machine;

    private String description;

    public MachineAccess() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
