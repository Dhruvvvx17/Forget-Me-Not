package com.example.mlabsystem2.setup_2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity_aws1 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyActivity";

    private ArrayList<Uri> fileUrilist = new ArrayList<>();
    private ArrayList<String> Imagelist = new ArrayList<>();

    private final String KEY = Credentials.KEY;
    private final String SECRET = Credentials.SECRET;

    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;

    private UploadListAdapter adapter;
    private RecyclerView recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    //track Choosing Image Intent
    private static final int CHOOSING_IMAGE_REQUEST = 1234;

    private TextView tvFileName;
    private ImageView imageView;

    private Uri fileUri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aws1);

        imageView = findViewById(R.id.img_file);
        tvFileName = findViewById(R.id.tv_file_name);
        tvFileName.setText("");

        findViewById(R.id.btn_choose_file).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_download).setOnClickListener(this);

        AWSMobileClient.getInstance().initialize(this).execute();

        credentials = new BasicAWSCredentials(KEY, SECRET);

        s3Client = new AmazonS3Client(credentials);
    }

    private void uploadFile() {

            if (fileUri != null) {
                final String fileName = "detectablepic";

                if (!validateInputFileName(fileName)) {
                    return;
                }

                final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "/" + fileName);

                createFile(getApplicationContext(), fileUri, file);

                TransferUtility transferUtility =
                        TransferUtility.builder()
                                .context(getApplicationContext())
                                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                .s3Client(s3Client)
                                .build();

                TransferObserver uploadObserver =
                        transferUtility.upload("Detectable/" + fileName + "." + getFileExtension(fileUri), file);

                uploadObserver.setTransferListener(new TransferListener() {

                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (TransferState.COMPLETED == state) {
                            Toast.makeText(getApplicationContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();

                            file.delete();
                        } else if (TransferState.FAILED == state) {
                            file.delete();
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                        int percentDone = (int) percentDonef;

                        tvFileName.setText("ID:" + id + "|bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        ex.printStackTrace();
                    }

                });
            }
    }

    private void downloadFile() {

        int Finalsize = 0;

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            for (S3ObjectSummary summary : S3Objects.withPrefix(s3Client, "dhruvtest-userfiles-mobilehub-701823593", "DetectedFaces/")) {
                Log.i(TAG, summary.getKey());
                Finalsize = Finalsize + 1;
            }
        }

        for ( int i = 0; i< Finalsize ; i++) {

            if (fileUri != null) {

                final String fileName = "Face" + Integer.toString(i);

                if (!validateInputFileName(fileName)) {
                    return;
                }

                try {
                    final File localFile = File.createTempFile("images", getFileExtension(fileUri));
                    final String Imagepath = localFile.getAbsolutePath();
                    Imagelist.add(Imagepath);

                    TransferUtility transferUtility =
                            TransferUtility.builder()
                                    .context(getApplicationContext())
                                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                    .s3Client(s3Client)
                                    .build();


                    //   ObjectListing objects = s3Client.listObjects(listObjectsRequest);

                    TransferObserver downloadObserver =
                            transferUtility.download("DetectedFaces/" + fileName + "." + getFileExtension(fileUri), localFile);

                    downloadObserver.setTransferListener(new TransferListener() {

                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (TransferState.COMPLETED == state) {
                             //   Toast.makeText(getApplicationContext(), "Download Completed!", Toast.LENGTH_SHORT).show();

                             //   tvFileName.setText(fileName + "." + getFileExtension(fileUri));
                                if (adapter == null) {
                                    recyclerview = (RecyclerView) findViewById(R.id.recycler);
                                    layoutManager = new LinearLayoutManager(MainActivity_aws1.this, LinearLayoutManager.VERTICAL, false);
                                    recyclerview.setHasFixedSize(true);
                                    recyclerview.setLayoutManager(layoutManager);
                                    adapter = new UploadListAdapter(Imagelist,MainActivity_aws1.this);
                                    recyclerview.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }


                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                            int percentDone = (int) percentDonef;
                            tvFileName.setText("ID:" + id + "|bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            ex.printStackTrace();
                        }

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Upload file before downloading", Toast.LENGTH_LONG).show();
            }
            int p = Imagelist.size();
        }
        Toast.makeText(getApplicationContext(), "Download Completed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.btn_choose_file) {
            showChoosingFile();
        } else if (i == R.id.btn_upload) {
            uploadFile();
        } else if (i == R.id.btn_download) {
            downloadFile();
        }
    }

    private void showChoosingFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (bitmap != null) {
            bitmap.recycle();
        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                } catch (IOException e) {
                e.printStackTrace();
                }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean validateInputFileName(String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(this, "Enter file name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FaceUpload(Context mycontext,String name,String path){
        final File file = new File(path);

        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(mycontext)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload("Faces/" + name + ".jpg", file);


    }
}