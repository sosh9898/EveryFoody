package dct.com.everyfoody.ui.detail.edit;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;
import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_ADD;
import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_EDIT;

public class EditMenuActivity extends WhiteThemeActivity {
    @BindView(R.id.edit_menu_toolbar)Toolbar editToolbar;
    @BindView(R.id.edit_menu_title)TextView titleText;
    @BindView(R.id.menu_item_edit_image)ImageView menuImage;
    @BindView(R.id.menu_item_name)EditText menuName;
    @BindView(R.id.menu_item_price)EditText menuPrice;

    private Uri resultUri;
    private NetworkService networkService;
    private int flag, menuId;
    private StoreInfo.MenuInfo menuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        ButterKnife.bind(this);

        initData();
        setToolbar();

    }

    private void initData(){
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        Intent getData = getIntent();
        flag = getData.getExtras().getInt("addORedit");
        if(flag == MENU_EDIT){
            Gson gson = new Gson();
            menuInfo = gson.fromJson(getData.getExtras().getString("menuItem"), StoreInfo.MenuInfo.class);
            menuId = menuInfo.getMenuID();
            titleText.setText("메뉴정보 수정");
            setLayout();
        }
    }

    private void setToolbar(){
        editToolbar.setTitle("");
        setSupportActionBar(editToolbar);
        editToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLayout(){
        Glide.with(this).load(menuInfo.getMenuImageURL()).into(menuImage);
        menuName.setText(menuInfo.getMenuTitle());
        menuPrice.setText(menuInfo.getMenuPrice()+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_complete) {
           networkMenuModify();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteIcon = menu.findItem(R.id.menu_delete);
        deleteIcon.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    private void networkMenuModify(){

        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), menuName.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), menuPrice.getText().toString());

        MultipartBody.Part body;

        if (resultUri == null) {
            body = null;
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();

            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(resultUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

            File photo = new File(resultUri.toString());

            body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

        }
        if(flag == MENU_ADD) {

            Log.d("???","ADD");
            Call<BaseModel> menuAddCall = networkService.registerMenu(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), body, name, price);

            menuAddCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            ToastMaker.makeShortToast(getApplicationContext(), "추가 완료");
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    Log.d("???","ADD" + t.toString());

                }
            });
        }
        else if(flag == MENU_EDIT){
            Log.d("???","edit");

            Call<BaseModel> menuEditCall = networkService.editMenu(SharedPreferencesService.getInstance().getPrefStringData("auth_token"),menuId, body, name, price);

            menuEditCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("success")) {
                            ToastMaker.makeShortToast(getApplicationContext(), "수정 완료");
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    Log.d("???","edit" + t.toString());

                }
            });
        }


    }

    private void imagePicker(){
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(EditMenuActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        cropImage(uri);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }

    private void cropImage(Uri tempUri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarTitle("사진 편집");
        options.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        Uri mDestinationUri = Uri.fromFile(new File(getCacheDir(), tempUri.toString().substring(tempUri.toString().lastIndexOf('/') + 1)));


        UCrop.of(tempUri, mDestinationUri)
                .withOptions(options)
                .withAspectRatio(2.8f, 1.1f)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this);
    }



    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(EditMenuActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(EditMenuActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri tempUri = UCrop.getOutput(data);
            resultUri = tempUri;
            Glide.with(menuImage.getContext())
                    .load(resultUri)
                    .into(menuImage);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @OnClick(R.id.menu_item_edit_image_picker)
    public void onClickPicker(View view){
        checkPermission();
        imagePicker();
    }
}
