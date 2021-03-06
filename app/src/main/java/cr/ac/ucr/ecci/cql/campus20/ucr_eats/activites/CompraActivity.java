package cr.ac.ucr.ecci.cql.campus20.ucr_eats.activites;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import cr.ac.ucr.ecci.cql.campus20.FirebaseBD;
import cr.ac.ucr.ecci.cql.campus20.CampusBD;
import cr.ac.ucr.ecci.cql.campus20.R;
import cr.ac.ucr.ecci.cql.campus20.ucr_eats.MainUcrEats;
import cr.ac.ucr.ecci.cql.campus20.ucr_eats.models.Meal;
import cr.ac.ucr.ecci.cql.campus20.ucr_eats.models.Order;
import cr.ac.ucr.ecci.cql.campus20.ucr_eats.models.OrderStatus;
import cr.ac.ucr.ecci.cql.campus20.ucr_eats.services.NotificacionPedidoService;

public class CompraActivity extends AppCompatActivity implements PermissionsListener
{
    private Meal meal;
    public static final String PATH_PEDIDOS = "ucr_eats/pedidos";
    private String currentRestaurant;
    private String restLatitude;
    private String restLongitude;

    private double latitude = 0.0;
    private double longitude = 0.0;

    // Referencia: https://docs.mapbox.com/android/maps/examples/show-a-users-location-on-a-fragment/
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    private boolean customLocation = false;

    private static final int CODIGO_RESULTADO = 0;

    private NotificacionPedidoService mService;
    Intent servicioIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        Toast.makeText(this, "Recordá que si no lo especificás, se usará tu ubicación actual", Toast.LENGTH_LONG).show();


        meal = getIntent().getParcelableExtra(MealsActivity.MEAL_KEY);
        currentRestaurant = getIntent().getStringExtra(MealsActivity.NOMBRE_SODA_KEY);
        restLatitude = getIntent().getStringExtra(MealsActivity.LATITUDE_SODA_KEY);
        restLongitude = getIntent().getStringExtra(MealsActivity.LONGITUDE_SODA_KEY);
        TextView restaurant = findViewById(R.id.tituloCompra);
        restaurant.setText(currentRestaurant);

        TextView mealText = findViewById(R.id.platilloCompra);
        mealText.setText(meal.getName());

        Picasso picasso = new Picasso.Builder(this)
                .indicatorsEnabled(true)
                .loggingEnabled(true) //add other settings as needed
                .build();

        ImageView photo = findViewById(R.id.platilloImagenCompra);
        picasso.load(meal.getPhoto())
                .placeholder(R.drawable.soda_placeholder)
                .into(photo);

        Button buttonCompra = findViewById(R.id.buttonCompra);
        buttonCompra.setOnClickListener(v -> realizarCompra());

        // Map box fragment:

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // Create supportMapFragment
        SupportMapFragment mapFragment;
        if (savedInstanceState == null)
        {
            // Create fragment
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Build a Mapbox map
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(38.899895, -77.03401))
                    .zoom(16.5f)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.location_frag_container, mapFragment, "com.mapbox.map");
            transaction.commit();
        }
        else
        {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        if (mapFragment != null)
        {
            mapFragment.getMapAsync(mapboxMap -> {
                CompraActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.OUTDOORS, this::enableLocationComponent);
            });
        }
    }

    public void realizarCompra()
    {
        CampusBD campusBD = new FirebaseBD();

        String email = campusBD.obtenerCorreoActual();
        String username = email.substring(0, email.indexOf('@'));

        if(customLocation == false)
        {
            Location location = locationComponent.getLastKnownLocation();
            if(location != null)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        Order order = new Order(username, meal, currentRestaurant,Double.valueOf(restLatitude),Double.valueOf(restLongitude), Calendar.getInstance().getTime(), latitude, longitude, OrderStatus.PENDIENTE);
        String orderId = campusBD.obtenerIdUnicoPath(PATH_PEDIDOS);
        order.setIdOrder(orderId);

        campusBD.escribirDatos(PATH_PEDIDOS + "/" + orderId, order);

        Toast.makeText(this, "Se realizó el pedido exitosamente", Toast.LENGTH_LONG).show();

        // Servicio para detectar el estado de la orden
        mService = new NotificacionPedidoService();
        servicioIntent = new Intent(this, NotificacionPedidoService.class);
        servicioIntent.putExtra(NotificacionPedidoService.LLAVE_ID_ORDEN, order.getIdOrder());
        if (!isMyServiceRunning(mService.getClass())) {
            startService(servicioIntent);
        }

        Intent actividadIntent = new Intent(this, MainUcrEats.class);
        startActivity(actividadIntent);
        finish();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle)
    {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this))
        {

            // Get an instance of the LocationComponent.
            locationComponent = mapboxMap.getLocationComponent();

            // Activate the LocationComponent
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable the LocationComponent so that it's actually visible on the map
            locationComponent.setLocationComponentEnabled(true);

            // Set the LocationComponent's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the LocationComponent's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);

            mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                @Override
                public boolean onMapClick(@NonNull LatLng point)
                {
                    Intent intent = new Intent(getApplicationContext(), OrderLocationActivity.class);
                    startActivityForResult(intent, CODIGO_RESULTADO);
                    return true;
                }
            });
        }
        else
        {
            permissionsManager = new PermissionsManager(this);
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
        if (granted)
        {
            mapboxMap.getStyle(this::enableLocationComponent);
        }
        else
        {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_RESULTADO && resultCode == Activity.RESULT_OK)
        {
            latitude = data.getDoubleExtra(OrderLocationActivity.LATITUD_KEY, this.latitude);
            longitude = data.getDoubleExtra(OrderLocationActivity.LONGITUD_KEY, this.longitude);
            customLocation = true;
            Toast.makeText(this, "Ubicación personalizada guardada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        //stopService(servicioIntent);
        super.onDestroy();
    }

}