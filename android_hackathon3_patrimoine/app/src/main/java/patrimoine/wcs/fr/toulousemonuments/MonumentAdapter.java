package patrimoine.wcs.fr.toulousemonuments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import patrimoine.wcs.fr.toulousemonuments.models.Record;

/**
 * Created by apprenti on 11/05/17.
 */

public class MonumentAdapter extends BaseAdapter {

    public static final double TOKMPERHOUR = 3.6;
    public static final double KELVIN = 273.15;
    private Context context; //context
    private ArrayList<Record> items; //data source of the list adapter
    private ImageView mImageViewListRecord;

    //public constructor
    public MonumentAdapter(Context context, ArrayList<Record> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.record_item, parent, false);
        }

        // get current item to be displayed
        Record currentRecordItem = (Record) getItem(position);

        // get the TextView for item name and item description
        TextView textViewForecastHour = (TextView)
                convertView.findViewById(R.id.textViewListRecord);
        mImageViewListRecord = (ImageView) convertView.findViewById(R.id.imageViewListRecord);

        //sets the text for item name and item description from the current item object

        textViewForecastHour.setText(currentRecordItem.getFields().getNom());
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        InputStream inputStream = null;
        if (currentRecordItem.getRecordid().equals("2d2dbd9934460646ae619839d30e58b19c28b261")){
            try {
                inputStream = assetManager.open( "HOTEL HUGUES DE BOISSON JEAN DE CHEVERRY" + ".jpg", AssetManager.ACCESS_BUFFER);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(inputStream);
            mImageViewListRecord.setImageBitmap(bitmap);
            //new ImageDownloadTask(mImageViewListRecord).execute("HOTEL HUGUES DE BOISSON JEAN DE CHEVERRY");
        }
        else {
            try {
                inputStream = assetManager.open( currentRecordItem.getFields().getNomCdt() + ".jpg", AssetManager.ACCESS_BUFFER);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(inputStream);
            mImageViewListRecord.setImageBitmap(bitmap);
            //new ImageDownloadTask(mImageViewListRecord).execute(currentRecordItem.getFields().getNomCdt());
        }

        return convertView;
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            AssetManager assetManager = context.getAssets();
            try {
                InputStream inputStream = assetManager.open( params[0] + ".jpg", AssetManager.ACCESS_BUFFER);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
        public ImageDownloadTask(ImageView iv){
            mImageViewListRecord = iv;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            mImageViewListRecord.setImageBitmap(bitmap);
        }
    }


}
