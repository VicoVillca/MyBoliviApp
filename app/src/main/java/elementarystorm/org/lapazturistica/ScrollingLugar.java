package elementarystorm.org.lapazturistica;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ScrollingLugar extends AppCompatActivity {
    private Lugar l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i=Integer.parseInt(getIntent().getExtras().getString("i"));
        setContentView(R.layout.activity_scrolling_lugar);
        l=new Funciones_utiles(this).getLugar(i);
        ((TextView)findViewById(R.id.scroll_text)).setText(l.getDescripcion());
        this.setTitle(l.getNombre());
        ((TextView)findViewById(R.id.scroll_text_descripcion)).setText(l.getDistancia());
        ((ImageView)findViewById(R.id.img_foto)).setImageDrawable(getResources().getDrawable(l.getFoto()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = getResources().getString(R.string.linck_g_map) + l.getLatitud() + "+" + l.getLongitud() + "/@" + l.getLatitud() + "," + l.getLongitud() + ",15z/data=!4m2!4m1!3e0";
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

