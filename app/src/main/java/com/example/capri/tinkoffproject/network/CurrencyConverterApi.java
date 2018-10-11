package com.example.capri.tinkoffproject.network;

import com.example.capri.tinkoffproject.model.ExchangeRates;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyConverterApi {
    @GET("api/v6/convert")
    // https://free.currencyconverterapi.com/api/v6/convert?q=USD_RUB,RUB_USD&compact=ultra
    Call<HashMap<String, Double>> fetchExchangeRates(@Query("q") String currencyPair, @Query("compact")
            String value);
}