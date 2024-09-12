package com.example.employee_app.repository;

import com.example.employee_app.model.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDetails,Long> {

    Optional<EmployeeDetails> findByEmail(String Email);
    //boolean existsByDesignationDetails_Designation_Id(int designationId);
    Optional<EmployeeDetails> findByEmpName(String empName);
    boolean existsByDesignationDetails_DesignationId(Long designationId);
    @Query(value = "SELECT * FROM employees e WHERE MONTH(e.dbo) = ?1 AND DAY(e.dbo) = ?2",nativeQuery = true)
    List<EmployeeDetails> findEmployeesWithBirthdayToday( int month, int day);
   }
