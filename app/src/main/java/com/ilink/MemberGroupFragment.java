package com.ilink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ilink.adapter.memberGroupAdapter;
import com.ilink.app.AppController;
import com.ilink.lib.DatabaseHandler;
import com.ilink.model.memberGroup;
import com.ilink.model.usersModel;
import java.util.ArrayList;
import java.util.List;

public class MemberGroupFragment extends Fragment implements OnItemClickListener, OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String DELETE_URL = "http://ilink-app.com/app/select/delete.php";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    private static final String TAG;
    private memberGroupAdapter adapter;
    DatabaseHandler db;
    private String emailRemote;
    private ImageButton imgBtnDown;
    private ImageButton imgBtnUp;
    private ListView listView;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<memberGroup> memberGroupList;
    private ListView memberListView;
    private View memberView;
    private MaterialDialog pDialog;
    private String phoneRemote;
    private SharedPreferences sharedPreferences;

    static {
        TAG = MainActivity.class.getSimpleName();
    }

    public MemberGroupFragment() {
        this.memberGroupList = new ArrayList();
    }

    public static MemberGroupFragment newInstance(int sectionNumber) {
        MemberGroupFragment fragment = new MemberGroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.memberView = inflater.inflate(C1558R.layout.fragment_member_group, container, false);
        this.memberListView = (ListView) this.memberView.findViewById(C1558R.id.listMemberGroupAll);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) this.memberView.findViewById(C1558R.id.swipeRefreshLayoutMemberGroup);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        this.imgBtnUp = (ImageButton) this.memberView.findViewById(C1558R.id.imgBtnUp);
        this.imgBtnDown = (ImageButton) this.memberView.findViewById(C1558R.id.imgBtnDown);
        this.imgBtnUp.setOnClickListener(new 1(this));
        this.imgBtnDown.setOnClickListener(new 2(this));
        new CheckUsers(this, null).execute(new String[0]);
        this.db = new DatabaseHandler(getActivity());
        this.memberListView.setOnItemClickListener(this);
        return this.memberView;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.phoneRemote = ((TextView) view.findViewById(C1558R.id.textMemberGroupPhone)).getText().toString();
        deleteUser(this.phoneRemote);
    }

    public void onRefresh() {
        this.mSwipeRefreshLayout.postDelayed(new 3(this), 5000);
    }

    private void deleteUser(String phone) {
        Builder alertDialogBuilder = new Builder(getActivity());
        alertDialogBuilder.setMessage("Etes vous sur de vouloir supprimer le " + phone + "?");
        alertDialogBuilder.setPositiveButton((CharSequence) "Oui", new 4(this));
        alertDialogBuilder.setNegativeButton((CharSequence) "Non", new 5(this));
        alertDialogBuilder.create().show();
    }

    private void deleteFromBdd() {
        Volley.newRequestQueue(getActivity()).add(new 8(this, 1, DELETE_URL, new 6(this), new 7(this)));
    }

    private void users() {
        this.pDialog = new MaterialDialog.Builder(getActivity()).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement marqueurs...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new 9(this), new 10(this));
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void refreshUsers() {
        this.memberGroupList.clear();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new 11(this), new 12(this));
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void getlocalUsersAsc() {
        this.memberGroupList.clear();
        ArrayList<usersModel> users = this.db.getUsersDetailsAsc();
        int usersLength = users.size();
        String email = this.sharedPreferences.getString(KEY_EMAIL, "Not Available");
        String phone = this.sharedPreferences.getString(KEY_PHONE, "Not Available");
        String network = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_NETWORK, "Not Available");
        String code_parrain = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_MEMBER_CODE, "Not Available");
        String category = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_CATEGORY, "Not Available");
        String categorie;
        if (category == "hyper") {
            categorie = "super";
        } else if (category == "super") {
            categorie = "geolocated";
        }
        for (int i = 0; i < usersLength; i++) {
            usersModel u = (usersModel) users.get(i);
            if (u.getLatitude() != null && u.getNetwork().equals(network) && u.getCode_parrain().equals(code_parrain)) {
                memberGroup members = new memberGroup();
                members.setName(String.valueOf(Html.fromHtml(u.getLastname())));
                members.setBalance(u.getBalance());
                members.setAdress(u.getName());
                members.setPhone(u.getPhone());
                this.memberGroupList.add(members);
            }
        }
        if (this.memberGroupList != null) {
            this.adapter = new memberGroupAdapter(getActivity(), this.memberGroupList);
        }
        this.memberListView.setAdapter(this.adapter);
    }

    private void getlocalUsersDsc() {
        this.memberGroupList.clear();
        ArrayList<usersModel> users = this.db.getUsersDetailsDsc();
        int usersLength = users.size();
        String email = this.sharedPreferences.getString(KEY_EMAIL, "Not Available");
        String phone = this.sharedPreferences.getString(KEY_PHONE, "Not Available");
        String network = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_NETWORK, "Not Available");
        String code_parrain = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_MEMBER_CODE, "Not Available");
        String category = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_CATEGORY, "Not Available");
        String categorie;
        if (category == "hyper") {
            categorie = "super";
        } else if (category == "super") {
            categorie = "geolocated";
        }
        for (int i = 0; i < usersLength; i++) {
            usersModel u = (usersModel) users.get(i);
            if (u.getLatitude() != null && u.getNetwork().equals(network) && u.getCode_parrain().equals(code_parrain)) {
                memberGroup members = new memberGroup();
                members.setName(String.valueOf(Html.fromHtml(u.getLastname())));
                members.setBalance(u.getBalance());
                members.setAdress(u.getName());
                members.setPhone(u.getPhone());
                this.memberGroupList.add(members);
            }
        }
        if (this.memberGroupList != null) {
            this.adapter = new memberGroupAdapter(getActivity(), this.memberGroupList);
        }
        this.memberListView.setAdapter(this.adapter);
    }

    private void getlocalUsers() {
        ArrayList<usersModel> users = this.db.getUsersDetails();
        int usersLength = users.size();
        String email = this.sharedPreferences.getString(KEY_EMAIL, "Not Available");
        String phone = this.sharedPreferences.getString(KEY_PHONE, "Not Available");
        String network = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_NETWORK, "Not Available");
        String code_membre = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_MEMBER_CODE, "Not Available");
        String category = this.sharedPreferences.getString(RegisterSimpleActivity.KEY_CATEGORY, "Not Available");
        String categorie;
        if (category == "hyper") {
            categorie = "super";
        } else if (category == "super") {
            categorie = "geolocated";
        }
        for (int i = 0; i < usersLength; i++) {
            usersModel u = (usersModel) users.get(i);
            if (u.getLatitude() != null && u.getNetwork().equals(network) && u.getCode_parrain().equals(code_membre)) {
                memberGroup members = new memberGroup();
                members.setName(String.valueOf(Html.fromHtml(u.getLastname())));
                members.setBalance(u.getBalance());
                members.setAdress(u.getName());
                members.setPhone(u.getPhone());
                this.memberGroupList.add(members);
            }
        }
        if (this.memberGroupList != null) {
            this.adapter = new memberGroupAdapter(getActivity(), this.memberGroupList);
        }
        this.memberListView.setAdapter(this.adapter);
    }

    private void hidePDialog() {
        if (this.pDialog != null) {
            this.pDialog.dismiss();
            this.pDialog = null;
        }
    }
}
