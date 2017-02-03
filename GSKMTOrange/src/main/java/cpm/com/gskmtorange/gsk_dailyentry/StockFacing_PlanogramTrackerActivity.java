package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cpm.com.gskmtorange.R;

public class StockFacing_PlanogramTrackerActivity extends AppCompatActivity {
    Button btn_addShelf, btn_addSKU;
    RecyclerView recyclerView;
    String brand, brand_id, company_id, sub_category, sub_category_id;
    String addShelfPosition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_facing__planogram_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_addShelf = (Button) findViewById(R.id.btn_addShelf);
        btn_addSKU = (Button) findViewById(R.id.btn_addSKU);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_stockFacingPlanogramTracker);

        Intent intent = getIntent();
        brand = intent.getStringExtra("brand");
        brand_id = intent.getStringExtra("brand_id");
        company_id = intent.getStringExtra("company_id");
        sub_category = intent.getStringExtra("sub_category");
        sub_category_id = intent.getStringExtra("sub_category_id");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        btn_addSKU.setEnabled(false);

        btn_addShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view1) {
                final Dialog dialog = new Dialog(StockFacing_PlanogramTrackerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_stock_facing_planogram_tracker);

                final EditText ed_shelf_position = (EditText) dialog.findViewById(R.id.ed_shelf_position);
                Button addShelf = (Button) dialog.findViewById(R.id.dialog_btn_addShelf);
                Button cancel = (Button) dialog.findViewById(R.id.dialog_btn_cancel);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                addShelf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addShelfPosition = ed_shelf_position.getText().toString().trim();

                        if (!addShelfPosition.equals("")) {
                            dialog.dismiss();

                            btn_addSKU.setEnabled(true);
                        } else {
                            /*Snackbar.make(view1, getResources().getString(R.string.empty_field), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();*/
                            Toast.makeText(StockFacing_PlanogramTrackerActivity.this,
                                    getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        String s = addShelfPosition;

        btn_addSKU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StockFacing_PlanogramTrackerActivity.this, "Add SKU", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
