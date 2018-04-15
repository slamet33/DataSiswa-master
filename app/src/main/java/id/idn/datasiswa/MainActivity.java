package id.idn.datasiswa;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.idn.datasiswa.ApiRetrofit.ApiService;
import id.idn.datasiswa.ApiRetrofit.InstanceRetrofit;
import id.idn.datasiswa.ResponseServer.DataItem;
import id.idn.datasiswa.ResponseServer.ResponseCreateData;
import id.idn.datasiswa.ResponseServer.ResponseReadData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // TODO Create Recyclerview variable class
    RecyclerView view;
    Dialog popUp;
    int requestCode = 1;
    Bitmap bitmap;
    Uri filePath;
    ImageView imagePreview;
    Boolean fc = false;
    EditText edtName, edtAddress, edtHomeTown, edtSex, edtClass, edtNameImage;
    String strId, strName, strAddress, strHomeTown, strSex, strClass;
    Button btnInsert, btnDelete, btnUploadImage;
    CustomAdapter adapter;
    List<DataItem> dataItem = new ArrayList<>();

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO Inlitialize Widget to Variable
        view = findViewById(R.id.recyclerview);

        requeststoragepermission();

        refreshLayout = findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshingLayout();
            }
        });

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
                edtNameImage = popUp.findViewById(R.id.edtNameImage);
                imagePreview = popUp.findViewById(R.id.imgupload);

                btnUploadImage = popUp.findViewById(R.id.btnUploadImage);
                btnUploadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFileChooser(requestCode);
                        fc = true;
                    }
                });

                btnInsert = popUp.findViewById(R.id.btnInsert);
                btnInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strName = edtName.getText().toString();
                        strAddress = edtAddress.getText().toString();
                        strClass = edtClass.getText().toString();
                        strSex = edtSex.getText().toString();
                        strHomeTown = edtHomeTown.getText().toString();

                        Log.d("TAG", "" + strName);

                        if (TextUtils.isEmpty(strName) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strClass)
                                && TextUtils.isEmpty(strHomeTown) && TextUtils.isEmpty(strSex)) {
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
                        }
//                        else if (fc == false) {
//                            Toast.makeText(MainActivity.this, "Please Choose File", Toast.LENGTH_SHORT).show();
//                        }
                        else {
                            insertData();
                            popUp.dismiss();
                        }

                    }
                });

//                btnDelete = popUp.findViewById(R.id.btnDelete);
//                btnDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                       deletedata();
//                    }
//                });
            }
        });
    }

    private void requeststoragepermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_SHORT).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser(int reqFileChooser) {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        // Will Continue at onActivityResult Method
        startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), reqFileChooser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshingLayout() {
        getData();
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        adapter = new CustomAdapter(MainActivity.this, dataItem);
        view.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
    }

//    private void deletedata() {
//        strId = CustomAdapter.id_data;
//        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Proses Delete", "Mohon Ditunggu");
//        ApiService apiService = InstanceRetrofit.getInstance();
//        Call<ResponseDeleteData> call = apiService.response_delete_data(strId);
//        call.enqueue(new Callback<ResponseDeleteData>() {
//            @Override
//            public void onResponse(Call<ResponseDeleteData> call, Response<ResponseDeleteData> response) {
//                boolean result = response.body().isResult();
//                if (result){
//                    Toast.makeText(MainActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                } else {
//                    Toast.makeText(MainActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseDeleteData> call, Throwable t) {
//                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//    }

    private void insertData() {
//        // Prepare Photo Upload
//        File file = new File(getPath(filePath));
//        RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(filePath)), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("vsimage", file.getName(), requestBody);
//        // Finish Prepare Photo Upload
//
//        String name = strName.get;
//        RequestBody address = RequestBody.create(MultipartBody.FORM, strAddress);
//        RequestBody sex = RequestBody.create(MultipartBody.FORM, strSex);
//        RequestBody hometown = RequestBody.create(MultipartBody.FORM, strHomeTown);
//        RequestBody classs = RequestBody.create(MultipartBody.FORM, strClass);

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
                Toast.makeText(MainActivity.this, "Ini" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private String getPath(Uri filePath) {
//        Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }

    private void getData() {
        ApiService api = InstanceRetrofit.getInstance();
        Call<ResponseReadData> call = api.response_read_data();
        call.enqueue(new Callback<ResponseReadData>() {
            @Override
            public void onResponse(Call<ResponseReadData> call, Response<ResponseReadData> response) {
                Boolean status = response.body().isSuccess();
                if (status) {
                    List<DataItem> dataItems = response.body().getData();
                    adapter = new CustomAdapter(MainActivity.this, dataItems);
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
            if (direction == ItemTouchHelper.RIGHT) {
                Toast.makeText(MainActivity.this, "Kekanan", Toast.LENGTH_SHORT).show();
            } else if (direction == ItemTouchHelper.LEFT) {
                Toast.makeText(MainActivity.this, "KeKIRI", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
