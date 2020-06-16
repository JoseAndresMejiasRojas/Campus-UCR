package cr.ac.ucr.ecci.cql.campus20.red_mujeres;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MainRedMujeresTest {

    @Test
    void onCreate() {
    }

    @Test
    void onMapReady() {
    }

    @Test
    void iniciarRuta() {
    }

    @Test
    void panico() { //
        MainRedMujeres test = Mockito.mock(MainRedMujeres.class);
        ArgumentCaptor<Integer> valueCapture = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(test).panico(valueCapture.capture());
        test.panico(1);
        assertEquals( (Object) 1, (Object) valueCapture.getValue());
    }

    @Test
    void popupPanico() {
        MainRedMujeres test = Mockito.mock(MainRedMujeres.class);
        test.popupPanico();
        verify(test, times(1)).popupPanico();
    }

    @Test
    void popupCompartir() {
    }

    @Test
    void enviarWhatsapp() {
    }

    @Test
    void enviarSMS() {
    }

    @Test
    void onMapClick() {
    }

    @Test
    void onRequestPermissionsResult() {
    }

    @Test
    void onExplanationNeeded() {
    }

    @Test
    void onPermissionResult() {
    }

    @Test
    void onCancelNavigation() {
    }

    @Test
    void onNavigationFinished() {
    }

    @Test
    void onNavigationRunning() {
    }

    @Test
    void onStart() {
    }

    @Test
    void onResume() {
    }

    @Test
    void onPause() {
    }

    @Test
    void onStop() {
    }

    @Test
    void onSaveInstanceState() {
    }

    @Test
    void onDestroy() {
    }

    @Test
    void onLowMemory() {
    }

    @Test
    void readGroupData() {
    }

    @Test
    void readGroupUsersData() {
    }

    @Test
    void updateMyLocation() {
    }
}