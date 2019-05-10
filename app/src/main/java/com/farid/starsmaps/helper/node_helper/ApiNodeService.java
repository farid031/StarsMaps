package com.farid.starsmaps.helper.node_helper;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiNodeService {
    @GET("read_node_jalur.php")
    Call<ListLocationModel> getAllLocation();
}
