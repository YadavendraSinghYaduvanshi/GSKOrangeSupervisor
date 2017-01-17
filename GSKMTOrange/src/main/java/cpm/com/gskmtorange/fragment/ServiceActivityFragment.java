package cpm.com.gskmtorange.fragment;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.dailyentry.ServiceActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServiceActivityFragment extends Fragment {

    public ServiceActivityFragment() {
    }

    ArrayList<ServiceGetterSetter> serviceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        RecyclerView rec = (RecyclerView) view.findViewById(R.id.rec_settings);

        ServiceGetterSetter service = new ServiceGetterSetter();
        service.setName(getString(R.string.export_database));
        service.setIcon(R.mipmap.entry_grey);
        //SelectLanguageFragment selectLanguageFragment = new SelectLanguageFragment();
        service.setFragment(null);

        serviceList = new ArrayList<>();
        serviceList.add(service);

        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        ServiceAdapter serviceAdapter = new ServiceAdapter();
        rec.setAdapter(serviceAdapter);

        return view;
    }

    class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ServiceGetterSetter mItem = serviceList.get(position);
            holder.tv_settings.setText(mItem.getName());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment nextFrag= mItem.getFragment();

                    if(nextFrag != null){
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, nextFrag,"Settings")
                                .addToBackStack(null)
                                .commit();
                    }
                    else{
                        showExportDialog();
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return serviceList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public CardView cardView;
            public TextView tv_settings;

            public ViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_layout);
                tv_settings = (TextView) itemView.findViewById(R.id.tv_settings);
            }
        }
    }

    class ServiceGetterSetter{

        String name;
        int icon;
        Fragment fragment;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }


        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }

    public void showExportDialog(){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to take the backup of your data")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressWarnings("resource")
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            File file = new File(Environment
                            .getExternalStorageDirectory(),
                                    "gsk_orange_backup");
                            if (!file.isDirectory()) {
                                file.mkdir();
                            }

                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();

                            if (sd.canWrite()) {
                                long date = System.currentTimeMillis();

                                SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                                String dateString = sdf.format(date);

                                String currentDBPath = "//data//cpm.com.gskmtorange//databases//" + GSKOrangeDB.DATABASE_NAME;
                                String backupDBPath = "GSKMT_ORANGE_Database_backup" + dateString.replace('/', '-');

                                String path = Environment.getExternalStorageDirectory().getPath()+ "/gsk_orange_backup";

                                File currentDB = new File(data, currentDBPath);
                                File backupDB = new File(path, backupDBPath);

                                //Snackbar.make(rec_store_data, "Database Exported Successfully", Snackbar.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), getString(R.string.data_exported_successfully), Toast.LENGTH_SHORT).show();

                                if (currentDB.exists()) {
                                    @SuppressWarnings("resource")
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }
}
