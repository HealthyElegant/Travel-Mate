package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.searchcitydialog.CitySearchBottomSheetDialogFragment;
import io.github.project_travel_mate.searchcitydialog.CitySearchModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

/**
 * Display list of hotels in destination city
 */
public class HotelsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_SELECTED_CITY = "KEY_SELECTED_CITY";

    @BindView(R.id.hotel_list)
    RecyclerView recyclerView;
    @BindView(R.id.select_city)
    Button selectCity;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.layout)
    LinearLayout layout;

    private Handler mHandler;
    private String mToken;

    private CitySearchModel mSelectedCity;

    private ArrayList<CitySearchModel> mSearchCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        fetchCitiesList();

        setTitle("Hotels");

        selectCity.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SELECTED_CITY)) {
            mSelectedCity = savedInstanceState.getParcelable(KEY_SELECTED_CITY);
            if (mSelectedCity != null) {
                selectCity.setText(String.format(getString(R.string.showing_hotels), mSelectedCity.getName()));
                getCityInfo(mSelectedCity.getId());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calls API to get hotel list
     */
    private void getHotelList(String latitude, String longitude) {

        String uri = API_LINK_V2 + "get-places/" + latitude + "/" + longitude + "/accommodation";

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                Log.v("RESPONSE", res + " ");
                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray feedItems = new JSONArray(res);
                            Log.v("response", feedItems + " ");
                            selectCity.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            animationView.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            int itemsSize = feedItems.length();
                            if (itemsSize > 0) {
                                /*
                                 * Extracting data from json and adding it to the model
                                 * then adding that model object to the list
                                 */
                                HotelsModel hotelsModel;
                                List<HotelsModel> hotelsModelList = new ArrayList<>();
                                JSONObject jo;
                                try {
                                    for (int i = 0; i < itemsSize; i++) {
                                        jo = feedItems.getJSONObject(i);
                                        hotelsModel = new HotelsModel(jo.getString("title"),
                                                jo.getString("address"),
                                                jo.optString("phone", "000"),
                                                jo.optString("href"),
                                                jo.getInt("distance"),
                                                jo.getDouble("latitude"),
                                                jo.getDouble("longitude"));
                                        hotelsModelList.add(hotelsModel);
                                    }
                                    //Passing the data list to the adapter
                                    recyclerView.setAdapter(new HotelsAdapter(HotelsActivity.this, hotelsModelList));
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                    networkError();
                                }
                            } else {
                                noResults();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }

    /**
     * Fetches the list cities from server
     */
    private void fetchCitiesList() {

        String uri = API_LINK_V2 + "get-all-cities/5000";
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();
                            Log.v("RESULT", res);
                            JSONArray ar = new JSONArray(res);
                            for (int i = 0; i < ar.length(); i++) {
                                mSearchCities.add(new CitySearchModel(
                                        ar.getJSONObject(i).getString("city_name"),
                                        ar.getJSONObject(i).optString("image"),
                                        ar.getJSONObject(i).getString("id")));
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            networkError();
                            Log.e("ERROR", "Message : " + e.getMessage());
                        }
                    } else {
                        Log.e("ERROR", "Network error");
                        networkError();
                    }
                });
            }
        });

    }

    /**
     * Calls the API & fetch details of city with given id
     *
     * @param cityId the city id
     */
    public void getCityInfo(String cityId) {

        animationView.setVisibility(View.VISIBLE);

        String uri = API_LINK_V2 + "get-city/" + cityId;
        Log.v("EXECUTING", uri);
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {

                    final String res = Objects.requireNonNull(response.body()).string();
                    try {
                        Log.v("Response", res);
                        JSONObject responseObject = new JSONObject(res);
                        String latitude = responseObject.getString("latitude");
                        String longitude = responseObject.getString("longitude");
                        getHotelList(latitude, longitude);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkError();
                    }
                } else {
                    networkError();
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_city:
                CitySearchBottomSheetDialogFragment citySearchBottomSheetDialogFragment =
                        CitySearchBottomSheetDialogFragment.newInstance(R.string.search_title, R.string.search_hint);
                citySearchBottomSheetDialogFragment.setmCitySearchModels(mSearchCities);
                citySearchBottomSheetDialogFragment.setmListener(position -> {
                    CitySearchModel item = mSearchCities.get(position);
                    mSelectedCity = item;
                    String selectedCity = item.getId();
                    selectCity.setText(String.format(getString(R.string.showing_hotels), item.getName()));
                    citySearchBottomSheetDialogFragment.dismissAllowingStateLoss();
                    getCityInfo(selectedCity);
                });
                citySearchBottomSheetDialogFragment.show(getSupportFragmentManager(), "CitySearch");
                recyclerView.setAdapter(null);
                break;
        }
    }


    /**
     * save selected city to bundle
     * in case of configuration change like device screen rotation.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SELECTED_CITY, mSelectedCity);
    }


    /**
     * Plays the network lost animation in the view
     */
    void networkError() {
        layout.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    /**
     * Plays the no results animation in the view
     */
    void noResults() {
        layout.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        Toast.makeText(HotelsActivity.this, R.string.no_trips, Toast.LENGTH_LONG).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, HotelsActivity.class);
    }

}
