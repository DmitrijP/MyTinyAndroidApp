package de.dmitrij.patuk.ourapp.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;
import de.dmitrij.patuk.ourapp.R;
import de.dmitrij.patuk.ourapp.navigation.MyNavDestinations;
import de.dmitrij.patuk.ourapp.navigation.MyNavigator;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements MyNavigator {

    private NavController navController;
    private BottomNavigationView  bottomNav;
    private AppBarLayout topAppBar;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        topAppBar = findViewById(R.id.main_activity_app_bar);
        toolBar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolBar);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(this::onNavigationItemSelected);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(toolBar, navController);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            handleBottomViewVisibility(navDestination, bundle);
        });
        navigate(MyNavDestinations.LOGIN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void handleBottomViewVisibility(NavDestination destination, Bundle arguments) {
        if(destination.getId() == R.id.loginFragment){
            bottomNav.setVisibility(View.GONE);
            topAppBar.setVisibility(View.GONE);
        }else{
            bottomNav.setVisibility(View.VISIBLE);
            topAppBar.setVisibility(View.VISIBLE);
        }
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.home_menu_item){
            navigate(MyNavDestinations.HOME);
            return true;
        }
        if(menuItem.getItemId() == R.id.capture_menu_item){
            navigate(MyNavDestinations.CAPTURE);
            return true;
        }
        if(menuItem.getItemId() == R.id.settings_menu_item){
            navigate(MyNavDestinations.SETTINGS);
            return true;
        }
        return false;
    }

    @Override
    public void navigate(MyNavDestinations destination) {
        switch (destination){
            case LOGIN:
                navController.navigate(R.id.action_navigate_to_login);
                break;
            case HOME:
                navController.navigate(R.id.action_navigate_to_home);
                break;
            case SETTINGS:
                navController.navigate(R.id.action_navigate_to_settings);
                break;
            case CAPTURE:
                navController.navigate(R.id.action_navigate_to_capture);
                break;
        }
    }
}