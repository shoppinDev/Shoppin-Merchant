package com.shoppin.merchant.myaccount;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppin.merchant.R;
import com.shoppin.merchant.model.DraftsDataModel;
import com.shoppin.merchant.model.PurchaseDataModel;
import com.shoppin.merchant.util.JSONParser;
import com.shoppin.merchant.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DraftsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DraftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DraftsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<DraftsDataModel> dealArrayList = new ArrayList<>();

    DraftsListAdapter adapter;

    ListView listDrafts;

    TextView tvEmpty;

    public DraftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DraftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DraftsFragment newInstance(String param1, String param2) {
        DraftsFragment fragment = new DraftsFragment();
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
        View view = inflater.inflate(R.layout.fragment_drafts, container, false);
        listDrafts = (ListView) view.findViewById(R.id.listDrafts);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        return view;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();

            if(b.getBoolean("deal_added")){
                if(ModuleClass.isInternetOn){
                    dealArrayList.clear();
                    new GetDraftsTask().execute();
                }
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(ModuleClass.isInternetOn){

            new GetDraftsTask().execute();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class GetDraftsTask extends AsyncTask<Void,Void,Void> {

        boolean success;
        String responseError;
        ProgressDialog dialog;
        public GetDraftsTask(){
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                if(dealArrayList.size() > 0){
                    adapter = new DraftsListAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,dealArrayList,getFragmentManager(),handler);
                    listDrafts.setAdapter(adapter);
                }else{
                    tvEmpty.setVisibility(View.VISIBLE);
                }

            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "my_drafts"));
            inputArray.add(new BasicNameValuePair("merchantid", ModuleClass.MERCHANT_ID));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Get Shop ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        DraftsDataModel data = new DraftsDataModel(
                                object.getString("save_id"),
                                object.getString("customer_id"),
                                object.getString("customer_name"),
                                object.getString("customer_email"),
                                object.getString("customer_password"),
                                object.getString("customer_mobile"),
                                object.getString("is_mobile_verify"),
                                object.getString("deal_id"),
                                object.getString("merchant_id"),
                                object.getString("shop_id"),
                                object.getString("deal_category"),
                                object.getString("deal_subcategory"),
                                object.getString("deal_title"),
                                object.getString("deal_description"),
                                object.getString("deal_startdate"),
                                object.getString("deal_enddate"),
                                object.getString("deal_amount"),
                                object.getString("all_days"),
                                object.getString("discount_value"),
                                object.getString("discount_type"),
                                object.getString("location"),
                                object.getString("deal_usage"),
                                object.getString("is_active"),
                                object.getString("added_date"),
                                object.getString("merchant_name")
                        );

                        dealArrayList.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                success = false;
                responseError = "There is some problem in server connection";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity(),R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }
}
