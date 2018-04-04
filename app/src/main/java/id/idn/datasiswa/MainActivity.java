package id.idn.datasiswa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import id.idn.datasiswa.ApiRetrofit.ApiService;
import id.idn.datasiswa.ApiRetrofit.InstanceRetrofit;
import id.idn.datasiswa.ResponseServer.DataItem;
import id.idn.datasiswa.ResponseServer.ResponseCreateData;
import id.idn.datasiswa.ResponseServer.ResponseReadData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // TODO Create Recyclerview variable class
    RecyclerView view;
    Dialog popUp;
    EditText edtName, edtAddress, edtHomeTown, edtSex, edtClass;
    String strName, strAddress, strHomeTown, strSex, strClass;
    Button btnInsert, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO Inlitialize Widget to Variable
        view = findViewById(R.id.recyclerview);
        view.setLayoutManager(new LinearLayoutManager(this));
        getData();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp = new Dialog(MainActivity.this);
                popUp.setContentView(R.layout.inputdata);
                popUp.show();

                edtName = popUp.findViewById(R.id.edtName);
                edtAddress = popUp.findViewById(R.id.edtAddress);
                edtClass = popUp.findViewById(R.id.edtClass);
                edtSex = popUp.findViewById(R.id.edtSex);
                edtHomeTown = popUp.findViewById(R.id.edtHomeTown);

                btnInsert = popUp.findViewById(R.id.btnInsert);
                btnInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strName = edtName.getText().toString();
                        strAddress = edtAddress.getText().toString();
                        strClass = edtClass.getText().toString();
                        strSex = edtSex.getText().toString();
                        strHomeTown = edtHomeTown.getText().toString();

                        Log.d("TAG", ""+strName);

                        if (TextUtils.isEmpty(strName) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strClass)
                                && TextUtils.isEmpty(strHomeTown) && TextUtils.isEmpty(strSex)){
                            edtHomeTown.setError("Tidak Boleh Kosong!");
                            edtName.setError("Tidak Boleh Kosong!");
                            edtClass.setError("Tidak Boleh Kosong!");
                            edtSex.setError("Tidak Boleh Kosong!");
                            edtAddress.setError("Tidak Boleh Kosong!");

                            edtHomeTown.requestFocus();
                            edtName.requestFocus();
                            edtClass.requestFocus();
                            edtSex.requestFocus();
                            edtAddress.requestFocus();
//                            edtName.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.));
                        } else {
                            insertData();
                            popUp.dismiss();
                        }

                    }
                });

                btnDelete = popUp.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       deletedata();
                    }
                });
            }
        });
    }

    private void deletedata() {

    }

    private void insertData() {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Proses Data", "Mohon Ditunggu");
        ApiService apiService = InstanceRetrofit.getInstance();
        Call<ResponseCreateData> createDataCall = apiService.response_create_data(strName, strAddress, strSex, strHomeTown, strClass);
        createDataCall.enqueue(new Callback<ResponseCreateData>() {
            @Override
            public void onResponse(Call<ResponseCreateData> call, Response<ResponseCreateData> response) {
                Log.d("TAG", "" + response.body().isSukses());
                if (response.body().isSukses()) {
                    Toast.makeText(MainActivity.this, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Ini"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        ApiService api = InstanceRetrofit.getInstance();
        Call<ResponseReadData> call = api.response_read_data();
        call.enqueue(new Callback<ResponseReadData>() {
            @Override
            public void onResponse(Call<ResponseReadData> call, Response<ResponseReadData> response) {
                Boolean status = response.body().isSuccess();
                if(status){
                    List<DataItem> dataItems = response.body().getData();
                    CustomAdapter adapter = new CustomAdapter(MainActivity.this, dataItems);
                    view.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseReadData> call, Throwable t) {

            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast toast = new Toast(MainActivity.this);
            toast.setText("Ini Move");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.RIGHT){
                Toast.makeText(MainActivity.this, "Kekanan", Toast.LENGTH_SHORT).show();
            } else if (direction == ItemTouchHelper.LEFT){
                Toast.makeText(MainActivity.this, "KeKIRI", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
