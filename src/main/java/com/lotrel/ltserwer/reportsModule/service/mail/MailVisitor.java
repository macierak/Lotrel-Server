package com.lotrel.ltserwer.reportsModule.service.mail;

import com.lotrel.ltserwer.reportsModule.domain.mail.MailReportRequest;

public interface MailVisitor {

    String visit(MailReportRequest request);
}
