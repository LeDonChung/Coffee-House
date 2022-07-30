package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private float DEFAULT_ZOOM = 15;
    private LatLng latLng = new LatLng(10.821854179047428, 106.68849252968513);
    private SearchView search;
    private TextView location;
    private ImageButton confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        search = findViewById(R.id.search);
        location = findViewById(R.id.location);
        confirm = findViewById(R.id.confirm);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationDefault();
        onClickMap();
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        placesClient = Places.createClient(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        updateLocationUI();
        getLastLocation();
        onQuerySearchView();
        eventButton();
    }

    public void eventButton() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lc = location.getText().toString().trim();
                if(lc != null && !lc.equals("")) {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle("Chọn địa điểm")
                            .setMessage("Bạn có muốn chọn " + lc + " làm điểm đến?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.shoppingCart.setAddressDelivery(lc);
                                    OrderActivity.getDeliveryWay(lc);
                                    finish();
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();

                } else {
                    Toast.makeText(MapsActivity.this, "Vui lòng chọn địa điểm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getAddressToLocationView(String strLocation) throws IOException {
        if (strLocation != null && !strLocation.equals("")) {
            // khi nhấp vào đỉa chỉ nào đó hoặc tìm kiếm
            location.setText(strLocation);
        }
    }

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            try {
                                ArrayList<Address> addresses = null;
                                Geocoder geocoder = new Geocoder(MapsActivity.this);
                                addresses = (ArrayList<Address>) geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String lc = addresses.get(0).getFeatureName()+ " " +addresses.get(0).getThoroughfare() +", " + addresses.get(0).getSubAdminArea() +", "+ addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                                getAddressToLocationView(lc);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    public void onQuerySearchView() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = search.getQuery().toString();
                List<Address> addresss = null;
                if(location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addresss = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addresss.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    try {
                        getAddressToLocationView(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void onClickMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                mMap.addMarker(markerOptions);
                getAddressByLocation(latLng.latitude, latLng.longitude);


                try {
                    ArrayList<Address> addresses = null;
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    addresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String lc = addresses.get(0).getFeatureName()+ " " +addresses.get(0).getThoroughfare() +", " + addresses.get(0).getSubAdminArea() +", "+ addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                    getAddressToLocationView(lc);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    public void getLocationDefault() {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Đại học Công nghiệp IUH"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(30.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        getAddressByLocation(latLng.latitude, latLng.longitude);
    }
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_GPS_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == REQUEST_CODE_GPS_PERMISSION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void getAddressByLocation(double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                Log.d("AAA", "wanning");
            }
            else {
                if (addresses.size() > 0) {
                    MarkerOptions options = new MarkerOptions();
                    options.title(addresses.get(0).getFeatureName()+ " " +addresses.get(0).getThoroughfare() +", " + addresses.get(0).getSubAdminArea() +", "+ addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    options.position(new LatLng(latitude, longitude));
                    mMap.clear();
                    mMap.addMarker(options);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }

}