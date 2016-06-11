package elementarystorm.org.lapazturistica;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MapView map;
    private IMapController mapController;
    private Funciones_utiles f_u;
    private long mLastPresed = 0;
    private long mTimeLimit = 2000;
    //Lugares Inperdibles
    private ArrayList<MyInfoWindow> InfoWindow_lugares = new ArrayList<MyInfoWindow>();
    private ArrayList<Lugar> LugaresInperdibles;
    private ArrayList<Lugar> Lugares;
    private List<Marker> Markers = new ArrayList<Marker>();
    private  FloatingActionButton fab_steep;
    private FloatingActionButton fab_list;
    private GeoPoint aux;
    private FloatingActionsMenu fabMenu;
    private Marker ubicacion = null;
    private MyLocationListener mlocListener;
    private LocationManager mlocManager;
    private AlertDialog dialog_gps=null;
    boolean centrear;
    private Locale locale;
    private Configuration config = new Configuration();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });
        /*
         Acciones a elementos de floatingbutommenu
         */
        final FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ayuda/policia
                ColocarMarker(1);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hospital/farmacias
                ColocarMarker(2);
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lugares inperdibles
                ColocarInfoWindows();

            }
        });
        FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iglesias/plazas/museos
                ColocarMarker(4);
            }
        });
        fab_steep = (FloatingActionButton) findViewById(R.id.fab_glg);
        fab_steep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //marker seleccionado usar google masp para dirigisse al lugar
                if (aux != null) {
                    String link = getResources().getString(R.string.linck_g_map) + aux.getLatitude() + "+" + aux.getLongitude() + "/@" + aux.getLatitude() + "," + aux.getLongitude() + ",15z/data=!4m2!4m1!3e0";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                }

            }
        });
        fab_list = (FloatingActionButton) findViewById(R.id.fab_list);
        fab_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_list();
            }
        });
        ///fin
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        f_u = new Funciones_utiles(this);
        addOverlays();
        LugaresInperdibles = f_u.getLugares_Inperdibles();
        Lugares = f_u.getLugares();
        ColocarInfoWindows();
    }
    private void showDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.str_button));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                    case 2:
                        locale = new Locale("pt");
                        config.locale =locale;
                        break;
                    case 3:
                        locale = new Locale("ja");
                        config.locale =locale;
                        break;
                    case 4:
                        locale = new Locale("zh");
                        config.locale =locale;
                        break;
                    case 5:
                        locale = new Locale("ar");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();
            }

        });

        b.show();
    }
    public void addOverlays() {
        map = (MapView) findViewById(R.id.mapa1);
        if (map != null) {
            mapController = map.getController();

            XYTileSource ts = new XYTileSource("lpztur", ResourceProxy.string.offline_mode, 6, 20, 500, ".png", new String[]{
                    getResources().getString(R.string.tile_a),
                    getResources().getString(R.string.tile_b),
                    getResources().getString(R.string.tile_c)});
            ///
            map.setTileSource(ts);
            map.setMultiTouchControls(true);
            GeoPoint startPoint = new GeoPoint(f_u.getLatitud(), f_u.getLongitud());
            mapController.setZoom(6);
            mapController.setCenter(startPoint);
            map.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fabMenu.isExpanded()) {
                fabMenu.collapse();
            } else {
                if(dialog_gps!=null){
                    dialog_gps.cancel();
                    dialog_gps=null;
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - mLastPresed > mTimeLimit) {
                    Toast.makeText(this, getResources().getText(R.string.exit), Toast.LENGTH_SHORT).show();
                    mLastPresed = currentTime;
                } else {
                    super.onBackPressed();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //GPS
        if (id == R.id.action_gps) {
            iniciamos_GPS();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent refresh = new Intent(MainActivity.this, InfoBolivia.class);
            startActivity(refresh);

        }  else if (id == R.id.facebook) {
            String link = "https://www.facebook.com/myboliviapp/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }  else if (id == R.id.config) {
            showDialog();
        }  else if (id == R.id.app) {
            //en esta parte abrimos la descripcion de la app
            Intent intent = new Intent(MainActivity.this,DescipcionActivity.class);
            startActivity(intent);
        }  else if (id == R.id.la_paz) {
            //centreamos lapaz
            Centrear(-16.5, -68.15);
            Toast.makeText(this,"La Paz",Toast.LENGTH_SHORT).show();
        }  else if (id == R.id.cochabamba) {
            //centreamos cochabamba
            Centrear(-17.4194, -66.1325);
            Toast.makeText(this,"Cochabamba",Toast.LENGTH_SHORT).show();
        }  else if (id == R.id.santa_cruz) {
            //centreamos santa cruz
            Centrear(-17.8,-63.1667);
            Toast.makeText(this,"Santa Cruz",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.oruro) {
            //centreamos santa cruz
            Centrear(-17.966802, -67.101360);
            Toast.makeText(this,"Oruro",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.sucre) {
            //centreamos santa cruz
            Centrear(-19.022108, -65.261338);
            Toast.makeText(this,"Sucre",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.potosi) {
            //centreamos santa cruz
            Centrear(-19.575986, -65.755867);
            Toast.makeText(this,"Potosi",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.tarija) {
            //centreamos santa cruz
            Centrear(-21.529607, -64.731237);
            Toast.makeText(this,"Tarija",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.el_alto) {
            //centreamos santa cruz
            Centrear(-16.507056, -68.207177);
            Toast.makeText(this,"El Alto",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.cobija) {
            //centreamos santa cruz
            Centrear(-11.035104, -68.778368);
            Toast.makeText(this,"Cobija",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.trinidad) {
            //centreamos santa cruz
            Centrear(-14.830647, -64.903993);
            Toast.makeText(this,"Trinidad",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.villazon) {
            //centreamos santa cruz
            Centrear(-22.083385, -65.597917);
            Toast.makeText(this,"Villazon",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.tupiza) {
            //centreamos santa cruz
            Centrear(-21.434742, -65.719559);
            Toast.makeText(this,"Tupiza",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.riberalta) {
            //centreamos santa cruz
            Centrear(-11.009988, -66.052680);
            Toast.makeText(this,"Riberalta",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.wayaramerin) {
            //centreamos santa cruz
            Centrear(-10.827951, -65.360842);
            Toast.makeText(this,"Guayaramerin",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.camiri) {
            //centreamos santa cruz
            Centrear(-20.031815, -63.526650);
            Toast.makeText(this,"Camiri",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.bermejo) {
            //centreamos santa cruz
            Centrear(-22.744966, -64.306077);
            Toast.makeText(this,"Bermejo",Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ColocarMarker(int n) {
        LimpiamosMapa();
        for (int i = 0; i < Lugares.size(); i++) {
            Lugar l=Lugares.get(i);
            if(n==1){
                if(l.getTipo().equals(getResources().getText(R.string.policia))||l.getTipo().equals(getResources().getText(R.string.informacion)))
                    c_m(l);
            }
            if(n==2){
                if(l.getTipo().equals(getResources().getText(R.string.centro))||l.getTipo().equals(getResources().getText(R.string.hospital))||l.getTipo().equals(getResources().getText(R.string.farmacia)))
                    c_m(l);
            }
            if(n==4){
                if(l.getTipo().equals(getResources().getText(R.string.iglesia))||l.getTipo().equals(getResources().getText(R.string.mirador))||l.getTipo().equals(getResources().getText(R.string.plaza))||l.getTipo().equals(getResources().getText(R.string.parque))||l.getTipo().equals(getResources().getText(R.string.museo)))
                    c_m(l);
            }

        }
        ActualizamosMapa();
    }
    public void c_m(Lugar l){
        Marker m = new Marker(map);
        m.setPosition(l.getGeoPoint());
        m.setIcon(l.getDrawable());
        m.setAnchor(m.ANCHOR_CENTER, 1.0f);
        m.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                fab_steep.setVisibility(View.VISIBLE);
                aux = marker.getPosition();

                marker.showInfoWindow();

                return false;
            }
        });

        m.setTitle(l.getNombre());
        m.setSnippet(l.getTipo());
        //m.setSubDescription("SusbDescripcion");
        map.getOverlays().add(m);
        Markers.add(m);
    }
    public void ColocarInfoWindows() {

        LimpiamosMapa();
        fab_list.setVisibility(View.VISIBLE);
        for (int i = 0; i < LugaresInperdibles.size(); i++) {
            MyInfoWindow infoWindow;
            if (LugaresInperdibles.get(i).getBitMap() == null) {
                LugaresInperdibles.get(i).setBitMap(f_u.decodeSampledBitMapFromResouce(getResources(), LugaresInperdibles.get(i).getFoto(), 50, 50));
            }
            infoWindow = new MyInfoWindow(R.layout.item_popup, map, LugaresInperdibles.get(i));
            infoWindow.open(null, LugaresInperdibles.get(i).getGeoPoint(), 15, 20);
            InfoWindow_lugares.add(infoWindow);
        }

        ActualizamosMapa();
    }

    public void LimpiamosMapa() {
        fab_list.setVisibility(View.INVISIBLE);
        fab_steep.setVisibility(View.INVISIBLE);
        //limpiamos infowindows
        if (InfoWindow_lugares.size() > 0) {
            for (int i = 0; i < InfoWindow_lugares.size(); i++) {
                InfoWindow_lugares.get(i).close();
                map.getOverlays().remove(InfoWindow_lugares.get(i));

            }
            InfoWindow_lugares = new ArrayList<MyInfoWindow>();
        }
        //limpiamos markers
        if (Markers.size() > 0) {
            for (int i = 0; i < Markers.size(); i++) {
                if (Markers.get(i).isInfoWindowOpen())
                    Markers.get(i).closeInfoWindow();
                map.getOverlays().remove(Markers.get(i));

            }
            Markers = new ArrayList<Marker>();
        }
    }
    public void ActualizamosMapa() {
        fabMenu.collapse();
        map.invalidate();
    }

    public void alert_list() {
        try {
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.alert_listar, null);
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            alert1.setView(alertLayout);
            alert1.setCancelable(true);

            ListView lb = (ListView) alertLayout.findViewById(R.id.lista_lugares);
            final ItemLugarAdapter adapter = new ItemLugarAdapter(this, LugaresInperdibles);
            //adapter.notifyDataSetChanged();
            lb.setAdapter(adapter);

            lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, ScrollingLugar.class);
                intent.putExtra("i", "" +position);
                startActivity(intent);
                }
            });
            //colocamos adaptadores
            AlertDialog dialog = alert1.create();
            dialog.show();

        } catch (Exception e) {
            Log.e("Alert gps  ", e.toString());
        }
    }

    public void iniciamos_GPS() {
        centrear =true;
        if(fabMenu.isExpanded()) {
            fabMenu.collapse();
        }
        try {

            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.alert_usar_gps, null);
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            alert1.setView(alertLayout);
            alert1.setCancelable(true);
            if (mlocListener == null) {
                mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mlocListener = new MyLocationListener();
                mlocListener.setMainActivity(this);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    return;
                }
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            }
            alert1.setNegativeButton(getResources().getText(R.string.cancelar), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //con esto se deveria concelar pero nose como
                    //dialog.cancel();
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        return;
                    }
                    mlocManager.removeUpdates(mlocListener);
                    mlocListener = null;
                }
            });
            dialog_gps = alert1.create();
            dialog_gps.show();


        }catch (Exception e){
            Log.e("Alert gps  ", e.toString());
        }


    }
    public void pocicion_actual(Location location) {
        try {
            dialog_gps.dismiss();
            dialog_gps=null;
        }catch (Exception e){
        }
        if(ubicacion!=null)
            map.getOverlays().remove(ubicacion);
        ubicacion = new Marker(map);
        ubicacion.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        //ubicacion.setIcon(this.getResources().getDrawable(R.drawable.circleactive));
        ubicacion.setAnchor(ubicacion.ANCHOR_CENTER, 1.0f);
        map.getOverlays().add(ubicacion);
        if(centrear){
            centrear=false;
            mapController.setCenter(ubicacion.getPosition());
        }
        map.invalidate();
    }
    public void Centrear(double latitud,double longitud){
        mapController.setCenter(new GeoPoint(latitud, longitud));
        mapController.setZoom(12);
    }

    private class MyInfoWindow extends InfoWindow {
        Lugar l;
        public MyInfoWindow(int layutResId, MapView mapView, Lugar l) {
            super(layutResId, mapView);
            this.l=l;
        }

        @Override
        public void onOpen(Object o) {

            ImageView img = (ImageView) mView.findViewById(R.id.imagen_popup);
            //img.setImageDrawable(getResources().getDrawable(l.getFoto()));
            img.setImageBitmap(l.getBitMap());
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ScrollingLugar.class);
                    intent.putExtra("i", "" + LugarPorNombre(l.getNombre()));
                    startActivity(intent);
                    //cuando se hace clich sobre un openInfo se tiene  que abrir un activity scroll
                }
            });
        }
        @Override
        public void onClose() {
            this.close();
        }
    }
    public int LugarPorNombre(String s){
        int r=-1;
        for(int i=0;i<LugaresInperdibles.size();i++){
            if(s.equals(LugaresInperdibles.get(i).getNombre()))
                r=i;
        }
        return r;
    }
}
