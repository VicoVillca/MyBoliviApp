package elementarystorm.org.lapazturistica;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DescipcionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descipcion);
    }
    public void linck_elementaryStorm(View view){
        String link = "http://www.comollego.org/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
    public void linck_comollego(View view){
        String link = "http://www.comollego.org/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
    public void linck_SinVidaSocial(View view){
        String link = "https://www.facebook.com/VicoVillca";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
    //https://www.facebook.com/myboliviapp/
}
