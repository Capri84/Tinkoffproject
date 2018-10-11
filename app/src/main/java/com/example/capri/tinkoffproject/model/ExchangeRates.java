package com.example.capri.tinkoffproject.model;

import com.google.gson.annotations.SerializedName;

public class ExchangeRates {

	@SerializedName("USD_RUB")
	private double exchangeRate1;

	public void setExchangeRate1(double exchangeRate1) {
		this.exchangeRate1 = exchangeRate1;
	}

	public double getExchangeRate1() {
		return exchangeRate1;
	}

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "exchangeRate1=" + exchangeRate1 +
                '}';
    }
}