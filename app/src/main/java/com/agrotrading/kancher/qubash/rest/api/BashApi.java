package com.agrotrading.kancher.qubash.rest.api;

import com.agrotrading.kancher.qubash.rest.model.BashModel;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;

public interface BashApi {

    @GET("/api/get?site=bash.im&name=bash&num=" + ConstantManager.QUANTITY_QUOTES)
    ArrayList<BashModel> getQuotes();

}
