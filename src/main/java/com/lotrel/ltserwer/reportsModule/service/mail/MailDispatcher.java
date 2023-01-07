package com.lotrel.ltserwer.reportsModule.service.mail;

import com.lotrel.ltserwer.reportsModule.domain.mail.MailType;
import com.lotrel.ltserwer.reportsModule.domain.mail.MailReportRequest;
import com.lotrel.ltserwer.reportsModule.service.AttachmentService;
import com.lotrel.ltserwer.reportsModule.service.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailDispatcher implements MailVisitor {

    private final SendMailService mailService;
    private final AttachmentService attachmentService;
    private final TemplateEngine templateEngine;

    @Override
    public String visit(MailReportRequest request) {
        File attachment = null;
        try {
            attachment = attachmentService.getFile(request.getFileLocation());
        } catch (FileNotFoundException | URISyntaxException e) {
            log.error("Nie można znaleźć pliku: {}", request.getFileLocation());
        }

        final Context context = new Context();
        context.setVariable("name", request.getUsername());
        context.setVariable("report", request.getReport());
        if (Objects.nonNull(attachment)) {
            context.setVariable("date", getDate(attachment));
        }

        final String content = getTemplate(MailType.getTemplateContent(MailType.REPORT_MAIL), context);
        final String subject = getTemplate(MailType.getTemplateSubject(MailType.REPORT_MAIL), null);

        sendMail(request.getMailTo(), subject, content, attachment);

        return "Wysłano wiadomość";
    }

    private void sendMail(String mailTo, String subject, String content, File attachment) {
        try {
            mailService.sendMail(mailTo, subject, content, attachment);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDate(File attachment) {
        try {
            return attachmentService.getCreationTime(attachment).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (Exception e) {
            log.error("Nie można pobrać daty utworzenia pliku {}", attachment.getName());
        }

        return OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private String getTemplate(String path, Context context) {
        if (Objects.isNull(context)) {
            return templateEngine.process(path, new Context());
        } else {
            return templateEngine.process(path, context);
        }
    }
}
