package com.demo.entity;

public class JgMessageEntity {

    private String test;
    private Boolean Success;
    private String TemplateID;
    private String Subject;
    private String Sender;
    private String Link;
    private String ConferenceDate;
    private String SendDate;

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getConferenceDate() {
        return ConferenceDate;
    }

    public void setConferenceDate(String conferenceDate) {
        ConferenceDate = conferenceDate;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(String templateID) {
        TemplateID = templateID;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
