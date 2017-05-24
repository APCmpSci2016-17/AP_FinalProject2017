package gov.fbi.wesss;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

// https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

/*
 TODO:
    ADD DARK MODE AND LIGHT MODE
    ADD UNDO (DO THIS LAST PROBABLY)
    GET CAMERA INPUT

 */

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private DrawingView drawView;
    private ImageButton clearBtn, undoBtn, camBtn;
    private final static int CAM_REQ_CODE = 1;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.context = getApplicationContext();

        drawView = (DrawingView)findViewById(R.id.drawing);

        clearBtn = (ImageButton) findViewById(R.id.x_btn);
        clearBtn.setOnClickListener(this);
        undoBtn = (ImageButton) findViewById(R.id.undo_btn);
        undoBtn.setOnClickListener(this);
        camBtn = (ImageButton) findViewById(R.id.cam_btn);
        camBtn.setOnClickListener(this);

        //For some reason this method does not exist
//        if (hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //The user has a camera

//        } else {
            // The user does not have a camera
//        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.x_btn) {
            //Clear button clicked
            drawView.clearCanvas();
        } else if (view.getId() == R.id.undo_btn) {
            //Undo button clicked
            drawView.undo();
        } else if (view.getId() == R.id.cam_btn) {
            Intent takePicInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePicInt.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicInt, CAM_REQ_CODE);
            }
        }
    }

//    @Override
//    protected void onActivityResult(int reqCode, int resCode, Intent data) {
//        if (reqCode == CAM_REQ_CODE && resCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imgBitmap = (Bitmap) extras.get("data");
//            drawView.clearCanvas();
//            drawView.drawBitmap(imgBitmap);
//        }
//    }


}

