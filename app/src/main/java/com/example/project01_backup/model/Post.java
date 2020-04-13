package com.example.project01_backup.model;

import java.io.Serializable;

public class Post implements Serializable {
    private String idPost, idUser, displayName ,emailUser, urlImage,urlAvatarUser;
    private String place, category, tittle, address, description, pubDate;
    private long longPubDate;


    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public long getLongPubDate() {
        return longPubDate;
    }

    public void setLongPubDate(long longPubDate) {
        this.longPubDate = longPubDate;
    }

    public String getUrlAvatarUser() {
        return urlAvatarUser;
    }

    public void setUrlAvatarUser(String urlAvatarUser) {
        this.urlAvatarUser = urlAvatarUser;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
