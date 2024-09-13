package com.example.employee_app.schedular;

import com.example.employee_app.constanst.MessageConfig;
import com.example.employee_app.email.EmailSchedularBirthDay;
import com.example.employee_app.model.EmployeeDetails;
import com.example.employee_app.repository.EmployeeRepository;
import com.example.employee_app.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmailSchedular {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmailSchedularBirthDay emailSchedularBirthDay;
    @Scheduled(cron = "0 0 0 * * * ")  //every 0th hour every day
    public String SendTheGreetingOfBirthday_CronJob() throws MessagingException {
        List<EmployeeDetails> listofEmp =  employeeService.FindTheListOfEmployeesAsPerTheMonthAndDate(LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        for(EmployeeDetails emp : listofEmp){
            emailSchedularBirthDay.sendthecomplexmail(emp.getEmail());
        }
        return MessageConfig.AUTO_EMAIL_SEND_SUCCESSFULLY;
    }
    public String SendTheGreetingOfBirthday_direct(int Month ,int day) throws MessagingException {
        List<EmployeeDetails> listofEmp =  employeeService.FindTheListOfEmployeesAsPerTheMonthAndDate(Month, day);
        for(EmployeeDetails emp : listofEmp){
            emailSchedularBirthDay.sendthecomplexmail(emp.getEmail());
        }
        return MessageConfig.Manual_EMAIL_SEND_SUCCESSFULLY;
    }
}
