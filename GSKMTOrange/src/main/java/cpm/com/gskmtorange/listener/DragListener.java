package cpm.com.gskmtorange.listener;

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.adapter.ListAdapter;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NoCameraDataGetterSetter;

/**
 * Created by yadavendras on 25-10-2017.
 */

public class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private Listener listener;

    public DragListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        View viewSource = (View) event.getLocalState();
        int viewId = v.getId();

        final int rvTop = R.id.rvTop;

        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

               if( v instanceof TextView ||  v instanceof RecyclerView){

                   if (viewSource != null) {

                       RecyclerView source = (RecyclerView) viewSource.getParent();

                       ListAdapter adapterSource = (ListAdapter) source.getAdapter();
                       int positionSource = (int) viewSource.getTag();
                       int sourceId = source.getId();

                       NoCameraDataGetterSetter parent_item = adapterSource.getList().get(positionSource);

                       if(viewId != rvTop){

                           listener.addNUpdateRow(v, viewId, parent_item);
                       }
                   }
               }

               break;

            case DragEvent.ACTION_DRAG_LOCATION:

                if(viewId == rvTop){
                    listener.smoothScrollToRow(v, event);
                }

                break;
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }

        return true;
    }
}
