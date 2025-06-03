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
        // Send email to the owner
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(owner);
        message.setSubject(contactDTO.getSubject());
        message.setText("Neue Anfrage von: "
                + contactDTO.getName()
                + (contactDTO.getAppellation() != null ? " (" + contactDTO.getAppellation() + ")\n" : "\n")
                + "Nachricht: " + contactDTO.getMessage()
                + "\n");
        message.setReplyTo(contactDTO.getEmail());
        mailSender.send(message);

        // Send confirmation email to the user
        SimpleMailMessage replyMessage = new SimpleMailMessage();
        replyMessage.setTo(contactDTO.getEmail());
        replyMessage.setSubject("Bestätigung: Ihre Nachricht ist bei uns eingegangen");
        replyMessage.setText("Vielen Dank für Ihre Nachricht! Wir werden uns so schnell wie möglich bei Ihnen melden.");
        mailSender.send(replyMessage);
    }
}
