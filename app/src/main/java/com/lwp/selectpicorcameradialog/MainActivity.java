package com.lwp.selectpicorcameradialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lwp.library.SelectPicOrCameraDialog;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SelectPicOrCameraDialog.OnSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_REQUEST_CODE = 0x1111;
    private static final int ALBUM_REQUEST_CODE = 0x1112;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mSelectButton = (Button) findViewById(R.id.select_btn);
        mImageView = (ImageView) findViewById(R.id.image);

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPicOrCameraDialog dialog = new SelectPicOrCameraDialog(MainActivity.this);
                dialog.setOnSelectedListener(MainActivity.this);
                dialog.show();
            }
        });
    }

    @Override
    public void OnSelected(View v) {
        switch (v.getId()) {
            case R.id.tv_photograph:
                startUpCamera();
                break;
            case R.id.tv_select_photo:
                startUpAlbum();
                break;
            default:
                break;
        }
    }

    private void startUpCamera() {
        /**
         * TODO: 添加权限
         * <uses-permission android:name="android.permission.CAMERA"/>
         * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
         */
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempCameraFile()));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private File getTempCameraFile() {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            Log.d(TAG, path);
            file = new File(path, "temp.jpg");
            Log.d(TAG, file.getAbsolutePath());
        } else {
            Toast.makeText(this, "SDCard 当前不可用", Toast.LENGTH_LONG).show();
            throw new RuntimeException();
        }
        return file;
    }

    private void startUpAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        albumIntent.addCategory(Intent.CATEGORY_OPENABLE).setType("image/*");
        startActivityForResult(albumIntent, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    File file = getTempCameraFile();
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    mImageView.setImageBitmap(bitmap);
                    break;
                case ALBUM_REQUEST_CODE:
                    Uri uri = data.getData();
                    // TODO: Android4.4以上由于在一些手机上 uri.getPath()获取不到其绝对路径，可以通过 CommonUtil.getPath(this,uri);
                    Log.d(TAG,uri.getPath());
                    mImageView.setImageURI(uri);
                    break;
            }
        } else {
            Toast.makeText(this, "resultCode:" + resultCode, Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
