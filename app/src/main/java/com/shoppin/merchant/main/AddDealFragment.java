package com.shoppin.merchant.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shoppin.merchant.R;
import com.shoppin.merchant.model.CategoryDataModel;
import com.shoppin.merchant.model.ShopModel;
import com.shoppin.merchant.model.SubCategoryDataModel;
import com.shoppin.merchant.util.JSONParser;
import com.shoppin.merchant.util.ModuleClass;
import com.shoppin.merchant.util.MultiSelectSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddDealFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddDealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AddDealFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView listShop;

    ArrayList<ShopModel> shopList = new ArrayList<>();

    Button btnAddShop;

    Spinner spinnerCategory,spinnerSubCategory;

    MultiSelectSpinner spinnerDays;

    EditText etDealTitle,etDescription,etStartTime,etEndTime,etDealPrice,etDiscPrice,etEndDate,etOffPrice,etOrgPrice,etDiscOffr;

    public static EditText etStartDate;

    TextView tvShopEmpty;

    Button btnAddDeal;

    RadioGroup rgDealUsage,rgDealLocation,rgDiscount;

    RadioButton rbSingle,rbMultiple,rbShop,rbAllShop,rbAmount,rbPercentage;

    String selectedCategory,selectedSubCategory;

    ArrayList<CategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    ArrayList<SubCategoryDataModel> subCategoryDataModelArrayList = new ArrayList<>();

    ArrayList<String> categoryNameArrayList = new ArrayList<>();
    ArrayList<String> subCategoryNameArrayList = new ArrayList<>();

    ShopListAdapter adapter;

    FrameLayout frameListContainer;

    String selectedDealUsage,selectedDealLocation,selectedDiscount;

    public static final int REQUEST_CODE_ADD_SHOP = 1001;

    String[] daysArray = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    Handler messageHandler;

    public static boolean isStartDateClicked,isEndDateClicked;

    public AddDealFragment() {
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
     * @return A new instance of fragment AddDealFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDealFragment newInstance(String param1, String param2) {
        AddDealFragment fragment = new AddDealFragment();
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
        setUpData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_deal, container, false);

        listShop = (ListView) view.findViewById(R.id.listShop);
        listShop.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        frameListContainer = (FrameLayout) view.findViewById(R.id.frameLayListContainer);

        btnAddShop = (Button) view.findViewById(R.id.btnAddShop);
        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShopInfoFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                fragment.setTargetFragment(AddDealFragment.this,REQUEST_CODE_ADD_SHOP);
                ft.replace(R.id.fragment_container,fragment);
                ft.commit();
            }
        });

        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryDataModelArrayList.get(position).getCategoryId();
                subCategoryNameArrayList.clear();
                subCategoryDataModelArrayList.clear();

                if(!selectedCategory.equals("")){
                    new GetSubCategoryTask(selectedCategory).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSubCategory = (Spinner) view.findViewById(R.id.spinnerSubCategory);
        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCategory = subCategoryDataModelArrayList.get(position).getSubCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDays = (MultiSelectSpinner) view.findViewById(R.id.spinnerDays);
        spinnerDays.setItems(daysArray);

        tvShopEmpty = (TextView) view.findViewById(R.id.tvShopEmpty);

        etDescription = (EditText) view.findViewById(R.id.input_deal_desc);
        etDealTitle = (EditText) view.findViewById(R.id.input_deal_title);
        etDiscPrice = (EditText) view.findViewById(R.id.input_disc_value);
        etOffPrice = (EditText) view.findViewById(R.id.input_off_price);
        etOrgPrice = (EditText) view.findViewById(R.id.input_original_price);

        etOrgPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(etDiscOffr.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please enter discount percentage",Toast.LENGTH_LONG).show();
                    etDiscOffr.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etOrgPrice.getText().toString().equals("")) {
                    if (!etDiscOffr.getText().toString().equals("")) {
                        long percentage = Long.parseLong(etDiscOffr.getText().toString());
                        long originalPrice = Long.parseLong(etOrgPrice.getText().toString());
                        long discPrice = originalPrice - (originalPrice * percentage)/100;

                        etOffPrice.setText(String.valueOf(discPrice));

                        long orgPrice = Long.parseLong(etOrgPrice.getText().toString());
                        if(orgPrice == 0){
                            Toast.makeText(getActivity(), "Original price should not be zero", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    etOffPrice.setText("");
                }
            }
        });

        etDiscOffr = (EditText) view.findViewById(R.id.input_disc_offr);
        etDiscOffr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etDiscOffr.getText().toString().equals("")) {
                    int discount = Integer.parseInt(etDiscOffr.getText().toString());
                    if (discount > 100) {
                        Toast.makeText(getActivity(), "Discount percentage(%) should not be more than 100", Toast.LENGTH_LONG).show();
                    }
                }

                if(!etOrgPrice.getText().toString().equals("")){
                    if (!etDiscOffr.getText().toString().equals("")) {
                        long percentage = Long.parseLong(etDiscOffr.getText().toString());
                        long originalPrice = Long.parseLong(etOrgPrice.getText().toString());
                        long discPrice = originalPrice - (originalPrice * percentage)/100;

                        etOffPrice.setText(String.valueOf(discPrice));
                    }
                }
            }
        });

        etStartTime = (EditText) view.findViewById(R.id.input_start_time);
        etStartTime.setClickable(true);
        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Notification","Start Time click");
                showTimePickerDialog(etStartTime);
            }
        });

        etStartDate = (EditText) view.findViewById(R.id.input_start_date);
        etStartDate.setClickable(true);
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDateClicked = true;
                isEndDateClicked = false;
                Log.v("Notification","Start Date click");
                showDatePickerDialog(etStartDate);
            }
        });

        etEndDate = (EditText) view.findViewById(R.id.input_end_date);
        etEndDate.setClickable(true);
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etStartDate.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Start Date field should not be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                isEndDateClicked = true;
                isStartDateClicked = false;
                showDatePickerDialog(v);

            }
        });

        etEndTime = (EditText) view.findViewById(R.id.input_end_time);
        etEndTime.setClickable(true);
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        etDealPrice = (EditText) view.findViewById(R.id.input_deal_price);

        rgDealLocation = (RadioGroup) view.findViewById(R.id.rgDealLocation);
        rgDealUsage = (RadioGroup) view.findViewById(R.id.rgDealUsage);
        rgDiscount = (RadioGroup) view.findViewById(R.id.rgDiscount);

        rbAllShop = (RadioButton) view.findViewById(R.id.rbAllShop);
        rbAllShop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedDealLocation = "1";
            }
        });

        rbAmount = (RadioButton) view.findViewById(R.id.rbAmount);
        rbAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedDiscount = "0";
            }
        });
        rbAmount.setChecked(true);


        rbMultiple = (RadioButton) view.findViewById(R.id.rbMultiple);
        rbMultiple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedDealUsage = "1";
            }
        });

        rbPercentage = (RadioButton) view.findViewById(R.id.rbPercentage);
        rbPercentage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedDiscount = "1";
            }
        });

        rbShop = (RadioButton) view.findViewById(R.id.rbShop);
        rbShop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedDealLocation = "0";
            }
        });
        rbShop.setChecked(true);


        rbSingle = (RadioButton) view.findViewById(R.id.rbSingle);
        rbSingle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedDealUsage = "0";
            }
        });
        rbSingle.setChecked(true);

        btnAddDeal = (Button) view.findViewById(R.id.btnAddDeal);
        btnAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter != null) {
                    if (adapter.getCheckedCount() <= 0) {
                        Toast.makeText(getActivity(), "Select at least one shop to add deal", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(getActivity(), "Select at least one shop to add deal", Toast.LENGTH_LONG).show();
                    return;
                }

                if(adapter != null) {
                    if (adapter.getCheckedCount() > 1) {
                        Toast.makeText(getActivity(), "Select only one shop to add deal", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(getActivity(), "Select at least one shop to add deal", Toast.LENGTH_LONG).show();
                    return;
                }

                if(selectedCategory == null || selectedCategory.equals("")){
                    Toast.makeText(getActivity(),"Select category to add deal",Toast.LENGTH_LONG).show();
                    return;
                }

                if(selectedSubCategory == null || selectedSubCategory.equals("")){
                    Toast.makeText(getActivity(),"Select sub category to add deal",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etDiscOffr.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Discount offer should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                int discount = Integer.parseInt(etDiscOffr.getText().toString());
                if(discount > 100){
                    Toast.makeText(getActivity(),"Discount percentage should not be more than 100",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etOrgPrice.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Original price should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                long orgPrice = Long.parseLong(etOrgPrice.getText().toString());
                if(orgPrice == 0){
                    Toast.makeText(getActivity(), "Original price should not be zero", Toast.LENGTH_LONG).show();
                    return;
                }

                if(etOffPrice.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Discount price should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etDescription.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Description should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etDealTitle.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Deal Title should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etStartTime.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Start time field should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etEndTime.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"End time field should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etStartDate.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Start Date field should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etEndDate.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"End Date field should not be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                try {
                    Date startDate = sdf.parse(etStartDate.getText().toString()+" "+etStartTime.getText().toString());
                    Date endDate = sdf.parse(etEndDate.getText().toString()+" "+etEndTime.getText().toString());
                    if(endDate.compareTo(startDate) < 0){
                        Log.v("Notification","End date time before start time");
                        Toast.makeText(getActivity(),"please enter valid end date and time",Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String shopId = adapter.getSelectedId();

                String days = "";
                List<String> daysList = spinnerDays.getSelectedStrings();
                for(int i = 0 ; i < daysList.size(); i++){
                    days += daysList.get(i)+",";
                }

                if(days.equals("")){
                    Toast.makeText(getActivity(),"Select Days for deal",Toast.LENGTH_LONG).show();
                    return;
                }

                if(ModuleClass.isInternetOn){
                    new AddDealTask(shopId,selectedCategory,selectedSubCategory,etDealTitle.getText().toString(),
                            etDescription.getText().toString(),etStartDate.getText().toString()+" "+etStartTime.getText().toString(),
                            etEndDate.getText().toString()+" "+etEndTime.getText().toString(),
                            days,selectedDealUsage,selectedDealLocation,"1",etOffPrice.getText().toString(),etOrgPrice.getText().toString()).execute();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_SHOP){
            Log.v("Notification","Shop Added successfully");
            if(ModuleClass.isInternetOn){
                new GetShopTask().execute();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListViewHeightBasedOnItems(listShop);

        if(ModuleClass.isInternetOn){
            new GetShopTask().execute();
            new GetCategoryTask().execute();
        }
    }

    public void  clearField(){
        etStartTime.setText("");
        etDiscPrice.setText("");
        etEndTime.setText("");
        etDealTitle.setText("");
        etDescription.setText("");
        etDealPrice.setText("");
        etStartDate.setText("");
        etEndDate.setText("");
    }

    public void setUpData(){

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
       /* if (context instanceof OnFragmentInteractionListener) {
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

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        EditText editText;
        public void setEditText(EditText editText){
            this.editText = editText;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Log.v("Notification","Hours of day : "+hourOfDay+" Minute : "+minute);
            if(editText != null)
                editText.setText(hourOfDay+":"+minute+":00");
        }
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setEditText((EditText) v);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        EditText editText;
        public void setEditText(EditText editText){
            this.editText = editText;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.v("Notification","Day : "+day+" month : "+month+" Year :"+year);
            //month = month+1;

            String monthStr = ""+(month+1);
            String dayStr = ""+day;

            if(monthStr.length() == 1){
                monthStr = "0"+(month+1);
            }
            Log.v("Notification","Month after adding :"+monthStr);

            if(dayStr.length() == 1){
                dayStr = "0"+day;
            }
            Log.v("Notification","Day after adding :"+dayStr);


            if(isStartDateClicked) {
                Calendar my = Calendar.getInstance();
                my.set(year, month, day);
                Log.v("Notification", "Date comparison : " + my.compareTo(Calendar.getInstance()));

                if (my.compareTo(Calendar.getInstance()) >= 0) {
                    if (editText != null) {

                        editText.setText(year + "-" + monthStr + "-" + dayStr);
                    }
                } else {
                    Toast.makeText(getActivity(),"Please select date after today date", Toast.LENGTH_LONG).show();
                }
            }else if(isEndDateClicked){
                if(compareEndDateWithStartDate(year,month,day,etStartDate,getActivity())){
                    if (editText != null) {
                        editText.setText(year + "-" + monthStr + "-" + dayStr);
                    }
                }else{
                    //Toast.makeText(getActivity(),"Please select date after Start date", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static boolean compareEndDateWithStartDate(int year,int month,int day,EditText editText,Context context){

        int startDateYear,startDateMonth,startDateDay;

        boolean isDateValid = false;

        Log.v("Notification","End Date Text : "+editText.getText().toString());
        String dateArray[] = editText.getText().toString().split("-");
        if(dateArray.length == 3){
            startDateYear = Integer.parseInt(dateArray[0]);
            startDateMonth = Integer.parseInt(dateArray[1]) - 1;
            startDateDay = Integer.parseInt(dateArray[2]);

            Log.v("Notification","End Date year : "+startDateYear+" month :"+startDateMonth+" day : "+startDateDay);

            Calendar startDateCal = Calendar.getInstance();
            startDateCal.set(startDateYear,startDateMonth,startDateDay);

            Calendar my = Calendar.getInstance();
            my.set(year,month,day);

            Log.v("Notification","Current date instance : "+startDateCal.getTime().toString());

            Log.v("Notification","Selected date instance : "+my.getTime().toString());

            Log.v("Notification","Compare end date and Current date : "+my.compareTo(startDateCal));

            if(my.compareTo(startDateCal) >= 0){
                isDateValid = true;
            }else{
                Toast.makeText(context,"End date should not be set before Start date",Toast.LENGTH_LONG).show();
                isDateValid = false;
            }
        }else{
            Toast.makeText(context,"Start date not selected properly",Toast.LENGTH_LONG).show();
        }

       return isDateValid;
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setEditText((EditText) v);
        newFragment.show(getFragmentManager(), "datePicker");
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

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + 40;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    class GetCategoryTask extends AsyncTask<Void,Void,Void> {

        boolean success;
        String responseError;
        ProgressDialog dialog;
        public GetCategoryTask(){

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
               if(categoryDataModelArrayList.size() > 0){
                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item_simple,categoryNameArrayList);
                   spinnerCategory.setAdapter(adapter);
               }

            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "category"));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Get category ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        CategoryDataModel data = new CategoryDataModel(object.getString("category_id"),object.getString("category_name"));
                        categoryDataModelArrayList.add(data);
                        categoryNameArrayList.add(object.getString("category_name"));
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

    class GetSubCategoryTask extends AsyncTask<Void,Void,Void> {

        boolean success;
        String responseError;
        ProgressDialog dialog;
        String categoryId;
        public GetSubCategoryTask(String categoryId){
            this.categoryId = categoryId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                if(categoryDataModelArrayList.size() > 0){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item_simple,subCategoryNameArrayList);
                    spinnerSubCategory.setAdapter(adapter);
                }

            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "subcategory"));
            inputArray.add(new BasicNameValuePair("category",categoryId));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Get sub category ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        SubCategoryDataModel data = new SubCategoryDataModel(object.getString("subcategory_id"),object.getString("subcategory_name"),object.getString("category_id"));
                        subCategoryDataModelArrayList.add(data);
                        subCategoryNameArrayList.add(object.getString("subcategory_name"));
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

    class AddDealTask extends AsyncTask<Void,Void,Void> {

        String shopId,dealCategory,dealSubCategory,dealTitle,dealDesc,dealStartDate,dealEndDate,dealAmount,allDays,dealUsage,location,discountType,discountValue;
        boolean success;
        String responseError;
        ProgressDialog dialog;
        public AddDealTask(String shopId,String dealCategory,String dealSubCategory,String dealTitle,String dealDesc,
                           String dealStartDate,String dealEndDate,String allDays,String dealUsage,String location,String discountType,String discountValue,String dealAmount){
            this.shopId = shopId;
            this.dealCategory = dealCategory;
            this.dealSubCategory = dealSubCategory;
            this.dealTitle = dealTitle;
            this.dealDesc = dealDesc;
            this.dealStartDate = dealStartDate;
            this.dealEndDate = dealEndDate;
            this.allDays = allDays;
            this.dealUsage = dealUsage;
            this.location = location;
            this.discountType = discountType;
            this.discountValue = discountValue;
            this.dealAmount = dealAmount;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                Toast.makeText(getActivity(),"Deal successfully added",Toast.LENGTH_LONG).show();
                clearField();
                if(messageHandler != null) {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putBoolean("deal_added", true);
                    msg.setData(b);
                    messageHandler.sendMessage(msg);
                }
            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "adddeal"));
            inputArray.add(new BasicNameValuePair("merchant_id", ModuleClass.MERCHANT_ID));
            inputArray.add(new BasicNameValuePair("shop_id", shopId));
            inputArray.add(new BasicNameValuePair("deal_category", dealCategory));
            inputArray.add(new BasicNameValuePair("deal_subcategory", dealSubCategory));
            inputArray.add(new BasicNameValuePair("deal_title", dealTitle));
            inputArray.add(new BasicNameValuePair("deal_description", dealDesc));
            inputArray.add(new BasicNameValuePair("deal_startdate", dealStartDate));
            inputArray.add(new BasicNameValuePair("deal_enddate", dealEndDate));
            inputArray.add(new BasicNameValuePair("all_days", allDays));
            inputArray.add(new BasicNameValuePair("deal_usage", dealUsage));
            inputArray.add(new BasicNameValuePair("location", location));
            inputArray.add(new BasicNameValuePair("discount_type", discountType));
            inputArray.add(new BasicNameValuePair("discount_value", discountValue));
            inputArray.add(new BasicNameValuePair("deal_amount",dealAmount));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Add Deal ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    Log.v("Notification","Result Data : "+dataArray.getString(0));
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

    class GetShopTask extends AsyncTask<Void,Void,Void> {

        boolean success;
        String responseError;
        ProgressDialog dialog;
        public GetShopTask(){
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                if(shopList.size() > 0){
                    adapter = new ShopListAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,shopList);
                    listShop.setAdapter(adapter);
                    ModuleClass.setListViewHeightBasedOnItems(listShop);
                }else{
                    tvShopEmpty.setVisibility(View.VISIBLE);
                }

            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "my_shop"));
            inputArray.add(new BasicNameValuePair("merchantid",ModuleClass.MERCHANT_ID));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"merchant.php", "GET", inputArray);
            Log.d("Get Shop ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        ShopModel data = new ShopModel(
                                object.getString("shop_id"),
                                object.getString("merchant_id"),
                                object.getString("shop_name"),
                                object.getString("shop_addres"),
                                object.getString("shop_latitude"),
                                object.getString("shop_longitude"),
                                object.getString("shop_email"),
                                object.getString("shop_mobile"),
                                object.getString("qr_image"),
                                object.getString("shop_add"),
                                object.getString("shop_city"),
                                object.getString("shop_state"),
                                object.getString("shop_zip"),
                                object.getString("shop_country"),
                                object.getString("is_active"),
                                object.getString("added_date")
                        );
                        shopList.add(data);
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
