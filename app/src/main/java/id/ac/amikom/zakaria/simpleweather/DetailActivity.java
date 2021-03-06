package id.ac.amikom.zakaria.simpleweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final TextView tvWeather = findViewById(R.id.tv_weather);
        final TextView tvTemp = findViewById(R.id.tv_temp);
        final ImageView img = findViewById(R.id.imageView);
        final TextView tvDesc = findViewById(R.id.tv_desc);
        final TextView tvCountry = findViewById(R.id.tv_country);


        Intent intent = getIntent();
        final String jakarta = intent.getStringExtra("data1");



        String url = "http://api.openweathermap.org/data/2.5/weather?q=" +
                jakarta +"&appid=4de4dd041e499ca8a4f6692097befdae";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),
                        "Tidak dapat terhubung server", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                try{
                    final JSONObject objData = new JSONObject(responseData);
                    final JSONArray arrayWeather = objData.getJSONArray("weather");

                    final JSONObject objWeather = new JSONObject(arrayWeather.get(0).toString());
                    final JSONObject objTemp = new JSONObject(objData.get("main").toString());

                    double temp = (objTemp.getDouble("temp"))-273.15;
                    final String celcius = String.valueOf(String.format("%.1f", temp));

                    DetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tvCountry.setText(objData.get("name").toString());
                                tvWeather.setText(objWeather.get("main").toString());
                                tvDesc.setText(objWeather.get("description").toString());
                                tvTemp.setText(celcius+"°C");

                                String urlIcon = "http://openweathermap.org/img/w/"+objWeather.get("icon")+".png";
                                Glide.with(DetailActivity.this)
                                        .load(urlIcon)
                                        .into(img);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });
    }
}
