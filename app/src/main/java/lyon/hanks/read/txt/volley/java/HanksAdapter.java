package lyon.hanks.read.txt.volley.java;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class HanksAdapter extends BaseAdapter{

    Context context; ArrayList<String> path2;
    public HanksAdapter(Context context, ArrayList<String> path2){
        this.context=context;
        this.path2=path2;
    }
    @Override
    public int getCount() {
        return path2.size();
    }

    @Override
    public Object getItem(int position) {
        return path2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
