package com.example.mobileproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseList extends AppCompatActivity {


    EditText select_ID;

    ListView lView;

    ArrayList<User> users=new ArrayList<>();


    ArrayList<User> fetchedUsers=new ArrayList<>();

    int uidToDisplay=-1; //-1 means all
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_list);

        setup();


        select_ID =findViewById(R.id.inp_select_uid);


//Functions

        lView =findViewById(R.id.lv_firebase);
        lView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return users.size();
            }


            @Override
            public Object getItem(int position) {
                return users.get(position);
            }


            @Override
            public long getItemId(int position) {
                return 0;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User u=users.get(position);
                System.out.println(u);
                TableLayout table=(TableLayout) LayoutInflater.from(FirebaseList.this).inflate(R.layout.list_item,parent,false);

                TextView ID_out=table.findViewById(R.id.out_fb_uid);

                TextView Name_out=table.findViewById(R.id.out_fb_name);

                TextView Phone_out=table.findViewById(R.id.out_fb_phone);

                TextView Email_out=table.findViewById(R.id.out_fb_email);

                ID_out.setText(""+u.userId);

                Name_out.setText(u.firstName+" "+u.lastName);

                Phone_out.setText(u.phoneNumber);

                Email_out.setText(u.emailAddress);

                return table;
            }


        });






        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get all users
                fetchedUsers=snapshot.getValue(new GenericTypeIndicator<ArrayList<User>>() {});
                //remove null objects
                ArrayList<User> newUsersList=new ArrayList<>();
                for (User u:fetchedUsers){
                    if (u!=null){
                        newUsersList.add(u);
                    }
                }
                fetchedUsers=newUsersList;
                update();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FETCH",error.getMessage());
                Toast.makeText(FirebaseList.this,"Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



        TextWatcher tw =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    uidToDisplay=-1;
                }else{
                    uidToDisplay=Integer.valueOf(s.toString());
                }
                update();
            }
        };
        select_ID.addTextChangedListener(tw);


    }




    FirebaseDatabase database;
    DatabaseReference ref;

    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }

    private void update(){
        if (uidToDisplay!=-1){
            for (User u:fetchedUsers) {
                if (uidToDisplay==u.get_userId()){
                    users.clear();

                    users.add(u);
                }
            }
        }


        else{
            users.clear();
            users.addAll(fetchedUsers);
        }
        ((BaseAdapter) lView.getAdapter()).notifyDataSetChanged();
    }


}