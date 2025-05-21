package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.github.project_travel_mate.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteOptimizationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String ARG_AIRPORT_LAT = "airport_lat";
    private static final String ARG_AIRPORT_LNG = "airport_lng";
    private static final String ARG_HOTEL_LAT = "hotel_lat";
    private static final String ARG_HOTEL_LNG = "hotel_lng";
    private static final String ARG_ATTRACTIONS = "attractions_list";

    private GoogleMap mMap;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RouteOptimizationActivity.class);
    }

    public static Intent getStartIntent(Context context, double airportLat, double airportLng,
                                        double hotelLat, double hotelLng,
                                        ArrayList<LatLng> attractions) {
        Intent intent = new Intent(context, RouteOptimizationActivity.class);
        intent.putExtra(ARG_AIRPORT_LAT, airportLat);
        intent.putExtra(ARG_AIRPORT_LNG, airportLng);
        intent.putExtra(ARG_HOTEL_LAT, hotelLat);
        intent.putExtra(ARG_HOTEL_LNG, hotelLng);
        intent.putParcelableArrayListExtra(ARG_ATTRACTIONS, attractions);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_optimization);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double airportLat = getIntent().getDoubleExtra(ARG_AIRPORT_LAT, 0);
        double airportLng = getIntent().getDoubleExtra(ARG_AIRPORT_LNG, 0);
        double hotelLat = getIntent().getDoubleExtra(ARG_HOTEL_LAT, 0);
        double hotelLng = getIntent().getDoubleExtra(ARG_HOTEL_LNG, 0);
        ArrayList<LatLng> attractions = getIntent().getParcelableArrayListExtra(ARG_ATTRACTIONS);
        if (attractions == null) {
            attractions = new ArrayList<>();
        }

        LatLng airport = new LatLng(airportLat, airportLng);
        LatLng hotel = new LatLng(hotelLat, hotelLng);

        mMap.addMarker(new MarkerOptions().position(airport).title("Airport"));
        mMap.addMarker(new MarkerOptions().position(hotel).title("Hotel"));
        for (LatLng point : attractions) {
            mMap.addMarker(new MarkerOptions().position(point).title("Attraction"));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotel, 12f));

        fetchRoute(airport, hotel, attractions);
    }

    private void fetchRoute(LatLng airport, LatLng hotel, List<LatLng> attractions) {
        StringBuilder waypoints = new StringBuilder();
        for (LatLng p : attractions) {
            waypoints.append(p.latitude).append(',').append(p.longitude).append('|');
        }
        if (waypoints.length() > 0) {
            waypoints.setLength(waypoints.length() - 1);
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + airport.latitude + "," + airport.longitude
                + "&destination=" + hotel.latitude + "," + hotel.longitude
                + "&waypoints=" + waypoints.toString()
                + "&key=" + getString(R.string.google_maps_key);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("RouteOptimization", "Route fetch failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("RouteOptimization", "Unexpected code " + response);
                    return;
                }
                // TODO: Parse directions response and draw polyline on map
            }
        });
    }
}
