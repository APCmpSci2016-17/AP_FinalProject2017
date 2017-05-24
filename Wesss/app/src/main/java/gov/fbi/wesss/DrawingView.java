package gov.fbi.wesss;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;

import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class DrawingView extends View {
    // Canvas for drawing
    private Canvas drawCanvas;
    // Bitmap of the canvas
    private Bitmap canvasBitmap;
    // Drawing path
    private Path drawPath;
    // Drawing paint
    private Paint drawPaint, canvasPaint;
    // The color black
//    private final int BLACK = 0xFF000000;
    // The color white
//    private final int WHITE = 0xFFFFFFFF;
//    private ArrayList

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();

        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStrokeWidth(8);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int width, int height, int lastWidth, int lastHeight) {
        super.onSizeChanged(width, height, lastWidth, lastHeight);

        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }
//    @Override
    protected void onDraw(Canvas canvas) {
//        System.out.println("ON DRAW CALLED");
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
//        drawPath.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("ON TOUCH CALLED");
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    // Sets canvas color to white
    public void clearCanvas() {
//        System.out.println("CLEAR CANVAS CALLED!");
        drawPath.reset();
        canvasBitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    // Draw a bitmap
    public void drawBitmap(Bitmap bm) {
        drawPath.reset();
        canvasBitmap = Bitmap.createScaledBitmap(bm, drawCanvas.getWidth(), drawCanvas.getHeight(), false);
        invalidate();
    }

    // Undos the last change
    public void undo() {
        System.out.println("UNDO CALLED");
    }
}
