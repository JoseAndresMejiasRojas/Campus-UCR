package cr.ac.ucr.ecci.cql.campus20.foro_general;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cr.ac.ucr.ecci.cql.campus20.CampusBD;
import cr.ac.ucr.ecci.cql.campus20.ConfiguracionActivity;
import cr.ac.ucr.ecci.cql.campus20.FirebaseBD;
import cr.ac.ucr.ecci.cql.campus20.LoginActivity;
import cr.ac.ucr.ecci.cql.campus20.CampusBD;
import cr.ac.ucr.ecci.cql.campus20.R;
import cr.ac.ucr.ecci.cql.campus20.foro_general.ViewModels.FavoritoViewModel;
import cr.ac.ucr.ecci.cql.campus20.foro_general.ViewModels.TemaViewModel;
import cr.ac.ucr.ecci.cql.campus20.foro_general.models.Favorito;
import cr.ac.ucr.ecci.cql.campus20.foro_general.models.Tema;

public class ForoGeneralVerTemas extends AppCompatActivity {

    private EditText search;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private AdaptadorTemas adapter;

    private TemaViewModel mTemaViewModel;
    private FavoritoViewModel mFavoritoViewModel;

    private RecyclerView recyclerView;
    private List<Integer> idList;

    // Instancia de Firebase para el foro general y sus hijos
    ForoGeneralFirebaseDatabase databaseReference;
    DatabaseReference temasDatabaseReference;
    List<Tema> temasLocales;
    List<Favorito> favoritosLocales;


    /**
     * Método que se invoca al iniciar la actividad temas en el foro general,
     * muestra una lista de temas, con la imagen y una descripción de los mismos     *
     * @param savedInstanceState almacena información del estado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro_general_ver_temas);
        idList = new ArrayList<>();

        temasLocales = new ArrayList<>();
        favoritosLocales = new ArrayList<>();

        // Se instancia el firebaseReference
        databaseReference = new ForoGeneralFirebaseDatabase();

        this.databaseReference.getTemasRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Se borra la lista
                ForoGeneralVerTemas.this.temasLocales.clear();
                // Se recorre el snapshot para sacar los datos
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    // Esto podría producir NullPointerException
                    int id = Objects.requireNonNull(ds.child("id").getValue(Integer.class));
                    String titulo = ds.child("titulo").getValue(String.class);
                    String description = ds.child("description").getValue(String.class);
                    int contUsers = Objects.requireNonNull(ds.child("contadorUsuarios").getValue(Integer.class));
                    int imagen = Objects.requireNonNull(ds.child("imagen").getValue(Integer.class));

                    // Se crea el tema
                    Tema temita = new Tema(id, titulo, description, contUsers, imagen);
                    ForoGeneralVerTemas.this.temasLocales.add(temita);
                }
                adapter.setTemas(ForoGeneralVerTemas.this.temasLocales);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", databaseError.toException());
            }
        });


        busquedaFiltrada();
        iniciarRecycler();
        iniciarAdapter();
        llenar();
        escuchar();


        //Codigo que maneja la navegacion de izquierda a derecha
        dl = (DrawerLayout)findViewById(R.id.activity_main_foro_general_ver_temas);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

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
                        startActivity(new Intent(ForoGeneralVerTemas.this, MainForoGeneral.class));
                        break;
                    case R.id.temas_foro:
                        startActivity(new Intent(ForoGeneralVerTemas.this, ForoGeneralVerTemas.class));
                        break;
                    case R.id.mis_preguntas_foro:
                        Intent intent = new Intent(ForoGeneralVerTemas.this, ForoGeneralVerMisPreguntas.class);
                        intent.putExtra("nombreUsuario", ForoGeneralVerTemas.this.databaseReference.obtenerUsuario());
                        // Llamada a la actividad de crear pregunta
                        startActivity(intent);
                        break;
                    case R.id.pref_foro:
                        startActivity(new Intent(ForoGeneralVerTemas.this, ConfiguracionActivity.class));
                        break;
                    case R.id.logout_foro:
                        CampusBD login = new FirebaseBD();
                        login.cerrarSesion();

                        ActivityCompat.finishAffinity(ForoGeneralVerTemas.this);
                        startActivity(new Intent(ForoGeneralVerTemas.this, LoginActivity.class));
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });

    }

    private void iniciarAdapter(){
        //instanciando el adapter
        this.adapter = new AdaptadorTemas(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Metodo para obtener la lista de temas y de favoritos desde los viewmodel
     */
    private void llenar(){
        mTemaViewModel = new ViewModelProvider(this).get(TemaViewModel.class);
        mFavoritoViewModel = new ViewModelProvider(this).get(FavoritoViewModel.class);

        // Obtiene el cambio en la lista de temas, directo desde el ViewModel
        mTemaViewModel.getAllTemas().observe(this, new Observer<List<Tema>>() {
            @Override
            public void onChanged(List<Tema> temas) {
//                if (temas != null)
//                    adapter.setTemas(temas);        // Se llama al método del adapter
            }
        });

        // Obtiene el cambio en la lista de favoritos, directo desde el ViewModel
        mFavoritoViewModel.getAllFavoritos().observe(this, new Observer<List<Favorito>>() {
            @Override
            public void onChanged( @Nullable final List<Favorito> favoritos) {
                //adapter.setFavoritos(favoritos);    // Se llama al método del adapter
            }
        });

        // Prueba con el Favorito -> usuario
        this.databaseReference.getFavoritosRef().child(this.databaseReference.obtenerUsuario())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Se borra la lista
                ForoGeneralVerTemas.this.favoritosLocales.clear();

                // Chequea si existe el snapshot y si el usuario tiene favoritos almacenados en Firebase
                if (dataSnapshot.exists()) //&& dataSnapshot.hasChild(ForoGeneralVerTemas.this.databaseReference.obtenerUsuario()))
                {
                    // Se recorre el snapshot para sacar los datos
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        // Esto podría producir NullPointerException
                        int id = Objects.requireNonNull(ds.child("idTema").getValue(Integer.class));
                        String nombreUsuario = ds.child("nombreUsuario").getValue(String.class);

                        // Se crea el tema
                        Favorito fav = new Favorito(id, nombreUsuario);
                        ForoGeneralVerTemas.this.favoritosLocales.add(fav);
                    }
                    adapter.setFavoritos(ForoGeneralVerTemas.this.favoritosLocales);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("FIREBASE", "Fallo al leer favoritos", databaseError.toException());
            }
        });
    }

    private void iniciarRecycler() {
        // Instanciación del RecyclewView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Metodo para escuchar los clicks sobre los items de la lista temas y los favoritos
     */
    private void escuchar(){
        // Recepción de los clicks del adapter
        adapter.setOnItemClickListener(new AdaptadorTemas.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                //conseguir id del tema seleccionado
                int idTemaSeleccionado;
                String temaSeleccionado;
                if(idList.size() != 0){
                    idTemaSeleccionado = idList.get(position);
                }
                else{
                    idTemaSeleccionado = ForoGeneralVerTemas.this.temasLocales.get(position).getId();
                }
                int counter = ForoGeneralVerTemas.this.temasLocales.size();
                int i = 0 ;
                int fin = 0;
                Tema result = new Tema(0 , "", "", 0,0); //tema comodin
                while (i < counter && fin ==0) {
                    if (ForoGeneralVerTemas.this.temasLocales.get(i).id == idTemaSeleccionado) {
                        result = ForoGeneralVerTemas.this.temasLocales.get(i);
                        fin = 1;
                    }
                    i++;
                }


                String nombreTema = ForoGeneralVerTemas.this.temasLocales.get(position).getTitulo();
                int idTemaSek = ForoGeneralVerTemas.this.temasLocales.get(position).getId();

                temaSeleccionado = result.getTitulo();
                //int idTemaSeleccionado = ForoGeneralVerTemas.this.temasLocales.get(position).getId();
                //String temaSeleccionado = ForoGeneralVerTemas.this.temasLocales.get(position).getTitulo();

                // Llamada a la actividad de ver preguntas
                Intent intent = new Intent(getApplicationContext(), ForoGeneralVerPreguntas.class);
                intent.putExtra("idTemaSeleccionado", idTemaSeleccionado);
                intent.putExtra("temaSeleccionado", temaSeleccionado);
                startActivity(intent);
            }

            @Override
            public void onHeartClick(boolean check, int position) {
                //conseguir id del tema seleccionado
                int idTema;
                String nombreTema;
                if(idList.size() != 0){
                    idTema = idList.get(position);
                }
                else{
                    idTema = ForoGeneralVerTemas.this.temasLocales.get(position).getId();
                }
                int counter = ForoGeneralVerTemas.this.temasLocales.size();
                int i = 0 ;
                int fin = 0;
                Tema result = new Tema(0 , "", "", 0,0); //tema comodin
                while (i < counter && fin ==0) {
                    if (ForoGeneralVerTemas.this.temasLocales.get(i).id == idTema) {
                        result = ForoGeneralVerTemas.this.temasLocales.get(i);
                        fin = 1;
                    }
                    i++;
                }
                nombreTema = result.getTitulo();

                //String nombreTema = mTemaViewModel.getAllTemas().getValue().get(position).getTitulo();
                //int idTema = mTemaViewModel.getAllTemas().getValue().get(position).getId();

                //String nombreTema = ForoGeneralVerTemas.this.temasLocales.get(position).getTitulo();
                //int idTema = ForoGeneralVerTemas.this.temasLocales.get(position).getId();

                if (check) {
                    // Se da un mensaje al usuario
                    Toast.makeText(ForoGeneralVerTemas.this, "Tema " + nombreTema +
                            " añadido a Favoritos", Toast.LENGTH_SHORT).show();

                    // Se inserta el tema como Favorito
                    añadirTemaFavorito(idTema, ForoGeneralVerTemas.this.databaseReference.obtenerUsuario());

                    // Se inserta el tema como Favorito en Firebase
                    añadirTemaFavoritoFirebase(idTema, ForoGeneralVerTemas.this.databaseReference.obtenerUsuario());

                } else {
                    // Se da un mensaje al usuario
                    Toast.makeText(ForoGeneralVerTemas.this, "Tema " + nombreTema +
                            " quitado de Favoritos", Toast.LENGTH_SHORT).show();

                    // Se elimina al tema de la lista de Favoritos
                    eliminarTemaFavorito(idTema, ForoGeneralVerTemas.this.databaseReference.obtenerUsuario());

                    // Se elimina el tema como Favorito en Firebase
                    eliminarTemaFavoritoFirebase(idTema, ForoGeneralVerTemas.this.databaseReference.obtenerUsuario());
                }
            }
            @Override
            public void onLongClick(View view, int position){
                Toast.makeText(ForoGeneralVerTemas.this, "HIII", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * inicia un cruadro de texto y espera los cambios, en caso de haber cambios, llama al metodo filtrar
     */
    private void busquedaFiltrada() {
        this.search = findViewById(R.id.search);
        this.search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
        this.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());

            }
        });
    }


    /**
     * Metodo que obtine el texto recibido del cuadro de texto, la lista de temas del viewmodel y
     * lo envia al adapter para realizar el filtrado tanto de temas como de favoritos
     * @param texto string recibido del cuadro de texto
     */
    private void filtrar(String texto) {
        // Obtiene el cambio en la lista de temas, directo desde el ViewModel
        mTemaViewModel.getAllTemas().observe(this, new Observer<List<Tema>>() {
            @Override
            public void onChanged(List<Tema> temas) {
                if (temas != null)
                    adapter.filterTemas(temas, texto.toLowerCase(), idList);        // Se llama al método del adapter
            }
        });


        // Obtiene el cambio en la lista de favoritos, directo desde el ViewModel
        mFavoritoViewModel.getAllFavoritos().observe(this, new Observer<List<Favorito>>() {
            @Override
            public void onChanged( @Nullable final List<Favorito> favoritos) {
                adapter.filterFavoritos(favoritos);    // Se llama al método del adapter
            }
        });

    }


    /**
     * Método que se encarga de invocar el método para inserción del ViewModel para
     * la inserción de temas favoritos
     * @param identificadorTema, que es el identificador del tema que se va a insertar
     * como favorito
     */
    public void añadirTemaFavorito(int identificadorTema, String nombreUsuario)
    {
        Favorito fav = new Favorito(identificadorTema, nombreUsuario);
        mFavoritoViewModel.insert(fav);
    }


    public void añadirTemaFavoritoFirebase(int identificadorTema, String nombreUsuario)
    {
        Favorito fav = new Favorito(identificadorTema, nombreUsuario);
        this.databaseReference.getFavoritosRef().child(nombreUsuario).child(Integer.toString(identificadorTema)).setValue(fav);
    }

    /**
     * Método que se encarga de invocar el método para borrado de un tema que está
     * añadido como favorito
     * @param identificadorTema, que es el identificador del tema que se va a eliminar
     */
    public void eliminarTemaFavorito(int identificadorTema, String nombreUsuario)
    {
        mFavoritoViewModel.deleteOneFavorito(identificadorTema, nombreUsuario);
    }


    public void eliminarTemaFavoritoFirebase(int identificadorTema, String nombreUsuario)
    {
        this.databaseReference.getFavoritosRef().child(nombreUsuario).child(Integer.toString(identificadorTema)).setValue(null);
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

