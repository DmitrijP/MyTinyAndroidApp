# Navigation mit LoginView

In diesem Tutoriel bauen wir eine LoginView mit Navigation die es uns erlaubt die View im Vollbild darzustellen.
Dabei werden die Bottom und Top Bar ausgeblendet. Weiterhin kümmern wir uns um richtiges Verhalten der Navigationshistorie.
So das der Zurück Button wie erwartet funktioniert.

Nützliche Links:
Navigation: https://developer.android.com/guide/navigation
M3 Material: https://m3.material.io/


## Navigator Interface
Als erstes benötigen wir ein Interface das die Navigation kapselt und ein Enum der die Ziele unserer Navigation enthalten soll.
```java 
public enum MyNavDestinations {
    LOGIN,
    HOME,
    SETTINGS,
    CAPTURE
}

public interface MyNavigator {
    void navigate(MyNavDestinations destination);
}
```

## Navigation XML

Als nächstes gehen wir in den `main_nav_graph.xml`. Hier müssen wir als erstes die `app:startDestination="@id/homeFragment"` ändern. 
Dadurch wird die `HomeView` als Einstiegspunkt in den Graphen eingerichtet und hat kein Zurück Button, da dies die Erste view ist. 
Das werden wir später ausnutzen.
```xml 
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main_nav_graph"
    app:startDestination="@id/homeFragment">
    
    <fragment
        android:id="@+id/homeFragment"
        android:name="de.dmitrij.patuk.ourapp.ui.home.HomeFragment"
        android:label="Home"
        tools:layout="@layout/fragment_home" />
    <fragment
        android:id="@+id/loginFragment"
        tools:layout="@layout/fragment_login"
        android:name="de.dmitrij.patuk.ourapp.ui.login.LoginFragment"
        android:label="Login" />
</navigation>
```

Jetzt definieren wir mehrere Actions die von keinen Fragmenten abhängen. Das erlaubt es uns diese von fast jedem Ausgangspunkt aufzurufen.
Solche Actions nutzen wir für globale navigation. Die Action die zum `LoginFragment` und `HomeFragment` navigieren versehen wir mit `app:popUpTo="@id/main_nav_graph"`.
Dadurch sagen wir das wir zum Anfang des Graphen navigieren also das `HomeFragment`. `app:popUpToInclusive="true"` Entfernt auch das `HomeFragment` und fügt das `LoginFragment` danach als Root ein.
Dies erlaubt es uns entweder das `LoginFragment` oder das `HomeFragment` als Einstiegspunkt zu setzen und somit die Zurück Navigation zu unterbinden.
```xml
<navigation>
    <action
        android:id="@+id/action_navigate_to_login"
        app:destination="@id/loginFragment"
        app:popUpTo="@id/main_nav_graph"
        app:popUpToInclusive="true"
        />
    
    <action
        android:id="@+id/action_navigate_to_home"
        app:destination="@id/homeFragment"
        app:popUpTo="@id/main_nav_graph"
        app:popUpToInclusive="true"
        />
</navigation>
```

Die letzten Actions und Fragmente sind die zwei anderen Elemente der Bottom Navbar die wir von überall aus erreichen wollen, da diese Buttons überall in der App sichtbar sind.
```xml
<navigation>
    <action
        android:id="@+id/action_navigate_to_capture"
        app:destination="@id/captureFragment"
        />
    <action
        android:id="@+id/action_navigate_to_settings"
        app:destination="@id/settingsFragment"
        />
    
    <fragment
    android:id="@+id/settingsFragment"
    android:name="de.dmitrij.patuk.ourapp.ui.settings.SettingsFragment"
    android:label="Settings"
    tools:layout="@layout/fragment_settings" />
    <fragment
    android:id="@+id/captureFragment"
    android:name="de.dmitrij.patuk.ourapp.ui.capture.CaptureFragment"
    android:label="Capture"
    tools:layout="@layout/fragment_capture" />
</navigation>
```

## Interface auf MainActivity anwenden

Jetzt implementieren wir die Interface in der MainActivity. Es wird unsere bisherige navigation in einer navigate() Methode kapseln.
Dadurch erschaffen wir einen Möglichkeit von jedem Fragment aus zu navigieren in dem wir ``((MyNavigator)reuireActivity()).navigate(DESTINATION)`` aufrufen.
Da wir eine Single Activity App haben können wir uns sicher sein das unsere Activity immer die `MainActivity` ist
```java

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements MyNavigator {
   //... Klasse gekürzt
    
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
```
Somit ist unsere Navigation vollständig.

## MainActivity BottomNavBar

```java
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
```

In der MainActivity müssen wir noch die `TopAppBar`, `ToolBar` und `BottomNavigationView` konfigurieren.
Dazu selectieren wir die `TopAppBar` und `ToolBar` per Id. Setzen die SupportActionBar um auch ältere Devices zu unterstützen.
Rufen `NavigationUI.setupWithNavController(toolBar, navController);` Auf um die Toolbar mit dem NavController zu verbinden.
```java
topAppBar = findViewById(R.id.main_activity_app_bar);
toolBar = findViewById(R.id.main_activity_toolbar);
setSupportActionBar(toolBar);
```

Mit dem EventListener können wir auf Änderungen der Navigationsziele reagieren und somit die BottomBar und TopBar ein und ausblenden.
```java
navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
    handleBottomViewVisibility(navDestination, bundle);
});

private void handleBottomViewVisibility(NavDestination destination, Bundle arguments) {
    if(destination.getId() == R.id.loginFragment){
        bottomNav.setVisibility(View.GONE);
        topAppBar.setVisibility(View.GONE);
    }else{
        bottomNav.setVisibility(View.VISIBLE);
        topAppBar.setVisibility(View.VISIBLE);
    }
}
```
Als letztes navigieren wir zum `LoginFragment` mit `navigate(MyNavDestinations.LOGIN);`. Dadurch wird das `HomeFragment` entfernt und wir sehen nur das Login.
Der Zurück Button beendet die App.

Es fehlt noch die Funktion zu aktualisieren die es uns erlaubt bei Klick auf die BottomBar zu navigieren.
```java
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
```