package com.iago.networkingconference.network;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface FestivalityAPI {

    @GET("user-list")
    @Headers({
            "x-apiclient: {\"apiClientId\":\"testing-account-cli\",\"apiToken\":\"$2y$10$C/quaRQUsrWa30hjQJuckOXbW9kIZ.W3G1TlLMYg6lr/XDUes7SM.\"}",
            "x-header-request: {\"deviceId\": \"CBF230BA-7F16-4ED7-9A0E-44C9B314D09C\"}"
    })
    Observable<APIResponse> fetchUserList();
}
