package com.example.yeneservice.Extra;

import com.example.yeneservice.Notification.NotificationSender;
import com.example.yeneservice.Notification.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:applicatioin/json",
                    "Authorization:key=AAAAzkXbDXc:APA91bH3IIglSKuUmHIqB0u4UFBakzaYyOLNzs-OgrznDkRaVe38Z450OvlibdcKHj8rSwzRDfsSd1BgXF2P7Wfo_wcE_dppozvSZxUmy6H0DWm2pGIVwWBopb6SyB5G5Y5EASn0IZDg"
            }
    )

    @POST("from/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
