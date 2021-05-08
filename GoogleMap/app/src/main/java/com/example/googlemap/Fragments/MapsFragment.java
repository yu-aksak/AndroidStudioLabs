package com.example.googlemap.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.googlemap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MapsFragment extends Fragment {
    Geocoder geocoder;
    String myCountry, myCity;
    double[] coordinates;
    Button save;
    Fragment fragment;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NotNull GoogleMap googleMap) {

            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            Bundle bundle = getArguments();
            if (bundle != null) {
                coordinates = bundle.getDoubleArray("coordinates");
            } else
                coordinates = new double[]{-34, 151};

            LatLng location = new LatLng(coordinates[0], coordinates[1]);
            googleMap.setMapType(MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(location).title(""));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            if (requireActivity().getSupportFragmentManager().findFragmentByTag("addPostFragment") != null) {
                googleMap.setOnMapClickListener(latLng -> {
                    MarkerOptions markerOptions = new MarkerOptions();
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addresses.size() != 0) {
                            coordinates = new double[]{Double.parseDouble(String.valueOf(latLng.latitude)),
                                    Double.parseDouble(String.valueOf(latLng.longitude))};
                            myCity = addresses.get(0).getLocality();
                            myCountry = addresses.get(0).getCountryName();
                        } else {
                            myCity = "";
                            myCountry = "don't work";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    markerOptions.position(latLng);
                    markerOptions.title(myCity);
                    markerOptions.snippet(latLng.latitude + " : " + latLng.longitude);
                    googleMap.clear();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                    googleMap.addMarker(markerOptions);
                });
                save.setVisibility(View.VISIBLE);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        save = rootView.findViewById(R.id.btn_save);

        View.OnClickListener saveButtonOnClickListener = view -> {
            Toast.makeText(getActivity(), "Вы добавили локацию",
                    Toast.LENGTH_SHORT).show();
            Add();
        };
        save.setOnClickListener(saveButtonOnClickListener);

        Button back = rootView.findViewById(R.id.back3);

        View.OnClickListener backButtonOnClickListener = view -> Back();

        back.setOnClickListener(backButtonOnClickListener);

        fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("addPostFragment");
        if(fragment == null)
            fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("postFragment");
        return rootView;
    }

    private void Add(){
        AddPostFragment apf = (AddPostFragment) fragment;
        Bundle bundle = new Bundle();
        bundle.putString("location", myCountry + ", " + myCity);
        bundle.putDoubleArray("coordinates", coordinates);
        assert apf != null;
        apf.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().show(apf).commit();
        apf.addLoc();
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void Back(){
        requireActivity().getSupportFragmentManager().beginTransaction().show(fragment).commit();
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}