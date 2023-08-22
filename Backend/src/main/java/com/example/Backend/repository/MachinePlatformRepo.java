package com.example.Backend.repository;

import com.example.Backend.model.MachinePlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachinePlatformRepo extends JpaRepository<MachinePlatform, Long>
{
    @Query("SELECT mp FROM MachinePlatform mp WHERE mp.machine.name=:machinename and mp.platform.name=:platformname")
    MachinePlatform findByMachineAndPlatform(@Param("machinename")String machinename, @Param("platformname") String platformname);

    @Query("SELECT mp FROM MachinePlatform mp WHERE mp.machine.name=:machinename")
    List<MachinePlatform> findAllByMachineName(@Param("machinename")String machinename);

    @Query("SELECT mp FROM MachinePlatform mp WHERE mp.platform.name=:platformname")
    List<MachinePlatform> findAllByPlatformName(@Param("platformname") String name);
}
