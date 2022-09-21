package com.example.dailyshoppinglist.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyshoppinglist.Adapter.ListAdapter;
import com.example.dailyshoppinglist.Model.Data;
import com.example.dailyshoppinglist.R;
import com.example.dailyshoppinglist.Interface.RecyclerViewInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements RecyclerViewInterface {

    private TextView tvTotalAmount;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    RecyclerView recyclerView;
    Context context = this;
    public ArrayList<Data> list = new ArrayList<>();
    ImageView ivLogout;

    private static String type = "type";
    private static String note = "note";
    private static String postKey;
    private static int amount = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvTotalAmount = findViewById(R.id.tv_total_amount);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);
        recyclerView = findViewById(R.id.rv_list);
        FloatingActionButton fb = findViewById(R.id.fb);
        ivLogout = findViewById(R.id.iv_logout);

        ListAdapter adapter = new ListAdapter(list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalAmount = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String id = dataSnapshot.child("id").getValue().toString();
                    String note = dataSnapshot.child("note").getValue().toString();
                    String type = dataSnapshot.child("type").getValue().toString();
                    String date = dataSnapshot.child("date").getValue().toString();
                    int amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                    totalAmount += amount;
                    Log.d("mainmaina", String.valueOf(totalAmount));
                    list.add(new Data(amount, type, note, date, id));
                    tvTotalAmount.setText("₹" + String.valueOf(totalAmount));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("mainmain", String.valueOf(list.size()));

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
    }


    public void customDialog() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
        View myview = LayoutInflater.from(HomeActivity.this).inflate(R.layout.add_data, null);
        AlertDialog dialog = myDialog.create();
        dialog.setView(myview);

        EditText type = myview.findViewById(R.id.et_type);
        EditText note = myview.findViewById(R.id.et_note);
        EditText amount = myview.findViewById(R.id.et_amount);
        Button bt_save = myview.findViewById(R.id.bt_save_data);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                String mType = type.getText().toString().trim();
                String mNote = note.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();


                if(TextUtils.isEmpty(mType)) {
                    type.setError("field empty");
                    return;
                }
                if(TextUtils.isEmpty(mAmount)) {
                    amount.setError("field empty");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(Integer.parseInt(mAmount), mType, mNote, date, id);

                mDatabase.child(id).setValue(data);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.update_data, null);
        AlertDialog dialog = myDialog.create();
        dialog.setView(view);

        final EditText uptType = view.findViewById(R.id.et_upt_type);
        final EditText uptAmount = view.findViewById(R.id.et_upt_amount);
        final EditText uptNote = view.findViewById(R.id.et_upt_note);

        uptType.setText(type);
        Log.d("mainmain", "data adding");
        uptType.setSelection(type.length());
        uptAmount.setText(String.valueOf(amount));
        uptAmount.setSelection(String.valueOf(amount).length());
        uptNote.setText(note);
        uptNote.setSelection(note.length());

        Button btUpdate = view.findViewById(R.id.bt_update_data);
        Button btDelete = view.findViewById(R.id.bt_delete_data);

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                mDatabase.child(postKey).removeValue();

                dialog.dismiss();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                type = uptType.getText().toString().trim();
                note = uptNote.getText().toString().trim();
                String mAmount = uptAmount.getText().toString().trim();
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(Integer.parseInt(mAmount), type, note, date, postKey);
                mDatabase.child(postKey).setValue(data);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onItemClick(int position) {
        Data upData = list.get(position);
        amount = upData.getAmount();
        type = upData.getType();
        note = upData.getNote();
        postKey = upData.getId();
        Log.d("mainmain", "rvListner run");
        updateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}