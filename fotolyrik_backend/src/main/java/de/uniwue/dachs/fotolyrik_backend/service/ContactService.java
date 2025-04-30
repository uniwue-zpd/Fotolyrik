package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.ContactDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final MailSender mailSender;

    public ContactService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String owner;

    public void sendEmail(ContactDTO contactDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(owner);
        message.setSubject(contactDTO.getSubject());
        message.setText(contactDTO.getMessage());
        message.setReplyTo(contactDTO.getEmail());
        mailSender.send(message);
    }
}
