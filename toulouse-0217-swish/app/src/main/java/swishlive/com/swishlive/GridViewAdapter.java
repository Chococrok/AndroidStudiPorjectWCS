package swishlive.com.swishlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GridViewAdapter extends BaseAdapter {

    private Context context;

    public GridViewAdapter(Context context){

        this.context = context;
    }
    @Override
    public int getCount() {
        return ressourcesId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_view_item, parent, false);
        }

        ImageView imageViewBall = (ImageView) convertView.findViewById(R.id.imageViewBall);
        TextView textViewSportName = (TextView) convertView.findViewById(R.id.textViewSportName);

        imageViewBall.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewBall.setPadding(8, 8, 8, 8);

        imageViewBall.setImageResource(ressourcesId[position]);
        textViewSportName.setText(sportsNames[position]);
        return convertView;

    }

    /** TODO FRAGMENT
     *  volley --> DONE
     *  basket --> DONE
     *  tennis --> TODO
     *  hand   --> DONE
     *  foot   --> DONE
     *  rugby  --> DONE
     *  */

    private String[] sportsNames = {
            "Volleyball",
            "Basketball",
            "Tennis",
            "Handball",
            "Football",
            "Rugby"
    };

    private Integer[] ressourcesId ={
            R.drawable.volleyball,
            R.drawable.basketball,
            R.drawable.tennis,
            R.drawable.handball,
            R.drawable.football,
            R.drawable.rugby
    };
}
