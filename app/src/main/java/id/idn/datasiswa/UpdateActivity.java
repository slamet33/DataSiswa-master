package id.idn.datasiswa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.idn.datasiswa.ApiRetrofit.ApiService;
import id.idn.datasiswa.ApiRetrofit.InstanceRetrofit;
import id.idn.datasiswa.ResponseServer.ResponseUpdateData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    EditText edtName, edtSex, edtAddress, edtClass, edtHomeTown;
    Button btnLatestUpdate;
    String strName, strAddress, strClass, strSex, strHomeTown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edtName = findViewById(R.id.edtNamed);
        edtSex = findViewById(R.id.edtSexd);
        edtAddress = findViewById(R.id.edtAddresssd);
        edtClass = findViewById(R.id.edtClassd);
        edtHomeTown = findViewById(R.id.edtHomeTownd);
        btnLatestUpdate = findViewById(R.id.btnInsertd);

        edtName.setText(getIntent().getStringExtra("name"));
        edtHomeTown.setText(getIntent().getStringExtra("hometown"));
        edtClass.setText(getIntent().getStringExtra("class"));
        edtSex.setText(getIntent().getStringExtra("sex"));
        edtAddress.setText(getIntent().getStringExtra("address"));
        btnLatestUpdate.setText("Update");

        btnLatestUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = getIntent().getStringExtra("id");

                strName = edtName.getText().toString();
                strAddress = edtAddress.getText().toString();
                strClass = edtClass.getText().toString();
                strSex = edtSex.getText().toString();
                strHomeTown = edtHomeTown.getText().toString();

                Toast.makeText(UpdateActivity.this, "" + strName, Toast.LENGTH_SHORT).show();
                final ProgressDialog dialog = ProgressDialog.show(UpdateActivity.this, "Proses Delete", "Mohon Ditunggu");
                ApiService apiService = InstanceRetrofit.getInstance();
                Call<ResponseUpdateData> call = apiService.response_update_Data(
                        strName, strAddress, strSex, strHomeTown, strClass, id
                );
                call.enqueue(new Callback<ResponseUpdateData>() {
                    @Override
                    public void onResponse(Call<ResponseUpdateData> call, Response<ResponseUpdateData> response) {
                        Log.d("TAG", "" + response.body().isSukses());
                        if (response.body().isSukses()) {
                            Toast.makeText(UpdateActivity.this, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(UpdateActivity.this, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(UpdateActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseUpdateData> call, Throwable t) {
                        Toast.makeText(UpdateActivity.this, "Ini"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
