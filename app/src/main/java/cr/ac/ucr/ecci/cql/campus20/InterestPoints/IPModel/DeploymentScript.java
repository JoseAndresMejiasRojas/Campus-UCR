package cr.ac.ucr.ecci.cql.campus20.InterestPoints.IPModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cr.ac.ucr.ecci.cql.campus20.InterestPoints.GeneralData;
import cr.ac.ucr.ecci.cql.campus20.InterestPoints.IPModel.RoomModel.ActivityInfo;
import cr.ac.ucr.ecci.cql.campus20.InterestPoints.IPModel.RoomModel.ActivityInfoDao;
import cr.ac.ucr.ecci.cql.campus20.InterestPoints.IPModel.RoomModel.IPRoomDatabase;
import cr.ac.ucr.ecci.cql.campus20.InterestPoints.Utilities.UtilDates;
import cr.ac.ucr.ecci.cql.campus20.R;

/**
 * @class DeploymentScript
 * @brief Enables the app to have a single point of database entities creation.
 * Here should be placed all methods with test data to be written to the database.
 */
public class DeploymentScript {

    /**
     * Enum representing the activity names inserted in the Room database.
     * */
    public enum ActivityNames {
        INTEREST_POINTS,
        COFFEE_SHOPS,
        FOOD_COURTS,
        PHOTOCOPIERS,
        FACULTIES,
        LIBRARIES,
        OFFICES
    }

    private FirebaseDB db;
    /**
     * Executes all the statements that create and populate the database.
     * Should be called once on application create in MainActivity.
     *
     * @param db Firebase database instance where the data will be inserted.
     */
    public void RunScript(FirebaseDB db, Context context) {
        this.db = db;
        createFaculties();
        createPlaces();
        createSchools();
        createComments();
        createCoffeShops();
        createLibrary();
        createOffice();
        createSoda();
        createPhotocopier();
        /*Saves the activity names in room database.*/
        AsyncTask.execute(new Runnable() {
            @Override
            public void run(){
                createActivityNames(context);
            }
        });
    }

    private void createActivityNames(Context context){
        IPRoomDatabase roomDatabase = Room.databaseBuilder(context, IPRoomDatabase.class, "IPRoomDatabase").build();
        ActivityInfoDao activityInfoDao = roomDatabase.activityInfoDao();
        String[] activityNames = {"Puntos de interés", "Cafeterías", "Sodas", "Fotocopiadoras", "Facultades", "Bibliotecas", "Oficinas"};
        for (int i = 0; i < activityNames.length; ++i) {
            activityInfoDao.insert(new ActivityInfo(i, activityNames[i]));
        }

    }

    private void createFaculties() {
        List<Faculty> list = new ArrayList<>();
        String[] falcuties = {"Artes", "Ciencias Agroalimentarias", "Ciencias Básicas", "Ciencias Económicas", "Ciencias Sociales", "Derecho",
                "Educación", "Farmacia", "Ingeniería", "Letras", "Medicina", "Microbiología", "Odontología"}; //Espacio vacío
        int[] Images = {R.drawable.artes512px, R.drawable.agro512px, R.drawable.ciencias512,
                R.drawable.economicas512px, R.drawable.sociales512p, R.drawable.derecho215px,
                R.drawable.educa512px, R.drawable.farmacia512px, R.drawable.ingenieria512px,
                R.drawable.letras512px, R.drawable.medicina512px, R.drawable.micro512px,
                R.drawable.odonto512px};

        for (int id = 0; id < falcuties.length; ++id) {
            list.add(new Faculty(id, falcuties[id], "", Images[id], "faculty"));
        }
        for (Faculty f : list) {
            db.insert("Faculty", f);
        }
        Log.d("faculties", "Faculties were inserted in database.");
    }

    private void createCoffeShops() {
        Coffe angar = new Coffe(0,  "Café Angar", "", R.drawable.coffeshop512px,9.9342365, -84.050532);
        db.insert("Coffe", angar);

        Coffe noventaYCinco = new Coffe(1, "95 grados", "", R.drawable.coffeshop512px,9.9342365, -84.050532);
        db.insert("Coffe", noventaYCinco);

        Coffe krakovia = new Coffe(2,  "Café Krakovia", "", R.drawable.coffeshop512px,9.9342365, -84.050532);
        db.insert("Coffe", krakovia);

        Coffe aroma = new Coffe(3, "Aroma y Sabor", "", R.drawable.coffeshop512px,9.9342365, -84.050532);
        db.insert("Coffe", aroma);

        Coffe musmanni = new Coffe(4,  "Musmanni San Pedro", "", R.drawable.pan512px,9.9342365, -84.050532);
        db.insert("Coffe", musmanni);

        Coffe rincon = new Coffe(5, "Café El Rincón de la Vieja", "", R.drawable.coffeshop512px,9.9342365, -84.050532);
        db.insert("Coffe", rincon);

        Coffe cafe_cacao = new Coffe(6, "Café & Cacao", "", R.drawable.coffeshop512px,9.9342365, -84.050532);
        db.insert("Coffe", cafe_cacao);

        Log.d("coffeShops", "CoffeShops were inserted in database.");
    }

    private void createPlaces() {
        List<Place> placesList = new ArrayList<>();
        String[] PlaceNames = {"Finca 1", "Finca 2", "Finca 3"};
        String[] PlaceDescriptions = {"Finca principal.", "Ciudad de la Investigación", "Deportivas"};
        String PlaceType = "Finca";
        int[] rating = {5, 3, 4};
        int floor = 0;
        for (int i = 0; i < PlaceNames.length; ++i) {
            placesList.add(new Place(i, PlaceNames[i], PlaceDescriptions[i], PlaceType, rating[i], floor));
        }
        for (Place p : placesList) {
            db.insert("Place", p);
        }
        Log.d("places", "Places were inserted in database.");
    }

    private void createSchools() {

        int[] FacultiesFK = {8, 8};

        School artes = new School(0, 0, 0, "Artes Dramáticas", "", R.drawable.artesdramaticas512px,9.9342365, -84.050532);
        db.insert("School", artes);
        School artes1 = new School(1, 0, 0, "Artes Plásticas", "", R.drawable.artesplasticas512px, 9.9363702, -84.0483954);
        db.insert("School", artes1);
        School artes2 = new School(2, 0, 0, "Artes Musicales", "", R.drawable.musica512px, 9.9374068, -84.0503629);
        db.insert("School", artes2);

        School agro = new School(3, 1, 0, "Agronomía", "", R.drawable.agronomia512px, 9.9386464, -84.0505298);
        db.insert("School", agro);
        School agro1 = new School(4, 1, 0, "Zootecnia", "", R.drawable.zootecnia512px, 9.9393851, -84.0488164);
        db.insert("School", agro1);
        School agro2 = new School(5, 1, 0, "Tecnología de Alimentos", "", R.drawable.alimentos512px, 9.9403089, -84.0481914);
        db.insert("School", agro2);

        School ciencias = new School(6, 2, 0, "Física", "", R.drawable.fisica512px, 9.9364933, -84.0523176);
        db.insert("School", ciencias);
        School ciencias1 = new School(7, 2, 0, "Geología", "", R.drawable.geologia512px, 9.9381168, -84.0535935);
        db.insert("School", ciencias1);
        School ciencias2 = new School(8, 2, 0, "Matemática", "", R.drawable.mate512px, 9.9364933, -84.0523176);
        db.insert("School", ciencias2);
        School ciencias3 = new School(9, 2, 0, "Química", "", R.drawable.quimica512px,  9.9372417, -84.0490453);
        db.insert("School", ciencias3);
        School ciencias4 = new School(10, 2, 0, "Biología", "", R.drawable.biolo512px, 9.9376465, -84.0516436);
        db.insert("School", ciencias4);

        School economicas = new School(11, 3, 0, "Administración de Negocios", "", R.drawable.admin512px, 9.9368941, -84.0518023);
        db.insert("School", economicas);
        School economicas1 = new School(12, 3, 0, "Administración Pública", "", R.drawable.administracion, 9.9370248, -84.0514024);
        db.insert("School", economicas1);
        School economicas2 = new School(13, 3, 0, "Economía", "", R.drawable.economicas, 9.9372525, -84.0519284);
        db.insert("School", economicas2);
        School economicas3 = new School(14, 3, 0, "Estadística", "", R.drawable.estadisticas512px, 9.9369581, -84.0514777);
        db.insert("School", economicas3);

        School sociales = new School(15, 4, 1, "Psicología", "", R.drawable.psico512px, 9.9376971, -84.0441797);
        db.insert("School", sociales);
        School sociales1 = new School(16, 4, 1, "Ciencias Políticas", "", R.drawable.policitos512px,  9.937377, -84.0444508);
        db.insert("School", sociales1);
        School sociales2 = new School(17, 4, 1, "Comunicación Colectiva", "", R.drawable.comunicacion512px, 9.9375567, -84.0441602);
        db.insert("School", sociales2);
        School sociales3 = new School(18, 4, 1, "Trabajo Social", "", R.drawable.ts512px,  9.9374072, -84.0445572);
        db.insert("School", sociales3);
        School sociales4 = new School(19, 4, 1, "Historia", "", R.drawable.historia512px,  9.9376551, -84.0444486);
        db.insert("School", sociales4);
        School sociales5 = new School(20, 4, 1, "Geografía", "", R.drawable.geografia512px, 9.9373135, -84.0447285);
        db.insert("School", sociales5);
        School sociales6 = new School(21, 4, 1, "Antropología", "", R.drawable.antropologia512px, 9.9376218, -84.0447799);
        db.insert("School", sociales6);
        School sociales7 = new School(22, 4, 1, "Sociología", "", R.drawable.socio512px, 9.9367071, -84.0529196);
        db.insert("School", sociales7);

        School derecho = new School(23, 5, 0, "Derecho", "", R.drawable.derecho512px,9.9367365, -84.0535668);
        db.insert("School", derecho);

        School educacion = new School(24, 6, 0, "Formación Docente", "", R.drawable.docente512px, 9.9367365, -84.0535668);
        db.insert("School", educacion);
        School educacion1 = new School(25, 6, 0, "Orientación y Educación Especial", "", R.drawable.orientacion512px, 9.936537, -84.0521004);
        db.insert("School", educacion1);
        School educacion2 = new School(26, 6, 0, "Bibliotecología y Ciencias de la Información", "", R.drawable.biblio512px, 9.9383532, -84.0556187);
        db.insert("School", educacion2);
        School educacion3 = new School(27, 6, 3, "Educación Física y Deportes", "", R.drawable.edufi512px, 9.9439988, -84.0474695);
        db.insert("School", educacion3);
        School educacion4 = new School(28, 6, 0, "Administración Educativa", "", R.drawable.admineduca512px, 9.9361541, -84.0509078);
        db.insert("School", educacion4);

        School farmacia = new School(29, 7, 0, "Farmacia", "", R.drawable.farmacos512px, 9.938875, -84.0521707);
        db.insert("School", farmacia);

        School ingenieria = new School(30, 8, 1, "Ingeniería Civil", "", R.drawable.civil512px,  9.9377147, -84.0439456);
        db.insert("School", ingenieria);
        School ingenieria1 = new School(31, 8, 1, "Ingeniería Eléctrica", "", R.drawable.electrica512px, 9.936884, -84.0442343);
        db.insert("School", ingenieria1);
        School ingenieria2 = new School(32, 8, 1, "Ingeniería Industrial", "", R.drawable.industrial512px, 9.9376215, -84.0462053);
        db.insert("School", ingenieria2);
        School ingenieria3 = new School(33, 8, 1, "Ingeniería Mecánica", "", R.drawable.mecanica512px, 9.9367091, -84.0440834);
        db.insert("School", ingenieria3);
        School ingenieria4 = new School(34, 8, 1, "Ingeniería Química", "", R.drawable.ingquimica512px, 9.9372878, -84.0501568);
        db.insert("School", ingenieria4);
        School ingenieria5 = new School(35, 8, 0, "Arquitectura", "", R.drawable.arqui512px, 9.934584, -84.0547457);
        db.insert("School", ingenieria5);
        School ingenieria6 = new School(36, 8, 0, "Computación e Informática", "", R.drawable.compu512px, 9.9379246, -84.0541789);
        db.insert("School", ingenieria6);
        School ingenieria7 = new School(37, 8, 1, "Ingeniería de Biosistemas", "", R.drawable.biosistemas512px, 9.9379246, -84.0541789);
        db.insert("School", ingenieria7);
        School ingenieria8 = new School(38, 8, 1, "Ingeniería Topográfica", "", R.drawable.topo512px, 9.9376555, -84.0463562);
        db.insert("School", ingenieria8);

        School letras = new School(39, 9, 0, "Filología, Lingüistica y Literatura", "", R.drawable.filologia512px, 9.9384127, -84.0528675);
        db.insert("School", letras);
        School letras2 = new School(40, 9, 0, "Filosofía", "", R.drawable.filo512px, 9.9384127, -84.0528675);
        db.insert("School", letras2);
        School letras3 = new School(41, 9, 0, "Lenguas Modernas", "", R.drawable.lenguas512px, 9.9384127, -84.0528675);
        db.insert("School", letras3);

        School medicina = new School(42, 10, 0, "Enfermería", "", R.drawable.enfermeria512px, 9.9230603, -84.052573);
        db.insert("School", medicina);
        School medicina1 = new School(43, 10, 0, "Medicina", "", R.drawable.medicinas512px, 9.9387007, -84.0529767);
        db.insert("School", medicina1);
        School medicina2 = new School(44, 10, 0, "Nutrición", "", R.drawable.nutricion512px, 9.9390325, -84.0470175);
        db.insert("School", medicina2);
        School medicina3 = new School(45, 10, 0, "Tecnologías de la Salud", "", R.drawable.alimentos512px, 9.9384571, -84.0560422);
        db.insert("School", medicina3);
        School medicina4 = new School(46, 10, 0, "Salud Púbilca", "", R.drawable.saludpublica512px, 9.9388922, -84.0479994);
        db.insert("School", medicina4);

        School micro = new School(47, 11, 0, "Microbiología", "", R.drawable.micro512px, 9.9379464, -84.0514961);
        db.insert("School", micro);

        School odonto = new School(48, 12, 0, "Odontología", "", R.drawable.odonto512px, 9.943473, -84.0469052);
        db.insert("School", odonto);

        Log.d("places", "Schools were inserted in database.");
    }

    private void createComments() {
        List<Comment> commentList = new ArrayList<>();
        int[] placesFK = {0, 1};
        String[] comments = {"La mejor escuela de la universidad.", "No tan buena, creen que son de compu pero no lo son."};
        for (int i = 0; i < placesFK.length; ++i) {
            commentList.add(new Comment(i, placesFK[i], comments[i], UtilDates.DateToString(Calendar.getInstance().getTime())));
        }
        for (Comment c : commentList) {
            db.insert("Comment", c);
        }
        Log.d("comments", "Comments were inserted in database.");
    }

    private void createLibrary() {
        Library tinoco = new Library(0,  "Tinoco", "", R.drawable.libros, 9.943473, -84.0469052);
        db.insert("Library", tinoco);

        Library carlos = new Library(1, "Carlos Monge", "", R.drawable.libros, 9.943473, -84.0469052);
        db.insert("Library", carlos);

    }

    private void createOffice() {
        Office oficina1 = new Office(0,  "Oficina de Generales", "", R.drawable.administracion, 9.943473, -84.0469052);
        db.insert("Office", oficina1);

        Office oficina2 = new Office(1,  "OCCI", "", R.drawable.administracion, 9.943473, -84.0469052);
        db.insert("Office", oficina2);

    }

    private void createSoda() {
        Soda odonto = new Soda(0,  "Soda de Odontología", "", R.drawable.sandwich, 9.9379464, -84.0514961);
        db.insert("Soda", odonto);

        Soda generales = new Soda(1,  "Soda de Generales", "", R.drawable.sandwich, 9.9379464, -84.0514961);
        db.insert("Soda", generales);

        Soda economicas = new Soda(2,  "Soda de Económicas", "", R.drawable.sandwich, 9.9379464, -84.0514961);
        db.insert("Soda", economicas);

    }

    private void createPhotocopier() {
        Photocopier exactas = new Photocopier(0,  "CopiasExactas", "", R.drawable.impresora, 9.943473, -84.0469052);
        db.insert("Photocopier", exactas);

        Photocopier millenium = new Photocopier(1,  "Impresiones Millenium", "", R.drawable.impresora, 9.943473, -84.0469052);
        db.insert("Photocopier", millenium);

        Photocopier misCopias = new Photocopier(2,  "Mis Copias", "", R.drawable.impresora, 9.943473, -84.0469052);
        db.insert("Photocopier", misCopias);
    }

}
