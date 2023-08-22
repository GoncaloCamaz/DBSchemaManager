package com.example.Backend.repository;

import com.example.Backend.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlatformRepo extends JpaRepository<Platform, Long>
{
    @Query("SELECT plt FROM Platform plt WHERE plt.name=:platform_name")
    Platform findByName(@Param("platform_name") String platform_name);

    @Query("SELECT plt FROM Platform plt WHERE plt.url=:platform_url")
    Platform findByURL(@Param("platform_url") String url);
}
