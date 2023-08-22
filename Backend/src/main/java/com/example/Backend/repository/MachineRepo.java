package com.example.Backend.repository;

import com.example.Backend.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineRepo extends JpaRepository<Machine, Long>
{
    @Query("SELECT mch FROM Machine mch WHERE mch.name=:name")
    Machine findByName(@Param("name")String name);
}
