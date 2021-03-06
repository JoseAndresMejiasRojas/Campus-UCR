package cr.ac.ucr.ecci.cql.campus20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import cr.ac.ucr.ecci.cql.campus20.InterestPoints.InterestPointsActivity;
import cr.ac.ucr.ecci.cql.campus20.foro_general.MainForoGeneral;
import cr.ac.ucr.ecci.cql.campus20.red_mujeres.MenuRedMujeres;
import cr.ac.ucr.ecci.cql.campus20.ucr_eats.activites.RoleActivity;

public class Redireccionador
{
    private DatabaseReference mDatabase;
    private CampusBD campusBD;


    public Redireccionador()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        campusBD = new FirebaseBD();
    }

    // Termina la actividad actual cuando redirecciona
    public void irActividadGuardada(Context context)
    {
        // Si no hay actividad guardada, ir a la actividad de configuracion
        String correo = campusBD.obtenerCorreoActual();
        String idUsuario = correo.substring(0, correo.indexOf('@'));
        Intent intentConfiguracion = new Intent(context, ConfiguracionActivity.class);

        AtomicBoolean resultado = new AtomicBoolean(false);

        FirebaseListener listener = new FirebaseListener()
        {
            @Override
            public void exito(DataSnapshot dataSnapshot)
            {
                resultado.set(true);
                Long appUsuario = (Long) dataSnapshot.getValue();

                if(appUsuario != null)
                {
                    irAppPredeterminada(appUsuario.intValue(), context);
                }
                else
                {
                    // No hay actividad guardada, enviar a la configuracion inicial
                    intentConfiguracion.putExtra(ConfiguracionActivity.KEY_CORREO, idUsuario);
                    context.startActivity(intentConfiguracion);
                }

                // Termino la actividad que ejecuto el redireccionamiento
                ((Activity) context).finish();
            }

            @Override
            public void fallo(DatabaseError databaseError)
            {
                resultado.set(true);
            }
        };


        if(VerificadorInternet.conexionInternet(context))
        {
            campusBD.tareaAppDefaultAsync(idUsuario, listener);

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    timer.cancel();
                    if (resultado.get() == false)
                    {
                        //  Timeout
                        campusBD.detenerAppDefaultAsync();

                        //Toast.makeText(context,"En este momento tenemos errores de conexión con nuestros servidores",Toast.LENGTH_LONG).show();
                    }
                }
            };
            // Timeout de 10 segundos
            timer.schedule(timerTask, 10000L);
        }
        else
        {
            Toast.makeText(context,"No hay conexión a internet, la aplicación tendrá funciones limitadas", Toast.LENGTH_LONG).show();

            // Cuando se esta loggeado pero no hay internet, ir a UCR Eats (porque es la primera)
            irAppPredeterminada(0, context);
        }
    }

    private void irAppPredeterminada(int id, Context context)
    {
        if(id == 0)
        {
            RoleActivity.startDefaultActivity(context);
        }
        else
        {
            Intent intent;
            switch (id)
            {
                default:
                case 1:
                {
                    intent = new Intent(context, MenuRedMujeres.class);
                    break;
                }

                case 2:
                {
                    intent = new Intent(context, MainForoGeneral.class);
                    break;
                }
                case 3:
                {
                    intent = new Intent(context, InterestPointsActivity.class);
                    break;
                }
            }

            context.startActivity(intent);
        }
    }
}
