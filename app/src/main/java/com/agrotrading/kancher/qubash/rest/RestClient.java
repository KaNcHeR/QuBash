package com.agrotrading.kancher.qubash.rest;

import com.agrotrading.kancher.qubash.rest.api.BashApi;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import retrofit.RestAdapter;

public class RestClient {

    private BashApi bashApi;

    public RestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ConstantManager.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        bashApi = restAdapter.create(BashApi.class);

    }

    public BashApi getBashApi() { return bashApi; }

}
