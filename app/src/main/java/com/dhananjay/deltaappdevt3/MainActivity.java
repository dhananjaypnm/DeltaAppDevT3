package com.dhananjay.deltaappdevt3;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ContactsFragment.Communicator,SingleContactFragment.Communicator2{

    ContactsFragment contactsFragment;
    SingleContactFragment singleContactFragment;
    FragmentManager manager;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsFragment=new ContactsFragment();
        singleContactFragment=new SingleContactFragment();
        manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.add(R.id.parent,contactsFragment,"MyFragment");
        transaction.commit();
    }

    @Override
    public void sendData(Bundle bundle) {
        transaction=manager.beginTransaction();
        transaction.addToBackStack("singleContact");
        transaction.replace(R.id.parent,singleContactFragment);
        transaction.commit();
        singleContactFragment.lendData(bundle);
    }

    @Override
    public void replace() {
        manager.popBackStack();
        contactsFragment=new ContactsFragment();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.parent,contactsFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
