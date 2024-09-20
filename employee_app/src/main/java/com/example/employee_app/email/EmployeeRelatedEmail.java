package com.example.employee_app.email;

import com.example.employee_app.model.EmployeeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmployeeRelatedEmail {

    @Autowired
    private JavaMailSender javaMailSender;


    public void SendTheEmployeeAdditionMAIL(EmployeeDetails employeeData) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); //new MimeMessageHelper(mimeMessage,true,"UTF-8");
        helper.setTo(employeeData.getEmail());
        helper.setSubject("you are added to the company");
        FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\ADMIN\\Desktop\\aws   devops\\empAdded.png"));
        String response = "<p> Congratulations  you are add  to our Company .Your ID is " + employeeData.getId() + " <img src='cid:testimage' </p>";
        helper.setText(response, true);
        helper.addInline("testimage", resource);
        javaMailSender.send(mimeMessage);
    }

    public void SendTheDeleteEmployeeEmail(EmployeeDetails employeeDetails) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); //new MimeMessageHelper(mimeMessage,true,"UTF-8");
        helper.setTo(employeeDetails.getEmail());
        helper.setSubject("you are removed from  the company");
        FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\ADMIN\\Desktop\\aws   devops\\deleteemp.jfif"));
        String response = "<p> We are sorry to inform you that you  are removed from the Company .Your ID is " + employeeDetails.getId() + " <img src='cid:testimage' </p>";
        helper.setText(response, true);
        helper.addInline("testimage", resource);
        javaMailSender.send(mimeMessage);
    }
}
