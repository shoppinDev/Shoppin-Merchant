package com.shoppin.merchant.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.shoppin.merchant.model.DealDataModel;
import com.shoppin.merchant.model.ShopModel;
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
 * {@link MyDealFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDealFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<DealDataModel> dealArrayList = new ArrayList<>();

    EditDealListAdapter adapter;

    ListView listMyDeal;

    TextView tvEmptyDeal;

    public MyDealFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDealFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDealFragment newInstance(String param1, String param2) {
        MyDealFragment fragment = new MyDealFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_deal, container, false);

        listMyDeal = (ListView) view.findViewById(R.id.listMyDeal);

        tvEmptyDeal = (TextView) view.findViewById(R.id.tvEmptyDeal);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ModuleClass.isInternetOn){
            new GetDealTask().execute();
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

    class GetDealTask extends AsyncTask<Void,Void,Void> {

        boolean success;
        String responseError;
        ProgressDialog dialog;
        public GetDealTask(){

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                if(dealArrayList.size() > 0){
                    adapter = new EditDealListAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,dealArrayList,getFragmentManager());
                    listMyDeal.setAdapter(adapter);
                }else{
                    tvEmptyDeal.setVisibility(View.VISIBLE);
                }

            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "my_deal"));
            inputArray.add(new BasicNameValuePair("merchantid", ModuleClass.MERCHANT_ID));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Get My Deal ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        DealDataModel data = new DealDataModel(
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
                                object.getString("category_name"),
                                object.getString("subcategory_name"),
                                object.getString("merchant_name"),
                                object.getString("shop_name")
                        );

                        data.setQrImagePath(object.getString("qr_image"));

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
