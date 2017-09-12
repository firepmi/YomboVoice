package com.app.yombovoice.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.yombovoice.R;
import com.app.yombovoice.common.Globals;
import com.app.yombovoice.common.Utils;
import com.app.yombovoice.common.VoiceMessage;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class OptionActivity extends AppCompatActivity {
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    Uri fileURI;
    String file_path = "";
    String filename = "";
    byte[] imageBytes;
    int CAMERA = 100;
    int IMAGE = 300;
    ImageView imageView;
    EditText editTextName;
    EditText editTextDescription;
    ParseFile file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getSupportActionBar().hide();
        ImageButton btn_before = (ImageButton)findViewById(R.id.btn_1);
        btn_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionActivity.this, MainActivity.class));
                finish();
            }
        });
        imageView = (ImageView)findViewById(R.id.img_bg);
        ImageButton btn_cam= (ImageButton)findViewById(R.id.btn_bg);
        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPictureDialog();
            }
        });
        uploadPictureDialog();
        ImageButton btn_submit = (ImageButton)findViewById(R.id.btn_send);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        ImageButton btn_setting = (ImageButton)findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionActivity.this, SettingActivity.class));
            }
        });

        audioPlayer(AUDIO_RECORDER_FOLDER, "vombo.mp4");
    }
    private void submit(){
        Toast.makeText(OptionActivity.this, "Voice message sent successfully! ",
                Toast.LENGTH_LONG).show();
        uploadFile();
    }
    private void uploadFile(){
        if(null != file_path && file_path.length() != 0) {
            final ProgressDialog dia = ProgressDialog.show(OptionActivity.this, null, "Saving Message...");

            file = new ParseFile(filename, imageBytes);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException arg0) {
                    dia.dismiss();
                    if (arg0 == null) {
                        if (null != file_path && file_path.length() != 0 && file != null) {
                            registerPicture();
                        }
                    }else {
                        Log.e("File", arg0.getMessage());
                    }
                }
            });
        }
        else Utils.showDialog(OptionActivity.this, "Please select background picture!");
    }
    private void registerPicture(){
        final ProgressDialog dia = ProgressDialog.show(OptionActivity.this, null, getString(R.string.alert_wait));
        VoiceMessage voiceMessage = new VoiceMessage();
//        voiceMessage.setImage();
//        post.setDescription(editTextDescription.getText().toString());
//        post.put("picture", file);
//        post.saveEventually(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                dia.dismiss();
//                if (e == null) {
//                    file_path = "";
//                    filename = "";
//                    sendNotification();
//                } else {
//                    Utils.showDialog(UploadPictureActivity.this, "Error: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
    }
    private void uploadPictureDialog(){
        CharSequence[] str = { "Take Photo", "Open Gallery", "Cancel" };
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OptionActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder
                .setTitle("Change background image")
                .setItems(str, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String fName = "vombo.jpg";
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, fName);
                                fileURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                Intent iintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                iintent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);
                                startActivityForResult(iintent, CAMERA);
                                break;
                            case 1:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE);
                                break;
                            default:
//                                finish();
                                break;
                        }
                    }
                });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();
    }
    public String getPath(Uri uri) {
        // just some safety built in

        if (uri == null) {
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        // this is our fallback here
        return uri.getPath();
    }
    /////////////////////Upload picture Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA) {

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(fileURI, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                file_path = cursor.getString(column_index_data);
                filename = file_path.substring(file_path.lastIndexOf("/") + 1);

                Bitmap myBitmap = BitmapFactory.decodeFile(file_path);

                imageView.setImageBitmap(myBitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageBytes = baos.toByteArray();

                Globals.backgroudImageBytes = imageBytes;

            } else if (requestCode == IMAGE) {

                Uri selectedImageUri = data.getData();
                file_path = getPath(selectedImageUri);
                filename = file_path.substring(file_path.lastIndexOf("/") + 1);

                Bitmap myBitmap = BitmapFactory.decodeFile(file_path);

                imageView.setImageBitmap(myBitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageBytes = baos.toByteArray();

                Globals.backgroudImageBytes = imageBytes;
            }
        }
    }
    //////////////////////////Play Audio
    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();
        FileInputStream fis = null;
        try {
            String s = "/sdcard/"+path + File.separator + fileName;
            mp.setDataSource(s);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
        } catch (Exception e) {
//            txt_log.setText("Play Exception");
            e.printStackTrace();
        }
    }
}
