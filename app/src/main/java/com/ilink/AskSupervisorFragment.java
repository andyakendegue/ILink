package com.ilink;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;

public class AskSupervisorFragment extends Fragment {
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_NOMBRE_MEMBRES = "membres";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    private static final String REGISTER_URL = "http://ilink-app.com/app/";
    private Spinner ListMembres;
    private Button btn_ask_supervisor;
    private String e_membres;
    private String[] membresItem;

    public AskSupervisorFragment() {
        this.e_membres = "0";
        this.ListMembres = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C1558R.layout.fragment_add_supervisor, container, false);
        this.ListMembres = (Spinner) rootView.findViewById(C1558R.id.list_nbr_member);
        List<String> listeMembres = new ArrayList();
        this.membresItem = getResources().getStringArray(C1558R.array.nombre_membres);
        for (Object add : this.membresItem) {
            listeMembres.add(add);
        }
        ArrayAdapter<String> membresAdapter = new ArrayAdapter(getActivity(), 17367048, listeMembres);
        membresAdapter.setDropDownViewResource(17367049);
        this.ListMembres.setAdapter(membresAdapter);
        this.ListMembres.setOnItemSelectedListener(new 1(this));
        this.btn_ask_supervisor = (Button) rootView.findViewById(C1558R.id.btn_ask_supervisor);
        this.btn_ask_supervisor.setOnClickListener(new 2(this));
        return rootView;
    }

    private void registerUser() {
        String phone = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_PHONE, "Not Available");
        if (this.e_membres != null) {
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(new 5(this, 1, REGISTER_URL, new 3(this, phone), new 4(this), phone));
            return;
        }
        Toast.makeText(getActivity().getApplicationContext(), "Vous ne pouvez avoir aucun membre!", 1).show();
    }
}
