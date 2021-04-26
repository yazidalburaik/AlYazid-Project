package com.example.mobileproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SQL extends AppCompatActivity {






    FirebaseDatabase database;

    DatabaseReference ref;

    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);
        setup();

        DBHelper dbHelper=new DBHelper(this);


        Button bttn_Update_SQL=findViewById(R.id.bttn_updateSQL);

        Button bttn_Select_SQL=findViewById(R.id.bttn_SelectSQL);

        Button bttn_Delete_SQL=findViewById(R.id.bttn_deleteSQL);

        Button bttn_Insert_SQL=findViewById(R.id.bttn_insertSQL);

        Button bttn_FBInsert_SQL=findViewById(R.id.bttn_insertFBSQL);


        EditText edit_ID=findViewById(R.id.ID_Sql);

        EditText edit_FirstName=findViewById(R.id.firstName_Sql);

        EditText edit_LastName=findViewById(R.id.lastName_Sql);

        EditText edit_PhoneNum=findViewById(R.id.Phone_Sql);

        EditText edit_Email=findViewById(R.id.Email_Sql);




        bttn_FBInsert_SQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=edit_ID.getText()+"";
                if (uid.isEmpty() ){
                    Toast.makeText(SQL.this,"Please fill in UID field for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childData:snapshot.getChildren()){
                            if (childData.child("userId").getValue(Integer.class)==Integer.valueOf(uid)){
                                User u=snapshot.child(childData.getKey()).getValue(User.class);
                                int i=dbHelper.insert(u);

                                if (i!=-1){
                                    Toast.makeText(SQL.this,"Record inserted",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SQL.this,"Record was not inserted.",Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        }
                        Toast.makeText(SQL.this,"No user with this UID was found",Toast.LENGTH_SHORT).show();
                        return;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        bttn_Insert_SQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname=edit_FirstName.getText()+"";

                String lname=edit_LastName.getText()+"";

                String phone=edit_PhoneNum.getText()+"";

                String email=edit_Email.getText()+"";

                String uid=edit_ID.getText()+"";

                if (
                        fname.isEmpty() ||
                                lname.isEmpty() ||
                                phone.isEmpty() ||
                                email.isEmpty() ||
                                uid.isEmpty()){
                    Toast.makeText(SQL.this,"Please fill in all fields for this operation",Toast.LENGTH_SHORT).show();
                }

                int i=dbHelper.insert(fname,lname,phone,email,Integer.valueOf(uid));
                if (i!=-1){
                    Toast.makeText(SQL.this,"Record inserted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SQL.this,"Record was not inserted.",Toast.LENGTH_SHORT).show();
                }
            }
        });




        bttn_Select_SQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SQL.this, SQLSearch.class));
            }
        });

        bttn_Delete_SQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=edit_ID.getText()+"";
                if (uid.isEmpty() ){
                    Toast.makeText(SQL.this,"Please fill in uid field for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                int i=dbHelper.deleteByUID(Integer.valueOf(uid));
                if (i>0){
                    Toast.makeText(SQL.this,"Record deleted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SQL.this,"No record was deleted",Toast.LENGTH_SHORT).show();

                }
            }
        });


        bttn_Update_SQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=edit_FirstName.getText()+"";
                String lname=edit_LastName.getText()+"";
                String phone=edit_PhoneNum.getText()+"";
                String email=edit_Email.getText()+"";
                String uid=edit_ID.getText()+"";

                if (
                        fname.isEmpty() ||
                                lname.isEmpty() ||
                                phone.isEmpty() ||
                                email.isEmpty() ||
                                uid.isEmpty()){
                    Toast.makeText(SQL.this,"Please fill in all fields for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                int i=dbHelper.updateUser(fname,lname,phone,email,Integer.valueOf(uid));
                if (i>0){
                    Toast.makeText(SQL.this,"Record updated",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SQL.this,"No records were updated",Toast.LENGTH_SHORT).show();
                }
            }
        });






    }
}