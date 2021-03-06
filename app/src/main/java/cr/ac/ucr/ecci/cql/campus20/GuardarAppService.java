package cr.ac.ucr.ecci.cql.campus20;

import android.app.IntentService;
import android.content.Intent;


public class GuardarAppService extends IntentService
{
    private static final String PATH_CONFIG = "config_usuarios/";
    private static final String DESTINO_APP = "/app_inicial";
    public static final String APP_ID_KEY = "appId";


    public GuardarAppService()
    {
        super("GuardarAppService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int appId = intent.getIntExtra(APP_ID_KEY, -1);

        if(appId != -1)
        {
            CampusBD campusBD = new FirebaseBD();
            String correo = campusBD.obtenerCorreoActual();
            String usuario = correo.substring(0, correo.indexOf('@'));

            // Guardar datos
            campusBD.escribirDatos(PATH_CONFIG + usuario + DESTINO_APP, appId);
        }
    }
}
