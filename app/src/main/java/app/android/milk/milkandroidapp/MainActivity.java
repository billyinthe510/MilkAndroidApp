package app.android.milk.milkandroidapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    public static int mainFragmentLayout;
    private FragmentManager fragmentManager;

    //region BottomNavigation OnNavigationItemSelectedListener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Create Fragment View when Item is Clicked
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    SetFragment(new HomeFragment());
                    break;
                case R.id.navigation_map:
                    SetMapFragment();
                    break;
                case R.id.navigation_camera:
                    SetFragment(new CameraFragment());
                    break;
                case R.id.navigation_profile:
                    SetFragment(new ProfileFragment());
                    break;
            }
            return true;
        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragmentLayout = R.id.container;
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // SET THE DEFAULT FRAGMENT
        SetFragment(new HomeFragment());
    }

    //region SetFragment
    private void SetFragment(Fragment fragment)
    {
        //Create Fragment View when Item is Clicked
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //clear backStack
        FragmentTransaction fragmentTx = fragmentManager.beginTransaction();
        fragmentTx.replace(mainFragmentLayout, fragment);
        fragmentTx.commit();
    }
    //endregion
    //region SetMapFragment
    private void SetMapFragment()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null)
        {
            GoogleMapOptions mapOptions = new GoogleMapOptions()
                    .mapType(GoogleMap.MAP_TYPE_NORMAL)
                    .zoomControlsEnabled(false)
                    .compassEnabled(true);
            FragmentTransaction fragmentTx = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance(mapOptions);
            fragmentTx.replace(mainFragmentLayout, mapFragment);
            fragmentTx.commit();
        }
        mapFragment.getMapAsync(this);
    }
    //endregion

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng UCSC = new LatLng(36.9914, -122.0609);
        googleMap.addMarker(new MarkerOptions().position(UCSC)
                .title("UCSC"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(UCSC));
    }
}
