package com.example.project01_backup.model;

public class Feedback {
    private String idFeedBack,idUser , emailUser;
    private String stringPubDate, contentFeedBack, uriAvatarUser;
    private long longPubDate;

    public String getIdFeedBack() {
        return idFeedBack;
    }

    public void setIdFeedBack(String idFeedBack) {
        this.idFeedBack = idFeedBack;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getStringPubDate() {
        return stringPubDate;
    }

    public void setStringPubDate(String stringPubDate) {
        this.stringPubDate = stringPubDate;
    }

    public String getContentFeedBack() {
        return contentFeedBack;
    }

    public void setContentFeedBack(String contentFeedBack) {
        this.contentFeedBack = contentFeedBack;
    }

    public String getUriAvatarUser() {
        return uriAvatarUser;
    }

    public void setUriAvatarUser(String uriAvatarUser) {
        this.uriAvatarUser = uriAvatarUser;
    }

    public long getLongPubDate() {
        return longPubDate;
    }

    public void setLongPubDate(long longPubDate) {
        this.longPubDate = longPubDate;
    }
}
