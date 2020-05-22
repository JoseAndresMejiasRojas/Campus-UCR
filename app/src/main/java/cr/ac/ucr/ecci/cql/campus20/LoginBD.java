package cr.ac.ucr.ecci.cql.campus20;

import com.google.android.gms.tasks.Task;

public interface LoginBD
{
    // Se obliga a que sea una tarea asincronica y el que lo invoca tiene que manejarlo.
    public Task iniciarSesion(String correo, String contrasenna);

    public boolean autenticado();

    public String obtenerCorreoActual();

    public void tareaAppDefaultAsync(String idUsuario, FirebaseListener listener);

    public void detenerAppDefaultAsync();

    public void cerrarSesion();

    public void escribirDatos(String path, Object datos);
}
