package com.example.employee_app.repository;

import com.example.employee_app.model.DesignationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationDetails,Long> {
    boolean existsByDepartmentDetails_Id(Long departmentId);


}
