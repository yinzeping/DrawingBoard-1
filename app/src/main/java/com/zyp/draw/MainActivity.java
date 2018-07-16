package com.zyp.draw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageLoader;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.zyp.draw.view.DrawingBoardView;
import com.zyp.draw.view.MoveRegionView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MoveRegionView mIvTouch;
    private DrawingBoardView mDrawingBoardView;

    public static Bitmap bitmap;
    private View viewColor;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawingBoardView = findViewById(R.id.drawingBoardView);
        mIvTouch = findViewById(R.id.iv_touch);

        mIvTouch.setTouchMoveListener(new MoveRegionView.OnTouchMoveListener() {
            @Override
            public void onTouchMove(float mx, float my) {
                mDrawingBoardView.moveCanvas(mx, my);

            }
        });

        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.zoomCanvas(true);

            }
        });
        findViewById(R.id.bt_subtrct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.zoomCanvas(false);

            }
        });

        findViewById(R.id.bt_add).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDrawingBoardView.zoomQuickCanvas(true);
                return true;
            }
        });

        findViewById(R.id.bt_subtrct).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDrawingBoardView.zoomQuickCanvas(false);
                return true;
            }
        });

        findViewById(R.id.bt_subtrct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.zoomCanvas(false);

            }
        });

        final ImageView ivUndo = findViewById(R.id.iv_undo);
        ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.undo();
            }
        });

        final ImageView ivRedo = findViewById(R.id.iv_redo);

        ivRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.redo();
            }
        });

        mDrawingBoardView.setOnUndoRedoListener(new DrawingBoardView.OnUndoRedoListener() {
            @Override
            public void onUndoRedo(boolean isUndo, boolean isRedo) {
                Log.d("UNDO","isUndo:"+isUndo+",isRedo:"+isRedo);
                if (isUndo){
                    if (ivUndo.getAlpha()<1f) {
                        ivUndo.setAlpha(1f);
                    }
                }else {
                    if(ivUndo.getAlpha() == 1f) {
                        ivUndo.setAlpha(0.5f);
                    }
                }
                if (isRedo){
                    if (ivRedo.getAlpha()<1f) {
                        ivRedo.setAlpha(1f);
                    }
                }else {
                    if(ivRedo.getAlpha() == 1f) {
                        ivRedo.setAlpha(0.5f);
                    }
                }
            }
        });
        findViewById(R.id.iv_image_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });
        findViewById(R.id.bt_draw_sketch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.drawSketch();
            }
        });
        findViewById(R.id.bt_draw_colours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.drawColours();
            }
        });

        findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .content("擦除手绘")
                        .positiveText("确认")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                viewColor.setBackgroundColor(Color.BLACK);
                                mDrawingBoardView.erase();
                            }
                        })
                        .build().show();
            }
        });
        findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawingBoardView.getPaths().size() == 0) {
                    Toast.makeText(MainActivity.this, "你还没有手绘", Toast.LENGTH_SHORT).show();
                    return;
                }

                bitmap = mDrawingBoardView.getDrawBitmap();
                startActivity(new Intent(MainActivity.this,ImageActivity.class));

            }
        });

       final ImageView ivBrush =  findViewById(R.id.iv_brush);
       final ImageView ivEraser =  findViewById(R.id.iv_eraser);
        ivBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivBrush.getAlpha()<1f) {
                    ivBrush.setAlpha(1f);
                    ivEraser.setAlpha(0.5f);
                }
                mColorPicker.setColor(mDrawingBoardView.getStrokeColor());
                showPopup(v,DrawingBoardView.STROKE);
            }
        });

        ivEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivEraser.getAlpha()<1f) {
                    ivEraser.setAlpha(1f);
                    ivBrush.setAlpha(0.5f);
                }
                showPopup(v,DrawingBoardView.ERASER);
            }
        });

        viewColor = findViewById(R.id.view_color);
        initPopuWindowLayout();
        mDrawingBoardView.setPickColorListener(new DrawingBoardView.OnPickColorListener() {
            @Override
            public void onPickColor(int color) {
                viewColor.setBackgroundColor(color);
            }
        });

    }
    private ImageView strokeImageView, eraserImageView;
    private int size;
    private ColorPicker mColorPicker;

    private View popupLayout, popupEraserLayout;
    private void initPopuWindowLayout() {

        // Inflate the popup_layout.xml
        LayoutInflater inflater = (LayoutInflater) getSystemService(AppCompatActivity
                .LAYOUT_INFLATER_SERVICE);
        popupLayout = inflater.inflate(R.layout.popup_sketch_stroke, null);
        // And the one for eraser
        LayoutInflater inflaterEraser = (LayoutInflater) getSystemService(AppCompatActivity
                .LAYOUT_INFLATER_SERVICE);
        popupEraserLayout = inflaterEraser.inflate(R.layout.popup_sketch_eraser, null);

        // Actual stroke shape size is retrieved
        strokeImageView = (ImageView) popupLayout.findViewById(R.id.stroke_circle);
        final Drawable circleDrawable = getResources().getDrawable(R.drawable.circle);
        size = circleDrawable.getIntrinsicWidth();
        // Actual eraser shape size is retrieved
        eraserImageView = (ImageView) popupEraserLayout.findViewById(R.id.stroke_circle);
        size = circleDrawable.getIntrinsicWidth();

        setSeekbarProgress(DrawingBoardView.DEFAULT_ERASER_SIZE, DrawingBoardView.ERASER);
        setSeekbarProgress(DrawingBoardView.DEFAULT_STROKE_SIZE, DrawingBoardView.STROKE);

        // Stroke color picker initialization and event managing
        mColorPicker = (ColorPicker) popupLayout.findViewById(R.id.stroke_color_picker);
        mColorPicker.addSVBar((SVBar) popupLayout.findViewById(R.id.svbar));
        mColorPicker.addOpacityBar((OpacityBar) popupLayout.findViewById(R.id.opacitybar));

        mColorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                mDrawingBoardView.setStrokeColor(color);
                viewColor.setBackgroundColor(color);
            }
        });
        mColorPicker.setColor(mDrawingBoardView.getStrokeColor());
        mColorPicker.setOldCenterColor(mDrawingBoardView.getStrokeColor());
    }


    private int seekBarStrokeProgress, seekBarEraserProgress;
    private int oldColor;
    // The method that displays the popup.
    private void showPopup(View anchor, final int eraserOrStroke) {

        boolean isErasing = eraserOrStroke == DrawingBoardView.ERASER;

        oldColor = mColorPicker.getColor();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Creating the PopupWindow
        PopupWindow popup = new PopupWindow(this);
        popup.setContentView(isErasing ? popupEraserLayout : popupLayout);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mColorPicker.getColor() != oldColor)
                    mColorPicker.setOldCenterColor(oldColor);
            }
        });

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets (transformed
        // dp to pixel to support multiple screen sizes)
        popup.showAsDropDown(findViewById(R.id.view_popu));

        // Stroke size seekbar initialization and event managing
        SeekBar mSeekBar;
        mSeekBar = (SeekBar) (isErasing ? popupEraserLayout
                .findViewById(R.id.stroke_seekbar) : popupLayout
                .findViewById(R.id.stroke_seekbar));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // When the seekbar is moved a new size is calculated and the new shape
                // is positioned centrally into the ImageView
                setSeekbarProgress(progress, eraserOrStroke);
            }
        });
        int progress = isErasing ? seekBarEraserProgress : seekBarStrokeProgress;
        mSeekBar.setProgress(progress);
        setSeekbarProgress(progress, eraserOrStroke);

    }


    protected void setSeekbarProgress(int progress, int eraserOrStroke) {
        int calcProgress = progress > 1 ? progress : 1;

        int newSize = Math.round((size / 100f) * calcProgress);
        int offset = Math.round((size - newSize) / 2);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newSize, newSize);
        lp.setMargins(offset, offset, offset, offset);
        if (eraserOrStroke == DrawingBoardView.STROKE) {
            strokeImageView.setLayoutParams(lp);
            seekBarStrokeProgress = progress;
        } else {
            eraserImageView.setLayoutParams(lp);
            seekBarEraserProgress = progress;
        }

        mDrawingBoardView.setSize(newSize, eraserOrStroke);
        Log.e("PPP","newSize:"+newSize+" , eraserOrStroke:"+eraserOrStroke);
    }



    public void openImageSelector() {
        ImageConfig imageConfig = new ImageConfig.Builder(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context)
                        .load(path)
                        .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                        .centerCrop()
                        .into(imageView);
            }
        })
                .steepToolBarColor(getResources().getColor(R.color.blue))
                .titleBgColor(getResources().getColor(R.color.blue))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                //截屏
                //.crop()
                // 开启单选   （默认为多选）
                .singleSelect()
                // 开启拍照功能 （默认关闭）
                .showCamera()
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/DrawingBoard/Pictures")
                .build();

        ImageSelector.open(this, imageConfig);   // 开启图片选择器
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Get Image Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                Log.d("ImagePathList", path);
                Glide.with(this).load(path)
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                mDrawingBoardView.setDrawBgBitmap(bitmap);
                            }

                        });
            }
        }

    }




}
