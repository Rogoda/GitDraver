package com.example.rafal.gitdraver;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends Activity {

    ImageView iv;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //iv = (ImageView) findViewById(R.id.imageView);
        v = findViewById(R.id.view);



        if(photo) {
//            ImageView imageView = (ImageView) findViewById(R.id.imageView);
//            imageView.setImageBitmap(imageBitmap);

            File imgFile = new  File(path);

            //if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.imageView);

                myImage.setImageBitmap(myBitmap);

           // }

        }
    }

    private static final String key ="key";

void share()
{

    Intent share = new Intent(Intent.ACTION_SEND);
    share.setType("image/png");

    ContentValues values = new ContentValues();
    values.put(Images.Media.TITLE, "title");
    values.put(Images.Media.MIME_TYPE, "image/PNG");
    Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
            values);


    OutputStream outstream;
    try {
        outstream = getContentResolver().openOutputStream(uri);
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
        outstream.close();
    } catch (Exception e) {
        System.err.println(e.toString());
    }

    share.putExtra(Intent.EXTRA_STREAM, uri);
    startActivity(Intent.createChooser(share, "Share Image"));
}

//
//    private File savebitmap(String filename) {
//        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//        OutputStream outStream = null;
//
//        File file = new File(filename + ".png");
//        if (file.exists()) {
//            file.delete();
//            file = new File(extStorageDirectory, filename + ".png");
//            Log.e("file exist", "" + file + ",Bitmap= " + filename);
//        }
//        try {
//            // make a new bitmap from your file
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());
//
//            outStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("file", "" + file);
//        return file;
//
//    }

//    private void save() {
//        if (imageBitmap != null) {
//
//            FileOutputStream out = null;
//            try {
//                out = new FileOutputStream(path);
//                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//                // PNG is a lossless format, the compression factor (100) is ignored
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    String path = "GitPhoto.png";

    public void share(View v) {


        if (imageBitmap != null) {

            File file = new File(path);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(path);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }
        else
        toast("Nie zrobiono zdjęcia");
    }

    private void toast(String messange)
    {
        Toast.makeText(getApplicationContext(),messange,Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //to sie dzieje w orientacji poziomej
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static boolean photo = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public void ClickOn(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

        photo = true;
    }

    static Bitmap imageBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap)extras.get("data");
                //ImageView imageView = (ImageView)findViewById(R.id.imageView);
                //imageView.setImageBitmap(imageBitmap);
                //save();
            }else
            {
                Toast.makeText(this,"Ups...",Toast.LENGTH_SHORT).show();
            }
        }
    }
}