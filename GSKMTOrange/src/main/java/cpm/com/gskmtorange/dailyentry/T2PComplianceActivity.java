package cpm.com.gskmtorange.dailyentry;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.GapsChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.T2PGetterSetter;

public class T2PComplianceActivity extends AppCompatActivity {

    GSKOrangeDB db;
    ArrayList<T2PGetterSetter> t2PGetterSetters;
    T2PAdapter t2PAdapter;
    RecyclerView rec_t2p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t2_pcompliance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new GSKOrangeDB(T2PComplianceActivity.this);
        db.open();

        rec_t2p = (RecyclerView) findViewById(R.id.rec_t2p);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        t2PGetterSetters = db.getT2PDefaultData("1");

        if(t2PGetterSetters.size()>0){
            rec_t2p.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            t2PAdapter = new T2PAdapter(t2PGetterSetters);
            rec_t2p.setAdapter(t2PAdapter);

        }
    }

    public class T2PAdapter extends RecyclerView.Adapter<T2PAdapter.ViewHolder> {

        private ArrayList<T2PGetterSetter> list;

        public T2PAdapter(ArrayList<T2PGetterSetter> t2PList) {
            list = t2PList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.t2p_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final T2PGetterSetter mItem = list.get(position);
            holder.tv_brand.setText(mItem.getBrand());
            holder.tv_display.setText(mItem.getDisplay().trim());

            holder.btn_gaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   ArrayList<GapsChecklistGetterSetter> gapsChecklist = showGapsDialog(mItem.getDisplay_id());
                    if(gapsChecklist.size()>0){

                    }
                }
            });

            holder.btn_sku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSkuDialog();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final LinearLayout parentLayout;
            public final TextView tv_brand, tv_display;
            public final ImageView img_cam, img_remark;
            public final Button btn_gaps, btn_sku, btn_ref_img;


            public ViewHolder(View view) {
                super(view);

                mView = view;

                tv_brand = (TextView) mView.findViewById(R.id.tv_brand);
                tv_display = (TextView) mView.findViewById(R.id.tv_display);
                img_cam = (ImageView) mView.findViewById(R.id.img_cam);
                img_remark = (ImageView) mView.findViewById(R.id.img_remark);
                btn_gaps = (Button) mView.findViewById(R.id.btn_gaps);
                btn_sku = (Button) mView.findViewById(R.id.btn_sku);
                btn_ref_img = (Button) mView.findViewById(R.id.btn_ref_image);
                parentLayout = (LinearLayout) mView.findViewById(R.id.parent_layout);

            }

        }
    }

    public ArrayList<GapsChecklistGetterSetter> showGapsDialog(String display_id){
        ArrayList<GapsChecklistGetterSetter> gapsChecklist = db.getGapsDefaultData(display_id);

        Dialog dialog = new Dialog(T2PComplianceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.gaps_dialog_layout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        RecyclerView rec_gap_checklist = (RecyclerView) dialog.findViewById(R.id.rec_gap_checklist);
        rec_gap_checklist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        GapsAdapter gapAdapter = new GapsAdapter(gapsChecklist);
        rec_gap_checklist.setAdapter(gapAdapter);

        //dialog.setCancelable(false);
        dialog.show();

        return gapsChecklist;
    }

    public void showSkuDialog(){

        ArrayList<BrandMasterGetterSetter> brandList = db.getBrandT2PData("1", "1", "1");
       // ArrayList<SkuMasterGetterSetter> skuMasterGetterSetterArrayList = db.getSkuT2PData("1", "1", "1",)

        Dialog dialog = new Dialog(T2PComplianceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.t2p_sku_dialog_layout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
         //dialog.setCancelable(false);
        dialog.show();

    }

    public class GapsAdapter extends RecyclerView.Adapter<GapsAdapter.ViewHolder> {

        private ArrayList<GapsChecklistGetterSetter> list;

        public GapsAdapter(ArrayList<GapsChecklistGetterSetter> gapsPList) {
            list = gapsPList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gap_checklist_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final GapsChecklistGetterSetter mItem = list.get(position);
            holder.tv_checklist.setText(mItem.getChecklist());

            holder.tb_present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final LinearLayout parentLayout;
            public final TextView tv_checklist;
            public final ToggleButton tb_present;


            public ViewHolder(View view) {
                super(view);

                mView = view;

                tv_checklist = (TextView) mView.findViewById(R.id.tv_checklist);
                tb_present = (ToggleButton) mView.findViewById(R.id.btn_is_present);
                parentLayout = (LinearLayout) mView.findViewById(R.id.parent_layout);

            }

        }
    }

}
