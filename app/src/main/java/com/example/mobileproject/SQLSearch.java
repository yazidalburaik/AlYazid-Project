package com.example.mobileproject;



import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLSearch extends AppCompatActivity {

    ArrayList<User> UserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_search);

        DBHelper dbHelper=new DBHelper(this);

        ListView viewList=findViewById(R.id.lv_sql);

        EditText inp_uid=findViewById(R.id.inp_sql_select_uid);

        Button select=findViewById(R.id.btn_sql_select);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=inp_uid.getText()+"";
                Cursor c;
                if (uid.isEmpty()){
                    c=dbHelper.selectAll();
                }
                else
                    {
                    c=dbHelper.selectByUID(Integer.valueOf(uid));
                }
                if (c==null){
                    Toast.makeText(SQLSearch.this,"No records were found.",Toast.LENGTH_SHORT).show();
                    return;
                }
                UserList.clear();
                do {
                    User u=new User();
                    u.firstName=c.getString(0);

                    u.lastName=c.getString(1);

                    u.phoneNumber=c.getString(2);

                    u.emailAddress=c.getString(3);

                    u.userId=c.getInt(4);

                    UserList.add(u);
                }while (c.moveToNext());

                ((BaseAdapter)viewList.getAdapter()).notifyDataSetChanged();
            }
        });

        UserList =new ArrayList<>();

        viewList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return UserList.size();
            }

            @Override
            public Object getItem(int position) {
                return UserList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User u= UserList.get(position);

                TableLayout table=(TableLayout) LayoutInflater.from(SQLSearch.this).inflate(R.layout.list_item,parent,false);

                TextView ID_Output=table.findViewById(R.id.out_fb_uid);

                TextView Name_Output=table.findViewById(R.id.out_fb_name);

                TextView Phone_Output=table.findViewById(R.id.out_fb_phone);

                TextView Email_Output=table.findViewById(R.id.out_fb_email);

                //Name and ID
                Name_Output.setText(u.firstName+" "+u.lastName);

                ID_Output.setText(""+u.userId);

//Phone and EMail
                Phone_Output.setText(u.phoneNumber);

                Email_Output.setText(u.emailAddress);

                return table;
            }
        });
        viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u= UserList.get(position);

                Toast.makeText(SQLSearch.this,"Name: " + u.get_FirstName()+""+ u.get_LastName() + ", " + "ID:" + u.get_userId(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}