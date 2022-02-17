package com.example.doxethongminh.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.doxethongminh.Model.XevaoModel;
import com.example.doxethongminh.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class xevaoFragment extends Fragment {
    private Button btnxevao, button_taoma;
    private ImageView imageViewcamera, imagenguoigui, vungmavach;
    private EditText text_khuvuc, text_bienso;
    private static final int CAMERA_REQUEST_CODE = 1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Firebase root;

    FirebaseUser user;
    DatabaseReference reference;
    String UserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_xevao, container, false);
        Firebase.setAndroidContext(this.getActivity().getApplication());
        root = new Firebase("https://baigiuxethongminh-6d643-default-rtdb.firebaseio.com/");
        StorageReference storageRef = storage.getReferenceFromUrl("gs://baigiuxethongminh-6d643.appspot.com/image");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();

        button_taoma = v.findViewById(R.id.button_taoma);
        imagenguoigui = v.findViewById(R.id.anhnguoigui);
        imageViewcamera = v.findViewById(R.id.camera);
        btnxevao = v.findViewById(R.id.btnxevao);
        text_khuvuc = v.findViewById(R.id.text_khuvuc);
        text_bienso = v.findViewById(R.id.text_bienso);
        vungmavach = v.findViewById(R.id.vungmavach);

        imageViewcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        //click button tạo mã QR code video create app in ahmed ibrahim
        button_taoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textbienso = text_bienso.getText().toString();
                textbienso = textbienso.toUpperCase();
                textbienso = textbienso.trim();
                MultiFormatWriter writer = new MultiFormatWriter();
                if (textbienso.isEmpty()) {
                    text_bienso.setError("mời nhập biển số");
                    text_bienso.requestFocus();
                    return;
                }
                try {
                    BitMatrix bitMatrix = writer.encode(textbienso.trim(), BarcodeFormat.QR_CODE, 300, 300);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(bitMatrix);
                    vungmavach.setImageBitmap(bitmap);
                    InputMethodManager manager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }


        });

// button save
        btnxevao.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String textbienso = text_bienso.getText().toString();
                if (textbienso.isEmpty()) {
                    text_bienso.setError("mời nhập biển số");
                    text_bienso.requestFocus();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis());
                // Get the data from an ImageView as bytes
                imagenguoigui.setDrawingCacheEnabled(true);
                imagenguoigui.buildDrawingCache();
                Bitmap bitmap = imagenguoigui.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //khi upload thất bại
                        Toast.makeText(getActivity(), "lưu hình không thành công", Toast.LENGTH_LONG).show();
                    }
                    //khi update ảnh thành công
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //download url  lấy đường link
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String urlIMAGE = task.getResult().toString();
                                Toast.makeText(getActivity(), "Lưu hình thành công", Toast.LENGTH_LONG).show();

                                //xử lý ảnh qrcode sau khi được chuyển về kiểu byte
                                //code doan nay chuyen hinh kieu byte sang  kieu chuoi luu vao firebase video bai 13 hd lưu file
                                byte[] manhinh = ImageView_To_Byte(vungmavach);
                                String chuoihinh = Base64.encodeToString(manhinh, Base64.DEFAULT);
                                //lấy thời gian hiện tại xe vào
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());
                                //taonode database
                                XevaoModel xevaoModel = new XevaoModel(String.valueOf(urlIMAGE), text_khuvuc.getText().toString(),
                                        text_bienso.getText().toString().trim(), String.valueOf(chuoihinh), String.valueOf(currentDateandTime));

                                //video 17 khoa pham
                                reference.child(UserID).child("xevao").push().setValue(xevaoModel, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                        if (error == null) {
                                            Toast.makeText(getActivity(), "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                            imagenguoigui.setImageResource(R.drawable.img_themhinhanh);
                                            text_khuvuc.setText("");
                                            text_bienso.setText("");
                                            vungmavach.setImageResource(R.drawable.img);
                                        } else {
                                            Toast.makeText(getActivity(), "Lưu dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        return v;
    }

    //xử lý chụp hìnhs
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imagenguoigui.setImageBitmap(bitmap);
        }
    }

    // xử lý hình qrcode chuyển hình về kiểu byte
    public byte[] ImageView_To_Byte(ImageView image) {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
