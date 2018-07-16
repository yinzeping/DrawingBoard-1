package com.zyp.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileOutputStream;

import uk.co.senab.photoview.PhotoView;
/**
 * Created by zhangyiipeng on 2018/7/6.
 */

public class ImageActivity extends AppCompatActivity {

    private Bitmap drawBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        drawBitmap = drawBg4Bitmap(Color.WHITE, MainActivity.bitmap);
        PhotoView photoView = findViewById(R.id.photo_view);
        photoView.setImageBitmap(drawBitmap);
        findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存
                alertDialog();

            }
        });

    }

    private void alertDialog() {
        new MaterialDialog.Builder(ImageActivity.this)
                .title("保存")
                .content("")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("手绘名称(.png)", "DrawingBoard.png", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        String drawName = input.toString();
                        Log.d("AAA", drawName);

                        saveDrawBimap(drawBitmap, drawName);
                    }
                }).show();
    }

    public void saveDrawBimap(final Bitmap bitmap, final String imgName) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("保存手绘")
                .content("保存中...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();


        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (bitmap != null) {
                    try {
                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DrawingBoard/";
                        File dir = new File(filePath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File f = new File(filePath, imgName);
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        FileOutputStream out = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.close();

                        dialog.dismiss();
                        return "保存手绘成功" + filePath;
                    } catch (Exception e) {

                        dialog.dismiss();
                        Log.i("AAA", e.getMessage());
                        return "保存手绘失败" + e.getMessage();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(ImageActivity.this, (String) o, Toast.LENGTH_SHORT).show();

            }
        }.execute("");
    }


    public Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }
}
