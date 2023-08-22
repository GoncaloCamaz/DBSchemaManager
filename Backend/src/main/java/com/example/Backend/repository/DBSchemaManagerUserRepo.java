package com.example.Backend.repository;

import com.example.Backend.model.DBSchemaManagerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DBSchemaManagerUserRepo extends JpaRepository<DBSchemaManagerUser, Long> {

    @Query("SELECT dbuser FROM DBSchemaManagerUser dbuser WHERE dbuser.username=:username")
    DBSchemaManagerUser findByUsername(@Param("username")String username);
}
