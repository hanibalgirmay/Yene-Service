package com.hanibalg.yeneservice.models;

public class PaymentCard {
    private int backgroundColor;
    private String cardName;
    private String expireIn;
    private String hint;
    private String content;

    public PaymentCard(int backgroundColor, String cardName, String expireIn, String hint, String content) {
        this.backgroundColor = backgroundColor;
        this.cardName = cardName;
        this.expireIn = expireIn;
        this.hint = hint;
        this.content = content;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
