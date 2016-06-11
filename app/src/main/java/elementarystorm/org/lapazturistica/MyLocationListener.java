package elementarystorm.org.lapazturistica;

/**
 * Created by Vico on 03/05/2016.
 */
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
public class MyLocationListener implements LocationListener {
    MainActivity mainActivity;
    public MainActivity getMainActivity(){
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        //este  metodo se ejecuta cada ves que el gps recive nuevas cordenadas
        //devido a la deteccion de un cambio de ubicacion
        location.getLatitude();
        location.getLongitude();
        String text ="mi ubicacion es "+ location.getLatitude()+" - "+ location.getLongitude();
        //Log.e("location ",text);
        //mensage text
        //mainActivity.b.setText(text);   //***********************
        //mainActivity.setLocation(location);           //////**************
        mainActivity.pocicion_actual(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        //  este metodo se activa cuando el gps esta desactivado
        ///mensage el gps esta desactivado
        //mainActivity.remove_mylocation();
        Log.e("location ", "GPS DESACTIVADO");
    }

    @Override
    public void onProviderEnabled(String provider) {
        //gps activado
        //mensage de gps activado
        Log.e("location ","GPS activado");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
