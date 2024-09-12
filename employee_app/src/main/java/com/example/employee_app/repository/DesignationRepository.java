package com.example.employee_app.repository;

import com.example.employee_app.model.DesignationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationDetails,Long> {
    //boolean existsByDepartmentDetails_Id(Long departmentId);
    boolean existsByDepartmentId(Long departmentId);
    List<DesignationDetails> findByDepartmentId(Long departmentId);
    Optional<DesignationDetails> findByDesignationName(String designationName );
}
