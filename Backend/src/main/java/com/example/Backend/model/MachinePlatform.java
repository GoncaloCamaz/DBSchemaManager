
package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a relation between Machine and a Platform since it is N to N.
 * This Class contains Spring Boot Tags in order to specify the MachinePlatform Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "machineplatform")
public class MachinePlatform implements Serializable
{
    private static final long serialVersionUID = 523456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_machineplatform",updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="machine_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="platform_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Platform platform;

    public MachinePlatform() {
        this.machine = new Machine();
        this.platform = new Platform();
    }

    public MachinePlatform(MachinePlatform mp)
    {
        this.id = mp.getId();
        this.machine = mp.getMachine();
        this.platform = mp.getPlatform();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Override
    public MachinePlatform clone()
    {
        return new MachinePlatform(this);
    }
}