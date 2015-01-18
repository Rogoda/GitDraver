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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;

public class MainActivity extends Activity {

    ImageView iv;
    View v;
    private static final String TAG = MainActivity.class.getName();
    private static final String FILENAME = "myFile.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = findViewById(R.id.view);
        iv = (ImageView) findViewById(R.id.imageView);

        if(imageBitmap != null)iv.setImageBitmap(imageBitmap);

    }
   static File file = null;
  public void share(View v) {

        if(imageBitmap !=null) {


            file = getFileStreamPath(FILENAME);
            if(file != null) {

                 //String path = Environment.getExternalAppStoragePath(this, "attatchment.html");

                //String path = Environment.getExternalStorageDirectory().getPath()

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share file");
                intent.putExtra(Intent.EXTRA_TEXT, file.getAbsolutePath());

                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(Intent.createChooser(intent , "Share"));
            }
        }else {
            toast("Nie zrobiono zdjęcia");
        }

    }


    private void writeToFile() {
        try {
            ObjectOutputStream outputStreamWriter = new ObjectOutputStream(openFileOutput(FILENAME, Context.BIND_AUTO_CREATE));
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100, outputStreamWriter);
            outputStreamWriter.close();
            imageBitmap = null;
        }

        catch (IOException e) {
            e.printStackTrace();
            //Log.e(TAG, "File write failed: " + e.toString());
        }

    }

    private void readFromFile() throws IOException {
        ObjectInputStream isteram = new ObjectInputStream(openFileInput(FILENAME));

            imageBitmap = BitmapFactory.decodeStream(isteram);

        isteram.close();
            iv.setImageBitmap(imageBitmap);
    }

    private void toast(String messange) {
        Toast.makeText(getApplicationContext(), messange, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void ClickOn(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    static Bitmap imageBitmap = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                //ImageView imageView = (ImageView)findViewById(R.id.imageView);
                //imageView.setImageBitmap(imageBitmap);

                writeToFile();
                try {
                    readFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "Ups...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
