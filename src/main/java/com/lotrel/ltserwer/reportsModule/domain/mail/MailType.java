package com.lotrel.ltserwer.reportsModule.domain.mail;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MailType {
    REPORT_MAIL(MailTemplateName.REPORT_CONTENT, MailTemplateName.REPORT_SUBJECT);

    final MailTemplateName templateContent;
    final MailTemplateName templateSubject;

    public static String getTemplateContent(MailType mailType) {
        return mailType.templateContent.name() + ".html";
    }

    public static String getTemplateSubject(MailType mailType) {
        return mailType.templateSubject.name() + ".html";
    }
}
