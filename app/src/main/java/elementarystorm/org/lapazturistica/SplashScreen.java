package elementarystorm.org.lapazturistica;

/**
 * Created by Vico on 02/05/2016.
 */
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity{
    long l_s=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        /*Thread timetheadre = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timetheadre.start();*/
        inicio2();
    }

    public void inicio2(){
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer time=new Timer();
        time.schedule(task,l_s);
    }
    @Override
    protected void onRestart() {

        super.onRestart();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                //Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                //startActivity(intent);
                finish();
            }
        };
        //Toast.makeText(this,"longitud "+l_s,Toast.LENGTH_LONG).show();
        Timer time=new Timer();
        time.schedule(task, 1000);
    }
}
