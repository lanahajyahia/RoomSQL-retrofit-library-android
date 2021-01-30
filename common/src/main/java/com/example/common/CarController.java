package com.example.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CarController {
    static final String BASE_URL = "https://dev-api.com/lana/";

    private CallBack_Car callBack_car;
    private CallBack_Cars callBack_cars;

    Callback<Car> carCallBack = new Callback<Car>() {
        @Override
        public void onResponse(Call<Car> call, Response<Car> response) {
            if (response.isSuccessful()) {
                Car car = response.body();
                if (callBack_car != null) {
                    callBack_car.car(car);
                }
            } else {
                System.out.println(response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<Car> call, Throwable t) {
            t.printStackTrace();
        }
    };

    public void fetchCar(CallBack_Car callBack_car) {
        this.callBack_car = callBack_car;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CarsAPI carsAPI = retrofit.create(CarsAPI.class);

        Call<Car> call = carsAPI.loadCars();
        call.enqueue(carCallBack);
    }

    public interface CallBack_Cars {
        void cars(List<Car> cars);
    }

    public interface CallBack_Car {
        void car(Car car);
    }
}
