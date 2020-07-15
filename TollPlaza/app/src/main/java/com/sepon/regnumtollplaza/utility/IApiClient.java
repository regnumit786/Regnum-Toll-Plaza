package com.sepon.regnumtollplaza.utility;

import com.sepon.regnumtollplaza.pojo.Norshinddi;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ismail Sepon on 1/30/2019.
 * Regnum IT Ltd
 * ismailhossainsepon@gmail.com
 */
public interface IApiClient {

        @GET(URLUtils.TODAY)
        Call<List<Norshinddi>> gettodaysreport();




}
