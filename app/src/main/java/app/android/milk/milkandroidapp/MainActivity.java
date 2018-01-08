package app.android.milk.milkandroidapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    private TextView mTextMessage;
    public static int mainFragmentLayout;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Create Fragment View when Item is Clicked
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //clear backStack
            boolean itemFlag;
            itemFlag = false;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    SetHomeFragment();
                    break;
                case R.id.navigation_map:
                    SetMapFragment();
                    break;
                case R.id.navigation_camera:
                    mTextMessage.setText(R.string.title_camera);
                    fragment = new CameraFragment();
                    itemFlag = true;
                    break;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    fragment = new ProfileFragment();
                    itemFlag = true;
                    break;
            }
            if(itemFlag)
            {
                FragmentTransaction fragmentTx = fragmentManager.beginTransaction();
                fragmentTx.replace(mainFragmentLayout, fragment);
                fragmentTx.commit();
            }
            return itemFlag;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragmentLayout = R.id.container;
        fragmentManager = getSupportFragmentManager();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // SET THE DEFAULT FRAGMENT
        SetHomeFragment();
    }

    private void SetHomeFragment() {
        //Create Fragment View when Item is Clicked
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //clear backStack
        Fragment homeFragment = new HomeFragment();
        FragmentTransaction homeFragmentTx = fragmentManager.beginTransaction();
        homeFragmentTx.replace(mainFragmentLayout, homeFragment);
        homeFragmentTx.commit();
    }
    private void SetMapFragment() {
        //Create Fragment View when Item is Clicked
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //clear backStack
        Fragment mapFragment = new MapFragment();
        FragmentTransaction mapFragmentTx = fragmentManager.beginTransaction();
        mapFragmentTx.replace(mainFragmentLayout, mapFragment);
        mapFragmentTx.commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng UCSC = new LatLng(36.9914, -122.0609);
        googleMap.addMarker(new MarkerOptions().position(UCSC)
                .title("UCSC"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(UCSC));
    }
}
