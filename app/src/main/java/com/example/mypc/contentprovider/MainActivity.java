package com.example.mypc.contentprovider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText e1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Uri uri = data.getData();    //uri is path from where we get data

        Cursor cursor = getContentResolver().query(uri,null,null,null,null);

        //super.onActivityResult(requestCode, resultCode, data);

        while(cursor.moveToNext())
        {
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0)
            {
                Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                        new String[]{cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))},
                        null);   //5 arguments

                while(c.moveToNext())
                {
                    String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    e1.setText(""+phone);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText) findViewById(R.id.editText);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},0);

            return;
        }

        e1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(i,0);
            }
        });
    }
}
