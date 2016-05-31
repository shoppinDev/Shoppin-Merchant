package com.shoppin.merchant.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shoppin.merchant.R;
import com.shoppin.merchant.login.EmailValidator;
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
 * {@link ShopInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText etName,etAddressLine1,etAddressLine2,etCity,etZipCode,etCountry,etState,etLocationLatitude,etLocationLongitude,etEmailId,etMobile;

    Button btnAddShop;

    boolean isOpenFromAddDeal;

    Handler messageHandler;

    public ShopInfoFragment() {
        // Required empty public constructor
    }

    public void setMessageHandler(Handler handler){
        messageHandler = handler;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopInfoFragment newInstance(String param1, String param2) {
        ShopInfoFragment fragment = new ShopInfoFragment();
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
            isOpenFromAddDeal = getArguments().getBoolean("from_deal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_info, container, false);

        etAddressLine1 = (EditText) view.findViewById(R.id.input_address_1);
        etAddressLine2 = (EditText) view.findViewById(R.id.input_address_2);
        etCity = (EditText) view.findViewById(R.id.input_city);
        etCountry = (EditText) view.findViewById(R.id.input_country);
        etEmailId = (EditText) view.findViewById(R.id.input_shop_email);
        etLocationLatitude = (EditText) view.findViewById(R.id.input_shop_latitude);
        etLocationLongitude = (EditText) view.findViewById(R.id.input_shop_longitude);
        etMobile = (EditText) view.findViewById(R.id.input_shop_contact);
        etName = (EditText) view.findViewById(R.id.input_name);
        etState = (EditText) view.findViewById(R.id.input_state);
        etZipCode = (EditText) view.findViewById(R.id.input_zip);

        btnAddShop = (Button) view.findViewById(R.id.btnAddShop);
        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etName.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a shop name",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etAddressLine1.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a shop address",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etMobile.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a Contact number",Toast.LENGTH_LONG).show();
                    return;
                }


                if(etEmailId.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a Email address",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isEmailValid(etEmailId.getText().toString())){
                    Toast.makeText(getActivity(),"Email address is incorrect",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etCity.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a City",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etState.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a state",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etCountry.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a Country",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etZipCode.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter a Zip code",Toast.LENGTH_LONG).show();
                    return;
                }

                if(ModuleClass.isInternetOn){
                    new AddShopTask(etName.getText().toString(),
                            etAddressLine1.getText().toString()+","+etAddressLine2.getText().toString(),
                            etEmailId.getText().toString(),
                            etMobile.getText().toString(),
                            etLocationLatitude.getText().toString(),
                            etLocationLongitude.getText().toString(),
                            etCountry.getText().toString(),
                            etCity.getText().toString(),
                            etState.getText().toString(),
                            etZipCode.getText().toString()).execute();
                }

            }
        });

        return view;
    }

    private boolean isEmailValid(String paramString) {
        return EmailValidator.isValidEmail(paramString);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void clearField(){
        etZipCode.setText("");
        etState.setText("");
        etCity.setText("");
        etCountry.setText("");
        etAddressLine1.setText("");
        etAddressLine2.setText("");
        etEmailId.setText("");
        etLocationLatitude.setText("");
        etLocationLongitude.setText("");
        etName.setText("");
        etMobile.setText("");
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

    class AddShopTask extends AsyncTask<Void,Void,Void> {

        String email,mobileNo,address,country,city,zipCode,state,latitude,longitude,name;
        boolean success;
        String responseError,resultMessage;
        ProgressDialog dialog;
        public AddShopTask(String name,String address,String email,String mobileNo,String latitude,String longitude,String country,String city,String state,String zipCode){
            this.email = email;
            this.mobileNo = mobileNo;
            this.address = address;
            this.country = country;
            this.city = city;
            this.zipCode = zipCode;
            this.latitude = latitude;
            this.longitude = longitude;
            this.state = state;
            this.name = name;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                clearField();
                if(!resultMessage.equals("")){
                    Toast.makeText(getActivity(),resultMessage,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"Shop successfully added",Toast.LENGTH_LONG).show();
                }
                if(isOpenFromAddDeal){
                    getTargetFragment().onActivityResult(AddDealFragment.REQUEST_CODE_ADD_SHOP,Activity.RESULT_OK,null);
                    getFragmentManager().popBackStack();
                }else{
                    if(messageHandler != null) {
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putBoolean("shop_added", true);
                        msg.setData(b);
                        messageHandler.sendMessage(msg);
                    }
                }

            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "addshop"));
            inputArray.add(new BasicNameValuePair("userid", ModuleClass.MERCHANT_ID));
            inputArray.add(new BasicNameValuePair("name", name));
            inputArray.add(new BasicNameValuePair("shop_addres", address));
            inputArray.add(new BasicNameValuePair("shop_latitude", latitude));
            inputArray.add(new BasicNameValuePair("shop_longitude", longitude));
            inputArray.add(new BasicNameValuePair("shop_email", email));
            inputArray.add(new BasicNameValuePair("shop_mobile", mobileNo));
            inputArray.add(new BasicNameValuePair("shop_city", city));
            inputArray.add(new BasicNameValuePair("shop_state", state));
            inputArray.add(new BasicNameValuePair("shop_zip", zipCode));
            inputArray.add(new BasicNameValuePair("shop_country", country));

            Log.d("Add shop input ",inputArray.toString());

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Add shop ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    resultMessage = dataArray.getString(0);

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
