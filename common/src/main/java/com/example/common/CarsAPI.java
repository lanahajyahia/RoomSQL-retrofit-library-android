package com.example.common;




import retrofit2.Call;
import retrofit2.http.GET;

public interface CarsAPI {

    @GET("cars")
    Call<Car> loadCars();

}
