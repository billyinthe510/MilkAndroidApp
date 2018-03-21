package app.android.milk.milkandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    public static int mainFragmentLayout;
    private FragmentManager fragmentManager;
    public GoogleMap gMap;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private List<Marker> markers;
    private List<MarkerInfo> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragmentLayout = R.id.container;
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        markers = new LinkedList<Marker>();
        info = new LinkedList<MarkerInfo>();
        info.add(new MarkerInfo("UCSC",36.9914, -122.0609 ));
        info.add(new MarkerInfo("SFSU",37.7219, -122.4782 ));
        info.add(new MarkerInfo("UCLA",34.0689, -118.4452 ));

        // SET THE DEFAULT FRAGMENT
        SetFragment(new HomeFragment());
    }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // Check Location Permission Granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission Already Granted
            // Enable MyLocation layer
            googleMap.setMyLocationEnabled(true);
        } else {
            // Request Location Permission
            RequestLocationPermission();
        }

        if(googleMap != null) {
            Marker marker;
            LatLng position;
            MarkerInfo currentMarker;
            int nMarkers = info.size();
            for(int i=0; i<nMarkers; i++)
            {
                currentMarker = info.get(i);
                position = new LatLng(currentMarker.getLatitude(), currentMarker.getLongitude());
                marker = googleMap.addMarker(
                        new MarkerOptions()
                        .title(currentMarker.getTitle())
                        .position(position)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                markers.add(marker);
            }

            // currently hardcoded viewpoint set to UCSC
            position = new LatLng(36.9914, -122.0609);
            // hardcoded initial position: needs future work
            CameraPosition.Builder builder = CameraPosition.builder();
            builder.target(position);
            builder.zoom(6);
            builder.bearing(0);
            builder.tilt(15);
            CameraPosition cameraPosition = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);
        }
    }
    //endregion
    //region Location Services Permission Section
    //region RequestLocationPermission
    public void RequestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            showExplanation("Permission Needed", "Rationale", android.Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    //endregion
    //region showExplanation
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
    //endregion
    //region requestPermission
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
    //endregion
    //region onRequestPermissionResult
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        gMap.setMyLocationEnabled(true);
                        Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
            default:
        }
    }
    //endregion
    //endregion

    //region SetCameraFragment
    private void SetCameraFragment()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // your code using Camera API here - is between 1-20
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // your code using Camera2 API here - is api 21 or higher
        }

    }
    //endregion

    //region Dialog Box Sample
    public void ShowDialog()
    {
        // Show rationale and request permission.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties
        builder.setTitle("Need Location Permission!");
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //endregion
}
