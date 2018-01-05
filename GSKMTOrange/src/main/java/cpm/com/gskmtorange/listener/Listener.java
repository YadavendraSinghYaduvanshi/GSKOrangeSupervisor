package cpm.com.gskmtorange.listener;

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

import cpm.com.gskmtorange.xmlGetterSetter.NoCameraDataGetterSetter;


/**
 * Created by yadavendras on 25-10-2017.
 */

public interface Listener {

    void setEmptyListBottom(boolean visibility, int tv, RecyclerView rv);
    void addNUpdateRow(View v, int view_id, NoCameraDataGetterSetter parent_item);
    void smoothScrollToRow(View v, DragEvent event);
    void deleteItem(View v, int column_no, RecyclerView rec);
}
