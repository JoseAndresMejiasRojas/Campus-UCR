package cr.ac.ucr.ecci.cql.campus20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity
{
    private CampusBD campusBD;

    private Redireccionador redireccionador;

    private EditText correoInput;
    private EditText contrasennaInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        redireccionador = new Redireccionador();
        campusBD = new FirebaseBD();

        if(campusBD.autenticado())
        {
            redireccionador.irActividadGuardada(this);
        }
        else
        {
            Button buttonIniciarSesion = findViewById(R.id.buttonLogin);
            correoInput = findViewById(R.id.editTextCorreo);
            contrasennaInput = findViewById(R.id.editTextContrasenna);

            buttonIniciarSesion.setOnClickListener(v -> procesarDatos());
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    public void procesarDatos()
    {
        String correo = correoInput.getText().toString().trim();
        String contrasenna = contrasennaInput.getText().toString();

        if(correo.length() > 0 && contrasenna.length() > 0)
        {
            validarCredenciales(correo, contrasenna);
        }
        else
        {
            mostrarMensajeError("Por favor ingrese el correo y la contraseña");
        }
    }


    public void validarCredenciales(String correo, String contrasenna)
    {

        if(campusBD.autenticado() == false)
        {
            Task tareaValidador = campusBD.iniciarSesion(correo, contrasenna);

            tareaValidador.addOnCompleteListener(this, task ->
            {
                if(task.isSuccessful())
                {
                    redireccionador.irActividadGuardada(this);
                }
                else
                {
                    if(VerificadorInternet.conexionInternet(this) == false)
                    {
                        mostrarMensajeError("Por favor revise su conexión a internet");
                    }
                    else
                    {
                        mostrarMensajeError("El correo o contraseña son inválidos");
                    }
                }
            });
        }
    }



    public void mostrarMensajeError(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}