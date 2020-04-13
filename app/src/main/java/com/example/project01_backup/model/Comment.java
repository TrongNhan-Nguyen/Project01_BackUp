package com.example.project01_backup.model;

public class Comment {
    private String idComment, idPost ,idUser , emailUser, displayName;
    private String pubDate,contentComment, uriAvatarUser;
    private long longPubDate;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

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

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }
}
