package com.example.elder.printstop.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.elder.printstop.AddMoreFiles;
import com.example.elder.printstop.R;
import com.example.elder.printstop.adapter.RecyclerViewAdapterMainScreen;
import com.example.elder.printstop.model.FileToPrint;
import com.example.elder.printstop.util.Downloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainScreen extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<FileToPrint> mFiles;
    private RecyclerViewAdapterMainScreen mRecyclerViewAdapterMainScreen;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //TODO:remove later
        mFiles = getFakeData();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_main_files_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapterMainScreen = new RecyclerViewAdapterMainScreen(this,mFiles);
        mRecyclerView.setAdapter(mRecyclerViewAdapterMainScreen);

//        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.print_fab);
//        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        openWebView("marm08.pdf");
//        Used to add a line between the cells
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
//        render();



        // http://stackoverflow.com/questions/20603713/opening-pdf-file-from-server-using-android-intent
//        String extStorageDirectory = Environment.getExternalStorageDirectory()
//                .toString();
//        File folder = new File(extStorageDirectory, "pdf");
//        folder.mkdir();
//        File file = new File(folder, "Read.pdf");
//        try {
//            file.createNewFile();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        Downloader.DownloadFile("http://122.248.233.68/pvfiles/Guide-2.pdf", file);

        showPdf();
    }

    private void showPdf() {

//        File file = new File(Environment.getExternalStorageDirectory()+"/Mypdf/Read.pdf");
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(filepath + "/"+ "marm08.pdf");

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
       }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //render("a04v33n3.pdf");
    }

//    private void render(String fileName) {
//
//        try {
//            mImageView = (ImageView) findViewById(R.id.img_pdf_file);
//            int REQ_WIDTH = 1000;//mImageView.getWidth();
//            int REQ_HEIGHT = mImageView.getHeight() + 1000;
//            int currentPage = 0;
//
//
//
//            Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Config.ARGB_4444);
//
//            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//            File file = new File(path + "/"+ fileName);
//            PdfRenderer pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
//
//            Matrix m = mImageView.getMatrix();
//            Rect rec = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
//            pdfRenderer.openPage(currentPage).render(bitmap,rec, m, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
//            mImageView.setImageMatrix(m);
//            mImageView.setImageBitmap(bitmap);
//            mImageView.invalidate();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }



    public void btnHelpClicked(View view){
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    public void btnAddMoreFilesClicked (View view){
        Intent intent = new Intent(this, AddMoreFiles.class);
        startActivity(intent);
    }

    public void openWebView(String fileName){


        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(filepath + "/"+ fileName);
 //       File file = new File("/sdcard/example.pdf");

        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
                String pdfurl = "http://machado.mec.gov.br/images/stories/pdf/romance/marm08.pdf"; //YOUR URL TO PDF
                String googleDocsUrl = "http://docs.google.com/viewer?url="+ pdfurl;
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(googleDocsUrl ), "text/html");
                startActivity(intent);


            }
        }
    }




    public ArrayList<FileToPrint> getFakeData() {

        ArrayList<FileToPrint> files = new ArrayList<>();

        String[] arquivos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();

        String pathUSB  = getApplicationContext().getExternalCacheDir().getAbsolutePath();
        Log.i("SSS", Environment.getExternalStorageDirectory().getAbsolutePath());// + APP_THUMBNAIL_PATH_SD_CARD;)
        Log.i("SSS", pathUSB);// + APP_THUMBNAIL_PATH_SD_CARD;)

        if(arquivos != null) {
            for (String s : arquivos) {
                FileToPrint file = new FileToPrint();

                file.setName(s);
                file.setFileSize(20);
                files.add(file);
            }
        }
        return files;
    }

    public void btnLogoutClicked(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        //finish();
    }
}
