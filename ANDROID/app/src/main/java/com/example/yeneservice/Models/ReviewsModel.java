package com.example.yeneservice.Models;

import java.util.Date;

public class ReviewsModel {
    private String reviewId,firstName;
    private String reviewedServiceProviderId;
    private String content, image;
    private Date reviewDate;
    private int rate;

    public ReviewsModel(String firstName, String content, String image, int rate) {
//        this.reviewId = reviewId;
        this.firstName = firstName;
        this.content = content;
        this.image = image;
        this.rate = rate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewedServiceProviderId() {
        return reviewedServiceProviderId;
    }

    public void setReviewedServiceProviderId(String reviewedServiceProviderId) {
        this.reviewedServiceProviderId = reviewedServiceProviderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
