package gov.fbi.wesss;

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

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private DrawingView drawView;
    private ImageButton clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView)findViewById(R.id.drawing);
        clearBtn = (ImageButton) findViewById(R.id.x_btn);
        clearBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.x_btn) {
            //Clear button clicked
            System.out.println("CLEAR BUTTON PRESSED");
            drawView.clearCanvas();
        }
    }
}

