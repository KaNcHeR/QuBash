package com.agrotrading.kancher.qubash.rest;

import com.agrotrading.kancher.qubash.rest.model.BashModel;

import java.util.ArrayList;

public class RestService {

    private RestClient restClient;

    public RestService() {
        restClient = new RestClient();
    }

    public ArrayList<BashModel> getQuotes() {
        return restClient.getBashApi().getQuotes();
    }

}
