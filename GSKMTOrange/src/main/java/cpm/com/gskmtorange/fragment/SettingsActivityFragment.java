package cpm.com.gskmtorange.fragment;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends Fragment {

    ArrayList<SettingsGetterSetter> settingsList;

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RecyclerView rec = (RecyclerView) view.findViewById(R.id.rec_settings);

        SettingsGetterSetter settings = new SettingsGetterSetter();
        settings.setName(getString(R.string.select_language_item));
        settings.setIcon(R.mipmap.entry_grey);
        SelectLanguageFragment selectLanguageFragment = new SelectLanguageFragment();
        settings.setFragment(selectLanguageFragment);

        settingsList = new ArrayList<>();
        settingsList.add(settings);

        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        SettingsAdapter settingsAdapter = new SettingsAdapter();
        rec.setAdapter(settingsAdapter);

        return view;
    }

    class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final SettingsGetterSetter mItem = settingsList.get(position);
            holder.tv_settings.setText(mItem.getName());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment nextFrag= mItem.getFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment, nextFrag,"Settings")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return settingsList.size();
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

    class SettingsGetterSetter{

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


}
