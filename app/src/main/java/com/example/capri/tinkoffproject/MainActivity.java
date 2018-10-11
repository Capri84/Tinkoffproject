package com.example.capri.tinkoffproject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capri.tinkoffproject.network.CurrencyConverterApi;
import com.example.capri.tinkoffproject.network.RetrofitInstance;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String COMPACT_VALUE = "ultra";
    Spinner spinnerConvertFrom;
    Spinner spinnerConvertTo;
    String convertFrom;
    String convertTo;
    ProgressBar progressBar;
    Button convertButton;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerConvertFrom = findViewById(R.id.spinner_from);
        spinnerConvertTo = findViewById(R.id.spinner_to);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        convertButton = findViewById(R.id.convert_btn);
        convertButton.setOnClickListener(this);

        setupSpinners();
    }

    /**
     * Setup the dialog spinner that allows the user to select the currency.
     */
    private void setupSpinners() {
        // Create adapter for spinners.
        ArrayAdapter currencySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);

        // Specify layout style - simple list view with 1 item per line
        currencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinners
        spinnerConvertFrom.setAdapter(currencySpinnerAdapter);
        spinnerConvertTo.setAdapter(currencySpinnerAdapter);

        spinnerConvertFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertFrom = spinnerConvertFrom.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Nothing selected.", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerConvertTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertTo = spinnerConvertTo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Nothing selected.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNetworkState() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        activeNetwork = cm.getActiveNetworkInfo();
    }

    @Override
    public void onClick(View v) {
        convertButton.setText("");
        progressBar.setVisibility(View.VISIBLE);
        getNetworkState();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            CurrencyConverterApi converterApi = RetrofitInstance.getRetrofitInstance().
                    create(CurrencyConverterApi.class);
            final String currencyPair = convertFrom + "_" + convertTo;
            Call<HashMap<String, Double>> call = converterApi.fetchExchangeRates(currencyPair, COMPACT_VALUE);

            /*Log the URL called*/
            Log.v("URL Called", call.request().url() + "");

            call.enqueue(new Callback<HashMap<String, Double>>() {
                @Override
                public void onResponse(Call<HashMap<String, Double>> call,
                                       Response<HashMap<String, Double>> response) {
                    double result = response.body().get(currencyPair);
                    TextView rate = findViewById(R.id.exchange_rate_tv);
                    rate.setText(String.valueOf(result));
                    TextView resultAmount = findViewById(R.id.result_amount_tv);
                    resultAmount.setText(String.valueOf(totalExchangeCount(result)));
                    progressBar.setVisibility(View.INVISIBLE);
                    convertButton.setText(R.string.convert_button);
                }

                @Override
                public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, getString(R.string.on_failure_toast),
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    convertButton.setText(R.string.convert_button);
                }
            });
        } else {
            Toast.makeText(MainActivity.this, R.string.no_internet_connection,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public double totalExchangeCount(double rate) {
        TextInputEditText inputEditText = findViewById(R.id.sum_to_convert);
        String sum = inputEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(sum)) {
            double sumToConvert = Double.parseDouble(sum);
            return sumToConvert * rate;
        } else {
            return 0;
        }
    }
}
