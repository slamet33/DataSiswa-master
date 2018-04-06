package id.idn.datasiswa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.idn.datasiswa.ApiRetrofit.ApiService;
import id.idn.datasiswa.ApiRetrofit.InstanceRetrofit;
import id.idn.datasiswa.ResponseServer.DataItem;
import id.idn.datasiswa.ResponseServer.ResponseDeleteData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 3/27/2018.
 */

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    List<DataItem> data = new ArrayList<>();
    Dialog popUpED, popUpUpdate;
    Button btnDelete, btnUpdate, btnLatestUpdate;
    EditText edtName, edtAddress, edtClass, edtHomeTown, edtSex;
    String strName, strAddress, strClass, strHomeTown, strSex;
    String id_data;
    public static final String WebUrl = "http:// ip komputer masing-masing / nama folder di htdocs";


    public CustomAdapter(Context context, List<DataItem> dataItems) {
        this.context = context;
        this.data = dataItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.d("RAM", "" + data.get(position).getAddress());
        id_data = data.get(position).getId();
        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getAddress());
        holder.gender.setText(data.get(position).getSex());
        holder.hometown.setText(data.get(position).getHometown());
        holder.classs.setText(data.get(position).getJsonMemberClass());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Nani ?", Toast.LENGTH_SHORT).show();
                popUpED = new Dialog(context);
                popUpED.setContentView(R.layout.popuped);
                popUpED.show();

                btnDelete = popUpED.findViewById(R.id.btnDeleted);
                btnUpdate = popUpED.findViewById(R.id.btnUpdated);

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog dialog = ProgressDialog.show(context, "Proses Delete", "Mohon Ditunggu");
                        ApiService apiService = InstanceRetrofit.getInstance();
                        Call<ResponseDeleteData> call = apiService.response_delete_data(id_data);
                        call.enqueue(new Callback<ResponseDeleteData>() {
                            @Override
                            public void onResponse(Call<ResponseDeleteData> call, Response<ResponseDeleteData> response) {
                                boolean result = response.body().isResult();
                                if (result) {
                                    Toast.makeText(context, "" + id_data, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    popUpED.dismiss();
                                } else {
                                    Toast.makeText(context, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseDeleteData> call, Throwable t) {
                                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpED.dismiss();
                        context.startActivity(new Intent(context, UpdateActivity.class)
                                .putExtra("name", data.get(position).getName())
                                .putExtra("address", data.get(position).getAddress())
                                .putExtra("sex", data.get(position).getSex())
                                .putExtra("id", data.get(position).getId())
                                .putExtra("class", data.get(position).getJsonMemberClass())
                                .putExtra("hometown", data.get(position).getHometown())
                        );
//                        popUpUpdate= new Dialog(context);
//                        popUpUpdate.setContentView(R.layout.inputdatas);
//                        popUpUpdate.show();


//                        edtName = popUpUpdate.findViewById(R.id.edtNamed);
//                        edtSex = popUpUpdate.findViewById(R.id.edtSexd);
//                        edtAddress = popUpUpdate.findViewById(R.id.edtAddresssd);
//                        edtClass = popUpUpdate.findViewById(R.id.edtClassd);
//                        edtHomeTown = popUpUpdate.findViewById(R.id.edtHomeTownd);
//                        btnLatestUpdate = popUpUpdate.findViewById(R.id.btnInsertd);
//
//                        edtName.setHint(data.get(position).getName());
//                        edtHomeTown.setHint(data.get(position).getHometown());
//                        edtClass.setHint(data.get(position).getJsonMemberClass());
//                        edtSex.setHint(data.get(position).getSex());
//                        edtAddress.setHint(data.get(position).getAddress());
//                        btnLatestUpdate.setText("Update");
//
//                        strName = edtName.getText().toString();
//                        strAddress = edtAddress.getText().toString();
//                        strClass = edtHomeTown.getText().toString();
//                        strSex = edtSex.getText().toString();
//                        strHomeTown = edtHomeTown.getText().toString();
//
//                        btnLatestUpdate.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(context, ""+strName, Toast.LENGTH_SHORT).show();
//                                final ProgressDialog dialog = ProgressDialog.show(context, "Proses Delete", "Mohon Ditunggu");
////                                try {
////                                    new MultipartUploadRequest(context, InstanceRetrofit.WebUrl)
////                                            .addParameter("vsname", strName)
////                                            .addParameter("vsaddress", strAddress)
////                                            .addParameter("vssex", strSex)
////                                            .addParameter("vshometown", strHomeTown)
////                                            .addParameter("vsclass", strClass)
////                                            .addParameter("vsid", id_data)
////                                            .setNotificationConfig(new UploadNotificationConfig())
////                                            .setMaxRetries(1)
////                                            .startUpload();
////                                } catch (MalformedURLException e) {
////                                    e.printStackTrace();
////                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
////                                }
//
//
//                                ApiService apiService = InstanceRetrofit.getInstance();
//                                Call<ResponseUpdateData> call = apiService.response_update_Data(
//                                        strName,strAddress,strSex,strHomeTown, strClass, id_data
//                                );
//                                call.enqueue(new Callback<ResponseUpdateData>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseUpdateData> call, Response<ResponseUpdateData> response) {
//                                        dialog.dismiss();
//                                        popUpUpdate.dismiss();
//                                        popUpED.dismiss();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseUpdateData> call, Throwable t) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                            }
//                        });
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView classs, name, gender, hometown, address;

        public MyViewHolder(View itemView) {
            super(itemView);

            classs = itemView.findViewById(R.id.classs);
            name = itemView.findViewById(R.id.nama);
            address = itemView.findViewById(R.id.address);
            hometown = itemView.findViewById(R.id.hometown);
            gender = itemView.findViewById(R.id.sex);
        }
    }
}
