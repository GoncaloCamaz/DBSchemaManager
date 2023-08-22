package com.example.Backend.repository;

import com.example.Backend.model.MachineAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachineAccessRepo extends JpaRepository<MachineAccess,Long> {

    @Query("SELECT macc FROM MachineAccess macc WHERE macc.machine.name=:machinename and macc.machine.serviceip=:serviceip")
    List<MachineAccess> findMachineAccessByMachineNameAndServiceIP(@Param("machinename")String machinename, @Param("serviceip") String serviceip);

    @Query("SELECT macc FROM MachineAccess macc WHERE macc.machine.name=:machinename and macc.username=:username")
    MachineAccess findMachineAccessByMachineNameAndByUsername(@Param("machinename")String machinename, @Param("username") String username);

    @Query("SELECT macc FROM MachineAccess macc WHERE macc.machine.name=:machinename")
    List<MachineAccess> findMachineAccessByMachineName(@Param("machinename") String name);
}
