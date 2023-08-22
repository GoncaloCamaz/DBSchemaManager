package com.example.Backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Machine or server.
 * This Class contains Spring Boot Tags in order to specify the machine Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "machine")
public class Machine implements Serializable
{
    private static final long serialVersionUID = 423456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_machine",updatable = false, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String serviceip;
    private String managementip;
    private String operativesystem;
    private String machinetype;

    @OneToMany(mappedBy = "machine",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private Set<MachineAccess> machineAccessSet;

    @Column(columnDefinition = "text")
    private String observations;

    @Column(columnDefinition = "text")
    private String description;

    public Machine(Long machine_id, String machine_name, String serviceip, String managementip, String machinetype,
                   String machine_operativeSystem, String machine_observations, String description, Set<MachineAccess> machineAccessSet) {
        this.id = machine_id;
        this.name = machine_name;
        this.serviceip = serviceip;
        this.managementip = managementip;
        this.machinetype = machinetype;
        this.description = description;
        this.machineAccessSet = machineAccessSet;
        this.operativesystem = machine_operativeSystem;
        this.observations = machine_observations;
    }

    public Machine()
    {
        this.serviceip = "";
        this.managementip = "";
        this.operativesystem = "";
        this.observations = "";
        this.description = "";
        this.machinetype = "";
        this.name = "";
        this.machineAccessSet = new HashSet<>();
    }

    public Machine(Machine m)
    {
        this.id = m.getId();
        this.name = m.getName();
        this.serviceip = m.getServiceip();
        this.managementip = m.getManagementip();
        this.machinetype = m.getMachinetype();
        this.observations = m.getObservations();
        this.operativesystem = m.getOperativesystem();
        this.description = m.getDescription();
        this.machineAccessSet = m.getMachineAccessSet();
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

    public String getServiceip() {
        return serviceip;
    }

    public void setServiceip(String serviceip) {
        this.serviceip = serviceip;
    }

    public String getManagementip() {
        return managementip;
    }

    public void setManagementip(String managementip) {
        this.managementip = managementip;
    }

    public String getOperativesystem() {
        return operativesystem;
    }

    public void setOperativesystem(String operativeSystem) {
        this.operativesystem = operativeSystem;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MachineAccess> getMachineAccessSet() {
        return machineAccessSet;
    }

    public void setMachineAccessSet(Set<MachineAccess> machineAccessSet) {
        this.machineAccessSet = machineAccessSet;
    }

    @Override
    public Machine clone()
    {
        return new Machine(this);
    }
}
