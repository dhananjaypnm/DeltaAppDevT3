package com.dhananjay.deltaappdevt3;

import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleContactFragment extends Fragment implements View.OnClickListener{

    String contactName;
    String contactEmail;
    String contactNumber;
    String contactID;
    Uri contactUri;
    Uri photoUri;
    ImageView image;
    TextView name;
    TextView number;
    TextView email;
    EditText editName;
    EditText editNumber;
    EditText editEmail;
    Button confirm;
    Communicator2 comm2;
    ImageButton back;
    ImageButton edit;
    ImageButton delete;

    public SingleContactFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void lendData(Bundle bundle){
        contactName=bundle.getString("contactName");
        contactNumber=bundle.getString("contactNumber");
        contactEmail=bundle.getString("contactEmail");
        contactID=bundle.getString("contactID");
        contactUri=Uri.parse(bundle.getString("contactUri"));
        photoUri=Uri.parse(bundle.getString("photoUri"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_single_contact, container, false);
        editName= (EditText) v.findViewById(R.id.editContactName);
        editNumber= (EditText) v.findViewById(R.id.editContactNumber);
        editEmail= (EditText) v.findViewById(R.id.editContactEmail);
        confirm= (Button) v.findViewById(R.id.confirm);
        back= (ImageButton) v.findViewById(R.id.back);
        edit= (ImageButton) v.findViewById(R.id.edit);
        delete= (ImageButton) v.findViewById(R.id.delete);
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        confirm.setOnClickListener(this);
        image= (ImageView) v.findViewById(R.id.contactImage);
        name= (TextView) v.findViewById(R.id.contactName);
        number= (TextView) v.findViewById(R.id.contactNumber);
        email= (TextView) v.findViewById(R.id.contactEmail);
        name.setText(contactName);
        number.setText(contactNumber);
        email.setText(contactEmail);
        image.setImageURI(photoUri);
        if (image.getDrawable()==null){
            image.setImageResource(R.drawable.avatar);
            Toast.makeText(getActivity(),"Photo not found",Toast.LENGTH_SHORT).show();
        }
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                comm2= (Communicator2) getActivity();
                comm2.replace();
                break;
            case R.id.edit:
                name.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                editName.setVisibility(View.VISIBLE);
                editNumber.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.VISIBLE);
                editName.setText(name.getText());
                editNumber.setText(number.getText());
                editEmail.setText(email.getText());
                confirm.setVisibility(View.VISIBLE);
                break;
            case R.id.delete:
                int num=getActivity().getContentResolver().delete(contactUri, ContactsContract.Contacts._ID,new String[]{contactID});
                Toast.makeText(getActivity(),"Contact Deleted",Toast.LENGTH_SHORT).show();
                comm2.replace();
                break;
            case R.id.confirm:
                confirm.setVisibility(View.GONE);
                editNumber.setVisibility(View.GONE);
                editName.setVisibility(View.GONE);
                editEmail.setVisibility(View.GONE);
                name.setText(editName.getText());
                number.setText(editNumber.getText());
                email.setText(editEmail.getText());
                number.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                ContentValues cvnumber = new ContentValues();
                cvnumber.put(ContactsContract.Contacts._ID, contactID);
                cvnumber.put(ContactsContract.CommonDataKinds.Phone.NUMBER, editNumber.getText().toString());
                int number = getActivity().getContentResolver().update(ContactsContract.Data.CONTENT_URI, cvnumber,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, new String[]{contactID});
                ContentValues cvemail = new ContentValues();
                cvemail.put(ContactsContract.Contacts._ID, contactID);
                cvemail.put(ContactsContract.CommonDataKinds.Email.ADDRESS, editEmail.getText().toString());
                int numb = getActivity().getContentResolver().update(ContactsContract.Data.CONTENT_URI, cvemail,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Email.TYPE + " = " + ContactsContract.CommonDataKinds.Email.TYPE_MOBILE, new String[]{contactID});
        }
    }

    interface Communicator2{
        void replace();
    }

}
