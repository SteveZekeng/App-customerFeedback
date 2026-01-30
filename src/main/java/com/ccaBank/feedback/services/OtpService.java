package com.ccaBank.feedback.services;

import com.ccaBank.feedback.entities.Otp;
import com.ccaBank.feedback.repositories.OtpRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private OtpRepository otpRepository;
    private JavaMailSender mailSender;
    private static final long OTP_EXPIRATION_MILLIS = 5 * 60 * 1000; // 5 minutes

    public OtpService(OtpRepository otpRepository, JavaMailSender mailSender) {
        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
    }

    public String generateOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'adresse email ne peut pas être vide !");
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Votre code OTP");
            message.setText("Bonjour,\n\nVoici votre code OTP : " + otp + "\n\nIl expire dans 5 minutes.");
            mailSender.send(message);

            System.out.println("OTP envoyé à : " + email + " -> " + otp);

        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'OTP : " + e.getMessage());
        }

        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(System.currentTimeMillis() + OTP_EXPIRATION_MILLIS);
        otpRepository.save(otpEntity);
        return otp;
    }

    private void sendEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Votre OTP est: " + otp);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'OTP par email");
        }
    }

    public boolean validateOtp(String email, String otp) {
        Otp otpEntity = otpRepository.findByEmail(email);
        return otpEntity != null && otpEntity.getOtp().equals(otp) &&
                System.currentTimeMillis() <= otpEntity.getExpirationTime();
    }
}
