package com.example.mobileproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMain extends AppCompatActivity {





    RequestQueue rQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);
        setup();

        //RequestQue (Very important)
        rQueue = Volley.newRequestQueue(this);

        rQueue.add(Helper.weather(this));


//Buttons
        Button delete=findViewById(R.id.bttn_delete);

        Button update=findViewById(R.id.bttn_update);

        Button insert=findViewById(R.id.bttn_insert);

        Button select=findViewById(R.id.bttn_select);

        //Inputs


        EditText input_uid=findViewById(R.id.edit_Uid);

        EditText emailAddress=findViewById(R.id.edit_email);

        EditText firstname = findViewById(R.id.edit_fname);

        EditText lastname=findViewById(R.id.edit_lname);

        EditText phoneNo=findViewById(R.id.edit_Phone);





        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=firstname.getText()+"";

                String lname=lastname.getText()+"";

                String phone=phoneNo.getText()+"";

                String email=emailAddress.getText()+"";

                String uid=input_uid.getText()+"";

                //Hashmap
                HashMap<String,Object> map=new HashMap<>();

                if (uid.isEmpty()){
                    Toast.makeText(FirebaseMain.this,"Please fill in the UID field for this operation.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fname.isEmpty()){
                    map.put("firstName",fname);
                }
                if (!lname.isEmpty()){
                    map.put("lastName",lname);
                }
                if (!phone.isEmpty()){
                    map.put("phoneNumber",phone);
                }
                if (!email.isEmpty()){
                    map.put("emailAddress",email);
                }
                if (!uid.isEmpty()){
                    map.put("userId",Integer.valueOf(uid));
                }
                update(Integer.valueOf(uid),map);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid=input_uid.getText()+"";
                if (uid.isEmpty()){
                    Toast.makeText(FirebaseMain.this,"Please fill in the uid field for this operation.",Toast.LENGTH_SHORT).show();
                    return;
                }
                delete(Integer.valueOf(uid));
            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirebaseMain.this,FirebaseList.class));
            }
        });



        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f_name=firstname.getText()+"";
                String l_name=lastname.getText()+"";
                String phone=phoneNo.getText()+"";
                String email=emailAddress.getText()+"";
                String uid=input_uid.getText()+"";

                if (f_name.isEmpty() || l_name.isEmpty() || phone.isEmpty() || email.isEmpty() || uid.isEmpty()){
                    Toast.makeText(FirebaseMain.this,"Please fill in all fields for this operation.",Toast.LENGTH_SHORT).show();
                    return;
                }
                insertUser(email,f_name,l_name,phone,Integer.valueOf(uid));
            }
        });




    }

    FirebaseDatabase database;
    
    
    DatabaseReference ref;


    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }

    private void delete(int uid){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FirebaseMain.this,"Deleted the record successfully.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FirebaseMain.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FirebaseMain.this,"No user with this UID was found",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





    private void insertUser(String email,String f_name,String l_name,String phoneNum,int user_ID){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("user_ID").getValue(Integer.class)==user_ID){

                        Toast.makeText(FirebaseMain.this,"Record with matching UID  "+user_ID+" was located.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                int initialCount=(int)snapshot.getChildrenCount();
                while(snapshot.hasChild(initialCount+"")){
                    initialCount++;
                }
                DatabaseReference insertRef=ref.child(initialCount+"");


                insertRef.child("emailAddress").setValue(email);

                insertRef.child("firstName").setValue(f_name);

                insertRef.child("lastName").setValue(l_name);

                insertRef.child("phoneNumber").setValue(phoneNum);

                insertRef.child("user_ID").setValue(user_ID);

                Toast.makeText(FirebaseMain.this,"Successfully Inserted.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateUserAllDetails(String email,String fName,String lName,String phone,int userId){
        Map<String,Object> stringObjectMap=new HashMap<>();
        stringObjectMap.put("emailAddress",email);

        stringObjectMap.put("firstName",fName);

        stringObjectMap.put("lastName",lName);

        stringObjectMap.put("phoneNumber",phone);

        stringObjectMap.put("userId",userId);

        update(userId,stringObjectMap);
    }






    private void update(int uid,Map<String,Object> keyValMap){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).updateChildren(keyValMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FirebaseMain.this,"Record Updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FirebaseMain.this,"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FirebaseMain.this,"No user with this UID was found",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}

@IgnoreExtraProperties
class User{


    int userId;
    String emailAddress;
    String phoneNumber;
    String firstName;
    String lastName;

    public User(){

    }



    public void set_userid(int userId) {
        this.userId = userId;
    }

    public int get_userId() {
        return userId;
    }


    public void set_FirstName(String firstName) {
        this.firstName = firstName;
    }

    public String get_FirstName() {
        return firstName;
    }



    public String get_PhoneNumber() {
        return phoneNumber;
    }

    public void set_PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public String get_LastName() {
        return lastName;
    }

    public void set_LastName(String lastName) {
        this.lastName = lastName;
    }



    public String get_EmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}