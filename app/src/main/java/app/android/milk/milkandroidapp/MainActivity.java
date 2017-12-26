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

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static int mainFragmentLayout;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private boolean itemFlag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Create Fragment View when Item is Clicked
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //clear backStack
            itemFlag = false;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    fragment = new HomeFragment();
                    itemFlag = true;
                    break;
                case R.id.navigation_map:
                    mTextMessage.setText(R.string.title_map);
                    fragment = new MapFragment();
                    itemFlag = true;
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

}
