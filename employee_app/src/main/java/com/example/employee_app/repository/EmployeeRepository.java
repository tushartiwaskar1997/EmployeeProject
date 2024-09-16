package com.example.employee_app.repository;

import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.*;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDetails,Long> {

    Optional<EmployeeDetails> findByEmail(String Email);
    //boolean existsByDesignationDetails_Designation_Id(int designationId);
    Optional<EmployeeDetails> findByEmpName(String empName);
    boolean existsByDesignationDetails_DesignationId(Long designationId);

    //boolean existsByDepartmentDetailsDepartmentId(Long id);

    @Query(value = "select * from employees e where MONTH(e.dbo) = ?1 and DAY(e.dbo) = ?2",nativeQuery = true)
    List<EmployeeDetails> findEmployeesWithBirthdayToday( int month, int day);

    @Query(value="select * from employees where department_id = ?1 and is_active=true " ,nativeQuery = true)
    List<EmployeeDetails> CheckIfAnyEmployeeOfDepartmentIsActive(Long departmentid );

    @Query(value ="select * from employees where department_id =?1 ",nativeQuery = true)
    List<EmployeeDetails>  GetTheListOfEmployeeBYDepartmentId(Long id);
   }

