package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.DepartmentRequestDto;
import com.example.employee_app.dto.HandleRequest;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.DepartmentRepository;
import com.example.employee_app.repository.DesignationRepository;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<DepartmentDetails> getthedepartmentlist() {
        return departmentRepository.findAll();
    }

    public Optional<DepartmentDetails> getthedepartmentbyid(Long id) {
        return departmentRepository.findById(id);
    }

    public String deletethedepartmentbyid(Long id) {
        Optional<DepartmentDetails> departmentOptional = getthedepartmentbyid(id);
        if (departmentOptional.isPresent()) {
            DepartmentDetails departmentDetails = departmentOptional.get();
            if (departmentDetails.getTotalEmployee() == 0) {
                if (CheckIfANyDesiagnatioisStatustrue(id)) {
                    return MessageConfig.DEPARTMENT_CANNOT_BE_DELETED;
                }
                departmentDetails.setIsActive(false);
                departmentDetails.setUpdatedBy("Admin");
                departmentDetails.setUpdatedDate(LocalDateTime.now());
                departmentRepository.save(departmentDetails);
                return MessageConfig.DEPARTMENT_DELETED_SUCCESSFULLY;
            } else {
                return MessageConfig.DEPARTMENT_ASSOCIATED_WITH_EMPLOYEE;
            }
        }
        return MessageConfig.DEPARTMENT_NOT_FOUND;
    }

    public ResponseEntity<Object> AddTheDepartment(DepartmentRequestDto departmentdto) {
        DepartmentDetails dept = new DepartmentDetails();
        dept.setDepartmentName(departmentdto.getDepartmentName());
        dept.setIsActive(true);
        dept.setCreatedDate(LocalDateTime.now());
        dept.setCreatedBy("User");
        dept.setTotalEmployee(0L);
        return new ResponseEntity<>(departmentRepository.save(dept), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> UpdateTheDepartment(DepartmentRequestDto departmentRequestDto) {
        Optional<DepartmentDetails> deptoptional = getthedepartmentbyid((departmentRequestDto.getDepartmentId()));
        DepartmentDetails dept = deptoptional.get();
        if (!dept.getDepartmentName().equals(departmentRequestDto.getDepartmentName().toLowerCase())) {
            Optional<DepartmentDetails> checkifDepartmentpresent = chekifDepartmentispresent(departmentRequestDto.getDepartmentName().toLowerCase());
            if (checkifDepartmentpresent.isPresent()) {
                return new ResponseEntity<>(MessageConfig.DEPARTMENT_ALREADY_EXIST + "service", HttpStatus.NOT_ACCEPTABLE);
            } else {
                dept.setDepartmentName(departmentRequestDto.getDepartmentName().toLowerCase());
            }
        }
        dept.setIsActive(departmentRequestDto.getIsActive());
        dept.setUpdatedDate(LocalDateTime.now());
        dept.setUpdatedBy("Admin");
        return new ResponseEntity<>(departmentRepository.save(dept), HttpStatus.OK);
    }

    public DepartmentDetails savethedepartmentdetails(DepartmentDetails dept) {
        return departmentRepository.save(dept);
    }


    public Optional<DepartmentDetails> chekifDepartmentispresent(String depname) {
        return departmentRepository.findBydepartmentName(depname);
    }

    public Boolean CheckIfANyDesiagnatioisStatustrue(Long id) {
        List<EmployeeDetails> EmpListASPerDeptId = employeeRepository.GetTheListOfEmployeeBYDepartmentId(id);
        if (!EmpListASPerDeptId.isEmpty()) {
            List<DesignationDetails> DesignationList = new ArrayList<>();
            for (EmployeeDetails emp : EmpListASPerDeptId) {
                if (emp.getDesignationDetails().getIsActive()) {
                    return true;
                }
            }
        }

        //List<DesignationDetails> list = designationRepository.findByDepartmentId(id);
//        for (DesignationDetails desig : list) {
//            if (desig.getIsActive()) {
//                return true;
//            }
//        }
        return false;
    }
}
