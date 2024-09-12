package com.example.employee_app.repository;

import com.example.employee_app.model.DepartmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository   extends JpaRepository<DepartmentDetails,Long> {

    Optional<DepartmentDetails> findBydepartmentName(String deptName);


}
