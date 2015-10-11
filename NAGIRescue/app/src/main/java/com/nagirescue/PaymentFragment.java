package com.nagirescue;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;

import java.math.BigDecimal;

/**
 * Created by cipher1729 on 10/10/2015.
 */
public class PaymentFragment extends Fragment {
    View rootView;
    EditText AmountDonating,firstName,lastName,email,phoneNo;
    Button paypal_button;
    String amount,fname,lname,emailid,phone;
    final int PAYPAL_RESPONSE=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId = R.layout.paymentlayout;
        rootView = inflater.inflate(layoutId, container, false);
        setOnClickListeners();
        initLibrary();

        return rootView;
    }

    private void setOnClickListeners() {
        //lost for found button clicks
        ((Button) rootView.findViewById(R.id.payPalBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientEmail="abc@xyz.com";
                paypal_button = (Button) rootView.findViewById(R.id.payPalBtn);
                AmountDonating = (EditText) rootView.findViewById(R.id.amountTextField);
                firstName = (EditText) rootView.findViewById(R.id.firstNameTextField);
                lastName = (EditText) rootView.findViewById(R.id.lastNameTextField);
                email =  (EditText) rootView.findViewById(R.id.emailTextField);
                phoneNo =  (EditText) rootView.findViewById(R.id.phoneTextField);
                PayPalButtonClick(recipientEmail);
            }
        });
    }

    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        if (pp == null) {
            pp = PayPal.initWithAppID(getActivity(), Util.sand_box_id, PayPal.ENV_SANDBOX);
            pp.setLanguage("en_US");
        }
    }

    public void PayPalButtonClick(String primary_id) {
        PayPalPayment payment = new PayPalPayment();
        // Set the currency type
        payment.setCurrencyType("USD");
        // Set the recipient for the payment (can be a phone number)
        payment.setRecipient(primary_id);
        amount = AmountDonating.getText().toString();
        fname = firstName.getText().toString();
        lname = lastName.getText().toString();
        emailid = email.getText().toString();
        phone = phoneNo.getText().toString();

        if(amount.equals("") || fname.equals("") || lname.equals("")|| emailid.equals("")|| phone.equals("")) {
            Toast.makeText(getActivity(), "Required Fields", Toast.LENGTH_LONG).show();
            Intent intent = getActivity().getIntent();
            getActivity().setIntent(intent);
        }
        else {
            // Set the payment amount, excluding tax and shipping costs
            payment.setSubtotal(new BigDecimal(amount));

            Intent paypalIntent = PayPal.getInstance().checkout(payment, getActivity());
            this.startActivityForResult(paypalIntent, PAYPAL_RESPONSE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_RESPONSE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //The payment succeeded
                    String payKey =
                            data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                    Toast.makeText(getActivity(), "Payment done succesfully ", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();
                    break;
                case PayPalActivity.RESULT_FAILURE:
                    Toast.makeText(getActivity(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
