package com.siddhant.checkmateclimate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// ... (imports)

public class MainActivity extends AppCompatActivity {

    private EditText cityEditText;
    private Button getWeatherButton;
    private LinearLayout weatherInfoLayout;
    private TextView temperatureTextView;
    private TextView descriptionTextView;
    private ImageView weatherIcon;
    private TextView forecastTextView;
    String cityName;

    private static final String API_KEY = "c9ce854ee5a20ea7ef98b6136767d849";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q={cityName}&appid={API_KEY}";
    //private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API_KEY}";
    private static final String API_FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        cityEditText = findViewById(R.id.cityEditText);
        getWeatherButton = findViewById(R.id.getWeatherButton);
        weatherInfoLayout = findViewById(R.id.weatherInfoLayout);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        weatherIcon = findViewById(R.id.weatherIcon);
        forecastTextView = findViewById(R.id.forecastTextView);

        // Set click listener for Get Weather button
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = cityEditText.getText().toString();
                new FetchWeatherTask().execute(cityName);
                new FetchForecastTask().execute(cityName);
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String cityName = strings[0];
            String urlString = API_URL.replace("{cityName}", cityName).replace("{API_KEY}", API_KEY);


            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder responseBuilder = new StringBuilder();

                while (scanner.hasNextLine()) {
                    responseBuilder.append(scanner.nextLine());
                }

                scanner.close();
                return responseBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);

                    String temperature = main.getString("temp");
                    String description = weatherObject.getString("description");

                    // Update UI elements with weather data
                    temperatureTextView.setText(temperature);
                    descriptionTextView.setText(description);

                    // You might also need to handle the weather icon
                    // ...

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private class FetchForecastTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0];
            String urlString = API_FORECAST_URL + "?q=" + cityName + "&appid=" + API_KEY;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder responseBuilder = new StringBuilder();

                while (scanner.hasNextLine()) {
                    responseBuilder.append(scanner.nextLine());
                }

                scanner.close();
                return responseBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("list");

                    // Parse and extract forecast data from the "list" array
                    // Update forecastTextView or other UI elements as needed

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
