package com.rps.smartsplit.service;

import com.rps.smartsplit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String email, String otp) {
        try {
            if (email != null && !email.isEmpty()) {

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("info@codvertex.in");
                message.setTo(email);
                message.setSubject("Your OTP - SmartSplit");

                message.setText(
                        "Dear User,\n\n" +
                                "Your One-Time Password (OTP) is:\n\n" +
                                otp + "\n\n" +
                                "This OTP is valid for 5 minutes. Please do not share it with anyone.\n\n" +
                                "If you did not request this, please ignore this email.\n\n" +
                                "Best Regards,\n" +
                                "Smart Split"
                );

                mailSender.send(message);
            }
        } catch (Exception e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
        }
    }

}
