package cpm.com.gskmtorange.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;


import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectLanguageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectLanguageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectLanguageFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;

    Button btn_lang_1, btn_lang_2;

    ArrayList<String> language, culture_id;

    LoginGetterSetter login_data;

    public SelectLanguageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectLanguageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectLanguageFragment newInstance(String param1, String param2) {
        SelectLanguageFragment fragment = new SelectLanguageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_language, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();

        getDataFromSharedPreferences();

        btn_lang_1 = (Button) view.findViewById(R.id.btn_language_one);
        btn_lang_2 = (Button) view.findViewById(R.id.btn_language_two);


      /*  login_data = (LoginGetterSetter) getIntent().getSerializableExtra(CommonString.KEY_LOGIN_DATA);
        language = login_data.getCULTURE_NAME();
        culture_id = login_data.getCULTURE_ID();
*/
        if (language.size() > 1) {

            btn_lang_1.setText(language.get(0));
            btn_lang_2.setText(language.get(1));

            String lang = preferences.getString(CommonString.KEY_LANGUAGE, "");

            if(lang.equals(language.get(0))){
                btn_lang_1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn_lang_2.setBackgroundColor(getResources().getColor(R.color.grey_background));
            }
            else if(lang.equals(language.get(1))){
                btn_lang_1.setBackgroundColor(getResources().getColor(R.color.grey_background));
                btn_lang_2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            btn_lang_1.setOnClickListener(this);
            btn_lang_2.setOnClickListener(this);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String selected_lang, String culture_id, String notice_url) {
        if (mListener != null) {

            mListener.onFragmentInteraction(selected_lang,culture_id,notice_url);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_language_one:

                //selected_flag = true;
                onButtonPressed(language.get(0),culture_id.get(0),login_data.getNOTICE_URL().get(0));

                updateResources(getActivity(), language.get(0));

                btn_lang_1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn_lang_2.setBackgroundColor(getResources().getColor(R.color.grey_background));

                editor.putString(CommonString.KEY_LANGUAGE, language.get(0));
                editor.putString(CommonString.KEY_CULTURE_ID, culture_id.get(0));
                editor.putString(CommonString.KEY_NOTICE_BOARD_LINK, login_data.getNOTICE_URL().get(0));
                editor.commit();

                break;

            case R.id.btn_language_two:

                //selected_flag = true;

                onButtonPressed(language.get(1),culture_id.get(1),login_data.getNOTICE_URL().get(1));

                updateResources(getActivity(), language.get(1));

                btn_lang_1.setBackgroundColor(getResources().getColor(R.color.grey_background));
                btn_lang_2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                editor.putString(CommonString.KEY_LANGUAGE, language.get(1));
                editor.putString(CommonString.KEY_CULTURE_ID, culture_id.get(1));
                editor.putString(CommonString.KEY_NOTICE_BOARD_LINK, login_data.getNOTICE_URL().get(1));
                editor.commit();

                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String lang, String culture_id, String notice);
    }

    private void getDataFromSharedPreferences(){
        Gson gson = new Gson();
        login_data = new LoginGetterSetter();

        String jsonPreferences = preferences.getString(CommonString.KEY_LOOGIN_PREF, "");

        Type type = new TypeToken<LoginGetterSetter>() {}.getType();
        login_data = gson.fromJson(jsonPreferences, type);

        language = login_data.getCULTURE_NAME();
        culture_id = login_data.getCULTURE_ID();

        //return ;
    }

    private static boolean updateResources(Context context, String language) {
        /*String lang;

        if (language.equalsIgnoreCase("English")) {
            lang = "EN";
        } else if (language.equalsIgnoreCase("ARABIC-KSA")) {
            lang = "AR";
        } else {
            lang = "TR";
        }*/

        String lang;

        if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ENGLISH)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_ENGLISH;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_ARABIC_KSA;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_TURKISH)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_TURKISH;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_OMAN)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_OMAN;
        }else{
            lang = CommonString.KEY_RETURN_LANGUAGE_DEFAULT;
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }
}
