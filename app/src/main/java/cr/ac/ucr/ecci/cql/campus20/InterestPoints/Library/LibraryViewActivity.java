package cr.ac.ucr.ecci.cql.campus20.InterestPoints.Library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cr.ac.ucr.ecci.cql.campus20.InterestPoints.IPModel.Library;
import cr.ac.ucr.ecci.cql.campus20.InterestPoints.ListAdapter;
import cr.ac.ucr.ecci.cql.campus20.R;

public class LibraryViewActivity extends AppCompatActivity implements ListAdapter.ListAdapterOnClickHandler {

    String libraryName;
    Library library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_view);

        Intent intentLibrary = getIntent();
        libraryName = intentLibrary.getStringExtra(Intent.EXTRA_TEXT);

        TextView tittle = findViewById(R.id.libraryName);
        tittle.setText(libraryName);

        library = intentLibrary.getParcelableExtra("place");


        TextView desc = findViewById(R.id.descripcion);
        desc.setText(library.description);

        TextView horario = findViewById(R.id.horario);
        horario.setText(library.horario);

        ImageView wifi = findViewById(R.id.imageWiFi);
        AppCompatCheckBox wifiCheck = findViewById(R.id.checkboxWiFi);

        wifi.setImageResource(R.drawable.icon_wifi);
        wifiCheck.setChecked(library.wifi);
        wifiCheck.setEnabled(false);

    }

    //    /**
//     * EFE: send the user to the location in maps
//     * REQ:
//     * view: send by the button that calls this method
//     * latitude : latitude of the point that the user wants to go.
//     * longitude: longitude of the point that the user wants to go.
//     * MOD: ---
//     * */
//    public void goTo(View view) {
//        Intent intent = new Intent(this, Map.class);
//        startActivity(intent);
//    }

    @Override
    public void onClick(String title) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}