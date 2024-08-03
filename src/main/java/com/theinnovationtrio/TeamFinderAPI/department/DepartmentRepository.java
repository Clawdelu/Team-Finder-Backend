package com.theinnovationtrio.TeamFinderAPI.department;

import com.theinnovationtrio.TeamFinderAPI.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    @Query("select d from Department d " +
            "join User u on d.createdBy = u.id " +
            "where u.organizationId = (select u1.organizationId from User u1 where u1.id = :userId)")
    List<Department> findAllSameOrgById(UUID userId);

    Department findByDepartmentManager(UUID departmentManager);

}
