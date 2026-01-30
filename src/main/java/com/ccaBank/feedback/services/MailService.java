package com.ccaBank.feedback.services;

import com.ccaBank.feedback.entities.RDV;
import com.ccaBank.feedback.entities.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHtmlMailFromClient(
            String to,
            String subject,
            String htmlContent,
            User sender) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("tariqlavoisier@gmail.com", "CCA Bank");

            helper.setReplyTo(sender.getClient().getEmail());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi mail RDV", e);
        }
    }

    public void sendHtmlMail(String to, String subject, String htmlContent) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("tariqlavoisier@gmail.com", "CCA Bank");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi du mail HTML", e);
        }
    }


    public void sendRDVHonoredNotification(RDV rdv) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(rdv.getStaff().getStaffEmail());
        message.setSubject("RDV Honoré");
        message.setText(
                "Bonjour " + rdv.getStaff().getStaffName() +
                        ",\n\nLe rendez-vous du client " +
                        rdv.getClient().getFirstName() +
                        " prévu le " + rdv.getDateHeure() +
                        " a été honoré."
        );

        mailSender.send(message);
    }

}
