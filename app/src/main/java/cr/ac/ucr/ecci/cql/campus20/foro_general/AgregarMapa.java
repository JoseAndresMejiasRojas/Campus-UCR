package cr.ac.ucr.ecci.cql.campus20.foro_general;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;

import java.util.List;

import cr.ac.ucr.ecci.cql.campus20.R;

//MapboxMap.OnMapClickListener,
public class AgregarMapa extends AppCompatActivity implements PermissionsListener, MapboxMap.OnMapClickListener {

    private MapboxMap mapboxMap;
    double lat;
    double lon;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    //En la UCR
    private static double latInicial = 9.9373255;
    private static double lonInicial = -84.0515752;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_mapa);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.MAPBOX_ACCESS_TOKEN));


        // Create supportMapFragment
        SupportMapFragment mapFragment;
        if (savedInstanceState == null) {

            // Create fragment
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Build mapboxMap
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(latInicial, lonInicial))
                    .zoom(15)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }


        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    AgregarMapa.this.mapboxMap = mapboxMap;
                    AgregarMapa.this.mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                            habilitarPermisos(style);

                            AgregarMapa.this.mapboxMap.addOnMapClickListener(AgregarMapa.this);

                            FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionRespuestas);
                            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (locationComponent.isLocationComponentEnabled()) {
                                        locationComponent.setLocationComponentEnabled(false);
                                        floatingActionButton.setImageResource(R.drawable.closed);
                                    } else {
                                        locationComponent.setLocationComponentEnabled(true);
                                        floatingActionButton.setImageResource(R.drawable.open);
                                    }
                                }
                            });

                        }
                    });
                }
            });
        }
    }

    //Habilitar permisos de ubicación
    @SuppressWarnings( {"MissingPermission"})
    private void habilitarPermisos(@NonNull Style loadedMapStyle){
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this))
        {
            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }
        else
        {
            permissionsManager = new PermissionsManager((PermissionsListener) this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain)
    {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted)
    {
        if (granted) {
            habilitarPermisos(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        agregarMarcador(point.getLatitude(), point.getLongitude());
        return true;
    }

    private void agregarMarcador(double lat, double lon){

        //TODO: Borrar anteriores
        mapboxMap.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lon))
        .title("Marcador"));

        this.lat = lat;
        this.lon = lon;
    }
}