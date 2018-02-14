package cpm.com.gskmtorange.adapter;

import android.content.ClipData;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.listener.DragListener;
import cpm.com.gskmtorange.listener.Listener;
import cpm.com.gskmtorange.xmlGetterSetter.NoCameraDataGetterSetter;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>
        implements View.OnTouchListener {

    private List<NoCameraDataGetterSetter> list;
    private Listener listener;

    public ListAdapter(List<NoCameraDataGetterSetter> list, Listener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.brand_item_top_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.text.setText(list.get(position).getSKUGROUP_NAME());
        holder.linear_parent.setTag(position);
        holder.linear_parent.setOnTouchListener(this);
        holder.linear_parent.setOnDragListener(new DragListener(listener));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, v, 0);
                } else {
                    v.startDrag(data, shadowBuilder, v, 0);
                }
                return true;
        }
        return false;
    }

    public List<NoCameraDataGetterSetter> getList() {
        return list;
    }

    void updateList(List<NoCameraDataGetterSetter> list) {
        this.list = list;
    }

    public DragListener getDragInstance() {
        if (listener != null) {
            return new DragListener(listener);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        LinearLayout linear_parent;

        ListViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.tv_brand);
            linear_parent = (LinearLayout) itemView.findViewById(R.id.linear_parent);
        }
    }
}
