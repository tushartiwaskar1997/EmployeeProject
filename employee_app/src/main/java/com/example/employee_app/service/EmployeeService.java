package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.EmployeeRequestDto;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DesignationService designationService;

    public List<EmployeeDetails> getTheListOfTheEmployees() {
        return employeeRepository.findAll();

    }

    public Optional<EmployeeDetails> GetTheEmployeeDetailsById(Long id) {
        return employeeRepository.findById(id);

    }

    public String  deleteTheEmployeeById(Long id) {
        Optional<EmployeeDetails> Employeeoptional = GetTheEmployeeDetailsById(id);
            EmployeeDetails Employee = Employeeoptional.get();

            DepartmentDetails departmentdetails = Employee.getDepartmentDetails();
            departmentdetails.setTotalEmployee(departmentdetails.getTotalEmployee() - 1L);
            departmentdetails.setUpdatedDate(LocalDateTime.now());
            departmentdetails.setUpdatedBy("Admin");
            departmentService.SaveTheDepartmentDetails(departmentdetails);

            DesignationDetails designationDetails = Employee.getDesignationDetails();
            designationDetails.setTotalEmployee(designationDetails.getTotalEmployee() - 1L);
            designationDetails.setUpdatedDate(LocalDateTime.now());
            designationDetails.setUpdatedBy("Admin");
            designationService.SaveTheDesignation(designationDetails);

            Employee.setIsActive(false);
            Employee.setUpdatedBy("Admin");
            Employee.setUpdatedDate(LocalDateTime.now());
            employeeRepository.save(Employee);
            return MessageConfig.EMPLOYEE_DELETED_SUCCESSFULLY;
    }

    public ResponseEntity<Object> AddTheEmployeeDetails(EmployeeRequestDto empdto, MultipartFile imagefile) throws IOException {
        EmployeeDetails emp = new EmployeeDetails();
        emp.setEmpName(empdto.getEmployeeName());
        emp.setEmail(empdto.getEmail());
        Optional<DesignationDetails> designationOptional = designationService.GetTheDesignationDetailsById((empdto.getDesignationId()));
        emp.setDesignationDetails(designationOptional.get());
        Optional<DepartmentDetails> deptOptional = departmentService.GetTheDepartmentById((empdto.getDepartmentId()));
        emp.setDepartmentDetails(deptOptional.get());
        emp.setCreatedBy("user");
        emp.setCreatedDate(LocalDateTime.now());
        emp.setDbo(empdto.getDbo());
        emp.setIsActive(true);
        emp.setImageData(imagefile.getBytes());
        UpdateTheTotalEmployeeCount_forDeptAndDesignation(emp.getDepartmentDetails().getId(), emp.getDesignationDetails().getId());
        return new ResponseEntity<>(employeeRepository.save(emp), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> UpdateTheEmployee(EmployeeRequestDto empdto, MultipartFile imagefile) throws IOException {
        Optional<EmployeeDetails> empOptional = GetTheEmployeeDetailsById((empdto.getEmployeeId()));
        EmployeeDetails emp_old_Object = empOptional.get();

        DesignationDetails emp_old_Desination = emp_old_Object.getDesignationDetails();
        DepartmentDetails emp_old_Department = emp_old_Object.getDepartmentDetails();

        Optional<DesignationDetails> designationOptionl_new = designationService.GetTheDesignationDetailsById((empdto.getDesignationId()));
        DesignationDetails emp_new_Desigantion = designationOptionl_new.get();

        if (emp_old_Desination.getId() != (empdto.getDesignationId())) {
            emp_old_Desination.setTotalEmployee(emp_old_Desination.getTotalEmployee() - 1l);
            emp_old_Desination.setUpdatedBy("Admin");
            emp_old_Desination.setUpdatedDate(LocalDateTime.now());
            designationService.SaveTheDesignation(emp_old_Desination);

            emp_new_Desigantion.setTotalEmployee(emp_new_Desigantion.getTotalEmployee() + 1L);
            emp_new_Desigantion.setUpdatedBy("Admin");
            emp_new_Desigantion.setUpdatedDate(LocalDateTime.now());
            designationService.SaveTheDesignation(emp_new_Desigantion);
        }
        Optional<DepartmentDetails> departmentOptional_new = departmentService.GetTheDepartmentById((empdto.getDepartmentId()));

        DepartmentDetails emp_new_Department = departmentOptional_new.get();

        if (emp_old_Department.getId() != (empdto.getDepartmentId())) {
            emp_old_Department.setTotalEmployee(emp_old_Department.getTotalEmployee() - 1L);
            emp_old_Department.setUpdatedBy("Admin");
            emp_old_Department.setUpdatedDate(LocalDateTime.now());
            departmentService.SaveTheDepartmentDetails(emp_old_Department);

            emp_new_Department.setTotalEmployee(emp_new_Department.getTotalEmployee() + 1L);
            emp_new_Department.setUpdatedBy("Admin");
            emp_new_Department.setUpdatedDate(LocalDateTime.now());
            departmentService.SaveTheDepartmentDetails(emp_new_Department);
        }
        emp_old_Object.setEmpName(empdto.getEmployeeName());

        emp_old_Object.setEmail(empdto.getEmail());
        emp_old_Object.setDesignationDetails(designationOptionl_new.get());
        emp_old_Object.setDepartmentDetails(departmentOptional_new.get());
        emp_old_Object.setDbo(empdto.getDbo());
        emp_old_Object.setUpdatedBy("Admin");
        emp_old_Object.setUpdatedDate(LocalDateTime.now());
        emp_old_Object.setImageData(imagefile.getBytes());

        return new ResponseEntity<>(employeeRepository.save(emp_old_Object), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> ChangeEmployeeStatusToTrue(Long id) {

        EmployeeDetails empObj = GetTheEmployeeDetailsById(id).get();


        if (!empObj.getDesignationDetails().getIsActive()) {
            DepartmentDetails dept = empObj.getDepartmentDetails();
            dept.setIsActive(true);
            dept.setUpdatedBy("SuperUser");
            dept.setUpdatedDate(LocalDateTime.now());
            departmentService.SaveTheDepartmentDetails(dept);
        }

        if (!empObj.getDesignationDetails().getIsActive()) {
            DesignationDetails desig = empObj.getDesignationDetails();
            desig.setIsActive(true);
            desig.setUpdatedBy("SuperUser");
            desig.setUpdatedDate(LocalDateTime.now());
            designationService.SaveTheDesignation(desig);
        }

        UpdateTheTotalEmployeeCount_forDeptAndDesignation(empObj.getDepartmentDetails().getId(), empObj.getDesignationDetails().getId());
        empObj.setIsActive(true);
        empObj.setUpdatedBy("Super_user");
        empObj.setUpdatedDate(LocalDateTime.now());
        return new ResponseEntity<>(employeeRepository.save(empObj), HttpStatus.OK);
    }

    public Optional<EmployeeDetails> FindTheEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }


    public List<EmployeeDetails> FindTheListOfEmployeesAsPerTheMonthAndDate(int month, int day) {
        return employeeRepository.findEmployeesWithBirthdayToday(month, day);
    }

    public Boolean CheckIfEmployeeOfSameNamePresentORNot(String employeename) {
        if (employeeRepository.findByEmpName(employeename).isPresent()) {
            return true;
        } else
            return false;
    }

    public void UpdateTheTotalEmployeeCount_forDeptAndDesignation(Long Deptid, Long DesigId) {
        DepartmentDetails departmentDetails = departmentService.GetTheDepartmentById(Deptid).get();
        departmentDetails.setTotalEmployee(departmentDetails.getTotalEmployee() + 1L);
        departmentDetails.setUpdatedDate(LocalDateTime.now());
        departmentDetails.setUpdatedBy("Admin");
        if (!departmentDetails.getIsActive()) {
            departmentDetails.setIsActive(true);
        }
        departmentService.SaveTheDepartmentDetails(departmentDetails);

        DesignationDetails designationDetails = designationService.GetTheDesignationDetailsById(DesigId).get();
        designationDetails.setTotalEmployee(designationDetails.getTotalEmployee() + 1L);
        designationDetails.setUpdatedDate(LocalDateTime.now());
        designationDetails.setUpdatedBy("Admin");
        if (!designationDetails.getIsActive()) {
            designationDetails.setIsActive(true);
        }
        designationService.SaveTheDesignation(designationDetails);
    }
}

