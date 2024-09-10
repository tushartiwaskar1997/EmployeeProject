package com.example.employee_app.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class EmailSchedularBirthDay {

    @Autowired
    private JavaMailSender javaMailSender ;

    public void sendthecomplexmail(String Emailid) throws MessagingException {

        MimeMessage mimeMessage  =  javaMailSender.createMimeMessage();
        MimeMessageHelper helper =  new MimeMessageHelper(mimeMessage,true,"UTF-8");
        helper.setTo(Emailid);
        helper.setSubject("Today' Greeting  !!");
        FileSystemResource resource  =  new FileSystemResource(new File("C:\\Users\\ADMIN\\Desktop\\aws   devops\\Nikon.jpg"));
        String response = "<p>This is the Greeting from Tushar . Have a Nice Day !!! <img src='cid:testimage' </p>";
        helper.setText(response ,true);
        helper.addInline("testimage",resource);
        javaMailSender.send(mimeMessage);
    }
}
