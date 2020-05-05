package cr.ac.ucr.ecci.cql.campus20.foro_general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import cr.ac.ucr.ecci.cql.campus20.MainActivity;
import cr.ac.ucr.ecci.cql.campus20.R;

public class MainForoGeneral extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    /**
     * Método que se invoca al iniciar la actividad general del módulo Foro General,
     * muestra una pequeña lista de temas sugeridos y un botón flotante para agregar una pregunta (pantalla en blanco)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_foro_general);

        // Boton flotante de Agregar Preguntas
        FloatingActionButton buttonAgregarPreguntas = findViewById(R.id.buttonAgregarPreguntas);

        // Asocia evento clic al boton
        buttonAgregarPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPregunta();
            }
        });

        // Llenado de la lista de temas sugeridos
        rellenarTemasSugeridos();

        // Este código debería ser una llamado y no el código en sí

        //Codigo que maneja la navegacion de izquierda a derecha
        dl = (DrawerLayout)findViewById(R.id.activity_main_foro_general);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Se lanza cada actividad, dependiendo de la selección del usuario
        nv = (NavigationView)findViewById(R.id.nv_foro);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.home_foro:
                        Intent intentForo = new Intent(MainForoGeneral.this, MainForoGeneral.class);
                        startActivity(intentForo);
                        break;
                    case R.id.temas_foro:
                        Intent intent2Foro = new Intent(MainForoGeneral.this, ForoGeneralVerTemas.class);
                        startActivity(intent2Foro);
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });
    }

    /**
     * Método que crea un Intent para ir a la actividad de CrearPregunta
     */
    private void crearPregunta() {
        Intent intent = new Intent(this, CrearPreguntaForoGeneral.class);
        // Llamada a la actividad de crear pregunta
        startActivity(intent);
    }


    /**
     * Método que realiza un llenado de la lista de temas sugeridos
     */
    private void rellenarTemasSugeridos(){
        // Codigo que realiza el llenado de la lista de temas recomendados
        ListView listaTemasRecomendados = findViewById(R.id.listaTemasSugeridos);
        ForoGeneralVerTemas temas = new ForoGeneralVerTemas();
        AdaptadorTemas adaptadorTemas = new AdaptadorTemas(this, temas.getTemasSugeridos());
        listaTemasRecomendados.setAdapter(adaptadorTemas);
    }

    /**
     * Este método realiza una actividad cuando un objeto específico de la lista es seleccionado
     * @param item funciona para indicar el objeto de la lista que se selecionó
     * @return un booleano, ya que aún no se ha implementado el llamado a la base de datos
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
