package com.ilink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ilink.adapter.askCreditAdapter;
import com.ilink.model.creditAsk;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DemandesCreditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DemandesCreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemandesCreditFragment extends Fragment implements AbsListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String url = "http://ilink-app.com/app/";
    private MaterialDialog pDialog;
    private List<creditAsk> askCreditList = new ArrayList<creditAsk>();
    private ListView listView;
    private askCreditAdapter adapter;
    private View demandeView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public DemandesCreditFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemandesCreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DemandesCreditFragment newInstance(String param1, String param2) {
        DemandesCreditFragment fragment = new DemandesCreditFragment();
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
        demandeView = inflater.inflate(R.layout.fragment_demandes_credit, container, false);
        mListView = (ListView) demandeView.findViewById(R.id.listMemberGroup);
        mSwipeRefreshLayout = (SwipeRefreshLayout) demandeView.findViewById(R.id.swipeRefreshLayoutMemberGroup);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        int nombre_membres = 10;

        for (nombre_membres = 0 ; nombre_membres < 10 ; nombre_membres++) {
            creditAsk members = new creditAsk();
            members.setNom_membre("Capp" + nombre_membres++);
            members.setMontant("20000" + nombre_membres++);
            members.setAdresse_membre("Nzeng-Ayong" + nombre_membres++);
            // adding movie to movies array
            askCreditList.add(members);

        }


        adapter = new askCreditAdapter(getActivity(), askCreditList);


        mListView.setAdapter(adapter);


        //Get Content
        /*



        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Chargement des membres...");
        pDialog.show();



        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                memberGroup members = new memberGroup();
                                members.setName(String.valueOf(Html.fromHtml(obj.getString("title"))));
                                members.setAmount(obj.getString("image"));
                                members.setAdress(obj.getString("content"));
                                // adding movie to movies array
                                memberGroupList.add(members);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


*/


        // End get content


        mListView.setOnItemClickListener(this);


        return demandeView;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.

        creditAsk m = askCreditList.get(position);

        Intent i = new Intent(getActivity(), MemberDetailActivity.class);
        // i.putExtra("flag", String.valueOf(movieList.get(position)));
        //i.putExtra("flag", parent.getItemAtPosition(position).toString());
        i.putExtra("titre", String.valueOf(m.getNom_membre()));
        i.putExtra("image", String.valueOf(m.getMontant()));
        i.putExtra("contenu", String.valueOf(m.getAdresse_membre()));

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public void onRefresh() {
        //appellé lors de l'action Pull To Refresh
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshNews();
                //avertie le SwipeRefreshLayout que la mise à jour a été effectuée
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, -1);
    }

    private void refreshNews() {
        // Newly Added
        mListView = (ListView) demandeView.findViewById(R.id.listMemberGroup);
        int nombre_membres = 10;

        for (nombre_membres = 0 ; nombre_membres < 10 ; nombre_membres++) {
            creditAsk members = new creditAsk();
            members.setNom_membre("Capp" + nombre_membres++);
            members.setMontant("20000" + nombre_membres++);
            members.setAdresse_membre("Nzeng-Ayong" + nombre_membres++);
            // adding movie to movies array
            askCreditList.add(members);

        }

        adapter = new askCreditAdapter(getActivity(), askCreditList);
        mListView.setAdapter(adapter);
        // changing action bar color
       /* getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));*/

        // Creating volley request obj

        // Creating volley request obj
        /*JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                memberGroup members = new memberGroup();
                                members.setName(String.valueOf(Html.fromHtml(obj.getString("title"))));
                                members.setAmount(obj.getString("image"));
                                members.setAdress(obj.getString("content"));
                                // adding movie to movies array
                                memberGroupList.add(members);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
        */

        mListView.setOnItemClickListener(this);
    }
}
