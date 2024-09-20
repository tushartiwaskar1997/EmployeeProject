package com.example.employee_app.service;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.dto.EmployeeRequestDto;
import com.example.employee_app.email.EmployeeRelatedEmail;
import com.example.employee_app.model.DepartmentDetails;
import com.example.employee_app.model.DesignationDetails;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
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
    @Autowired
    private EmployeeRelatedEmail employeeRelatedEmail;


    public List<EmployeeDetails> getTheListOfTheEmployees() {
        return employeeRepository.findAll();

    }

    public Optional<EmployeeDetails> GetTheEmployeeDetailsById(Long id) {
        return employeeRepository.findById(id);

    }

    public String deleteTheEmployeeById(Long id) throws MessagingException {
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
        EmployeeDetails response = employeeRepository.save(Employee);
        employeeRelatedEmail.SendTheDeleteEmployeeEmail(response);
        return MessageConfig.SUCCESS_EMPLOYEE_DELETED;
    }

    public EmployeeDetails AddTheEmployeeDetails(EmployeeRequestDto empdto, MultipartFile imagefile) throws IOException, MessagingException {
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
        EmployeeDetails response = employeeRepository.save(emp);
        employeeRelatedEmail.SendTheEmployeeAdditionMAIL(response);
        return response;
    }

    public EmployeeDetails UpdateTheEmployee(EmployeeRequestDto empdto, MultipartFile imagefile) throws IOException {
        Optional<EmployeeDetails> empOptional = GetTheEmployeeDetailsById((empdto.getEmployeeId()));
        EmployeeDetails emp_previous_Object = empOptional.get();
        DesignationDetails emp_previous_Desination = emp_previous_Object.getDesignationDetails();
        DepartmentDetails emp_previous_Department = emp_previous_Object.getDepartmentDetails();
        Optional<DesignationDetails> designationOptionl_new = designationService.GetTheDesignationDetailsById((empdto.getDesignationId()));
        DesignationDetails emp_new_Desigantion = designationOptionl_new.get();
        Optional<DepartmentDetails> departmentOptional_new = departmentService.GetTheDepartmentById((empdto.getDepartmentId()));
        DepartmentDetails emp_new_Department = departmentOptional_new.get();
        if (emp_previous_Object.getIsActive()) {
            if (emp_previous_Desination.getId() != (empdto.getDesignationId())) {
                emp_previous_Desination.setTotalEmployee(emp_previous_Desination.getTotalEmployee() - 1l);
                emp_previous_Desination.setUpdatedBy("Admin");
                emp_previous_Desination.setUpdatedDate(LocalDateTime.now());
                designationService.SaveTheDesignation(emp_previous_Desination);
                emp_new_Desigantion.setTotalEmployee(emp_new_Desigantion.getTotalEmployee() + 1L);
                emp_new_Desigantion.setUpdatedBy("Admin");
                emp_new_Desigantion.setUpdatedDate(LocalDateTime.now());
                designationService.SaveTheDesignation(emp_new_Desigantion);
            }
            if (emp_previous_Department.getId() != (empdto.getDepartmentId())) {
                emp_previous_Department.setTotalEmployee(emp_previous_Department.getTotalEmployee() - 1L);
                emp_previous_Department.setUpdatedBy("Admin");
                emp_previous_Department.setUpdatedDate(LocalDateTime.now());
                departmentService.SaveTheDepartmentDetails(emp_previous_Department);
                emp_new_Department.setTotalEmployee(emp_new_Department.getTotalEmployee() + 1L);
                emp_new_Department.setUpdatedBy("Admin");
                emp_new_Department.setUpdatedDate(LocalDateTime.now());
                departmentService.SaveTheDepartmentDetails(emp_new_Department);
            }
        } else {
            if (emp_previous_Desination.getId() != (empdto.getDesignationId())) {
                emp_previous_Object.setDesignationDetails(designationService.GetTheDesignationDetailsById(empdto.getDesignationId()).get());
            }
            if (emp_previous_Department.getId() != (empdto.getDepartmentId())) {
                emp_previous_Object.setDepartmentDetails((departmentService.GetTheDepartmentById(empdto.getDepartmentId()).get()));
            }
        }
        emp_previous_Object.setEmpName(empdto.getEmployeeName());
        emp_previous_Object.setEmail(empdto.getEmail());
        emp_previous_Object.setDesignationDetails(designationOptionl_new.get());
        emp_previous_Object.setDepartmentDetails(departmentOptional_new.get());
        emp_previous_Object.setDbo(empdto.getDbo());
        emp_previous_Object.setUpdatedBy("Admin");
        emp_previous_Object.setUpdatedDate(LocalDateTime.now());
        emp_previous_Object.setImageData(imagefile.getBytes());
        return employeeRepository.save(emp_previous_Object);
    }

    public EmployeeDetails ChangeEmployeeStatusToTrue(Long id) {
        EmployeeDetails empObj = GetTheEmployeeDetailsById(id).get();
        UpdateTheTotalEmployeeCount_forDeptAndDesignation(empObj.getDepartmentDetails().getId(), empObj.getDesignationDetails().getId());
        empObj.setIsActive(true);
        empObj.setUpdatedBy("Super_user");
        empObj.setUpdatedDate(LocalDateTime.now());
        return employeeRepository.save(empObj);
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

