package io.github.project_travel_mate.booking;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Simple wrapper for third party hotel booking API.
 */
public class BookingApiService {

    private static final String TAG = "BookingApiService";
    private static final String BASE_URL = "https://example-booking.com/api/search";

    public interface BookingCallback {
        void onSuccess(JSONArray results);
        void onFailure();
    }

    public void searchHotels(String city, @Nullable BookingCallback callback) {
        String url = BASE_URL + "?city=" + city;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Booking request failed", e);
                if (callback != null) {
                    callback.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (callback != null) {
                        callback.onFailure();
                    }
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response.body().string());
                    if (callback != null) {
                        callback.onSuccess(array);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Invalid response", e);
                    if (callback != null) {
                        callback.onFailure();
                    }
                }
            }
        });
    }
}
