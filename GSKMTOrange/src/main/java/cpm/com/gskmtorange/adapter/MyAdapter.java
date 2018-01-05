package cpm.com.gskmtorange.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.listener.DragListener;
import cpm.com.gskmtorange.listener.Listener;
import cpm.com.gskmtorange.xmlGetterSetter.NoCameraDataGetterSetter;

/**
 * Created by yadavendras on 17-10-2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflator;

    List<NoCameraDataGetterSetter> data = Collections.emptyList();
    private Listener listener;
    RecyclerView rec;

    public MyAdapter(Context context, List<NoCameraDataGetterSetter> data, Listener listener, RecyclerView rec) {

        inflator = LayoutInflater.from(context);
        this.data = data;
        this.listener = listener;
        this.rec = rec;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.brand_item, parent, false);

        MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final NoCameraDataGetterSetter current = data.get(position);

        final String name = current.getSKUGROUP_NAME();

        holder.name.setText(name);
        holder.tv_facing.setText(current.getFacing()+"");

        holder.linear_parent.setTag(position);
        //holder.linear_parent.getLayoutParams().width = 100;
        //holder.linear_parent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        holder.linear_parent.setOnDragListener(new DragListener(listener));

        holder.linear_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.deleteItem(view, position, rec);
                return false;
            }

        });

    }

    public DragListener getDragInstance() {
        if (listener != null) {
            return new DragListener(listener);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<NoCameraDataGetterSetter> getList() {
        return data;
    }

    public void updateList(List<NoCameraDataGetterSetter> list) {
        this.data = list;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView tv_facing;
        LinearLayout linear_parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_brand);
            tv_facing = (TextView) itemView.findViewById(R.id.tv_facing);
            linear_parent = (LinearLayout) itemView.findViewById(R.id.linear_parent);

        }

    }
}
