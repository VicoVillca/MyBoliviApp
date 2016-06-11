package elementarystorm.org.lapazturistica;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vico on 02/05/2016.
 */
public class Lugar {
    private int id;
    private String nombre;
    private String descripcion;
    private int foto;
    private double latitud;
    private double longitud;
    private String distancia;
    private String tipo;
    private Bitmap b=null;
    private Drawable drawable;
    public Lugar(int i, String nombre, String descripcion, int foto,double latitud,double longitud,String distancia) {
        this.id=i;
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.foto=foto;
        this.latitud=latitud;
        this.longitud=longitud;
        this.distancia=distancia;
    }
    public Lugar(String nombre,String tipo,int foto,double latitud,double longitud,Drawable drawable){
        this.nombre=nombre;
        this.descripcion=tipo;
        this.tipo=tipo;
        this.foto=foto;
        this.latitud=latitud;
        this.longitud=longitud;
        this.drawable=drawable;
    }
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public int getFoto() {
        return foto;
    }
    public double getLatitud(){
        return latitud;
    }
    public double getLongitud(){
        return longitud;
    }
    public String getDistancia(){
        return this.distancia;
    }
    public String getTipo(){
        return this.tipo;
    }
    public GeoPoint getGeoPoint(){
        return new GeoPoint(this.latitud,this.longitud);
    }
    public void setBitMap(Bitmap b){
        this.b=b;
    }
    public Bitmap getBitMap(){
        return this.b;
    }
    public Drawable getDrawable(){
        return this.drawable;
    }
}