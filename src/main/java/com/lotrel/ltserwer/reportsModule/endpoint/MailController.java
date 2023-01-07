package com.lotrel.ltserwer.reportsModule.endpoint;

import com.lotrel.ltserwer.reportsModule.domain.mail.MailRequest;
import com.lotrel.ltserwer.reportsModule.domain.mail.MailReportRequest;
import com.lotrel.ltserwer.reportsModule.service.mail.MailDispatcher;
import com.lotrel.ltserwer.reportsModule.service.SendMailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MailController {

    private final MailDispatcher mailDispatcher;

    @GetMapping("api/mail")
    public String sendMail() {
        MailRequest request = new MailReportRequest("izawojtowicz0@gmail.com", "Iza Wojtowicz", "Nazwa raportu", "report/report.txt");
        return request.accept(mailDispatcher);
    }
}
