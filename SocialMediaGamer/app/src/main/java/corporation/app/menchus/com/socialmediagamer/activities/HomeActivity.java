package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.fragments.ChatsFragment;
import corporation.app.menchus.com.socialmediagamer.fragments.FiltersFragment;
import corporation.app.menchus.com.socialmediagamer.fragments.HomeFragment;
import corporation.app.menchus.com.socialmediagamer.fragments.ProfileFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.itemHome:
                            openFragment(new HomeFragment());
                            return true;
                        case R.id.itemFilters:
                            openFragment(new FiltersFragment());
                            return true;
                        case R.id.itemChat:
                            openFragment(new ChatsFragment());
                            return true;
                        case R.id.itemProfile:
                            openFragment(new ProfileFragment());
                            return true;
                    }
                    return true;
                }
            };
}
