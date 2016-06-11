package elementarystorm.org.lapazturistica;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vico on 03/05/2016.
 */
public class ItemLugarAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Lugar> items;
    public ItemLugarAdapter(Activity activity, ArrayList<Lugar> items) {
        this.activity = activity;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        if(contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.item_lugar, null);
        }
        Lugar item = items.get(position);
        TextView titulo = (TextView)contentView.findViewById(R.id.item_title);
        titulo.setText(item.getNombre());
        TextView subtitulo = (TextView)contentView.findViewById(R.id.item_descripcion);
        if(item.getDescripcion().length()>110)
            subtitulo.setText(item.getDescripcion().substring(0,110)+" ...");
        else
            subtitulo.setText(item.getDescripcion() );
        ImageView img=(ImageView)contentView.findViewById(R.id.item_img);
        img.setImageBitmap(item.getBitMap());
        return contentView;
    }

}