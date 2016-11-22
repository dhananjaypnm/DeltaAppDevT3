package com.dhananjay.deltaappdevt3;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener{

    Communicator comm;

    ListView listView;
    SimpleCursorAdapter cursorAdapter;
    String[] PROJECTION={
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts._ID};
    String[] selectionArgs={"na"};
    CursorLoader loader;
    Bundle bundle;
    String contactName;
    String contactNumber;
    String contactEmail;
    public ContactsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView=(ListView)getActivity().findViewById(R.id.listView);
        String[] name={ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        int[] id={R.id.textView};
        cursorAdapter=new SimpleCursorAdapter(getActivity(),R.layout.single_contact_item_layout,null, name,id,0);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0,null,this);
        comm= (Communicator) getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor= (Cursor) cursorAdapter.getItem(position);
        contactName=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        String contactID=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,new Long(contactID));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        ContentResolver cr = getActivity().getContentResolver();

        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ", new String[]{contactID},null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        else {

        }

        Cursor cursorEmail = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactID}, null);
        if (cursorEmail.moveToFirst()) {
            contactEmail = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        }
        else {
            contactEmail="Not Found";
        }

            cursorPhone.close();
            bundle = new Bundle();
            bundle.putString("contactName", contactName);
            bundle.putString("contactNumber", contactNumber);
            bundle.putString("contactEmail",contactEmail);
            bundle.putString("contactID", contactID);
            bundle.putString("contactUri", contactUri.toString());
            bundle.putString("photoUri", photoUri.toString());
            comm.sendData(bundle);

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        selectionArgs[0]="%"+selectionArgs[0]+"%";
        loader= new CursorLoader(getActivity(),ContactsContract.Contacts.CONTENT_URI,PROJECTION, ContactsContract.Contacts.HAS_PHONE_NUMBER+" <>0",null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        return loader;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    interface Communicator{
         void sendData(Bundle bundle);
    }
}
