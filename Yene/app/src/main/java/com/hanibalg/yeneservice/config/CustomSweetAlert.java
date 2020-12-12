package com.hanibalg.yeneservice.config;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomSweetAlert {

    private static Context context;
    private static String content;

    public CustomSweetAlert(Context context,String content) {
        this.context = context;
        this.content = content;
    }

    public void alertLoading(String msg){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void successMessageAlert(){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("You clicked the button!")
                .show();
    }

    public void errorMEssageAlert(){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong!")
                .show();
    }

    public void warningMessageAlert(String message){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText(message)
                .setConfirmText("Yes,delete it!")
                .show();
    }

    public void stopAlert(){
        new SweetAlertDialog(context)
                .dismiss();
    }
}
