package com.example.project01_backup.model;

public class Comment {
    private String idComment, idUser , emailUser, pubDate,contentComment, uriAvatarUser;
    private long longPubDate;

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
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

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}
