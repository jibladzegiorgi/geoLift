package ge.idevelopers.Lifti.app.slider;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ge.idevelopers.Lifti.app.R;

/**
 * Created by GIorgi on 2/2/2017.
 */

public class Slider extends LinearLayout {
    protected final static float DEFAULT_MAX_PROGRESS = 100f;
    protected final static float DEFAULT_PROGRESS = 30f;
    protected final static float DEFAULT_PROGRESS_RADIUS = 0;
    protected final static float DEFAULT_BACKGROUND_PADDING = 0;

    GradientDrawable backgroundDrawable;
    GradientDrawable progreeColor;

    LinearLayout backgroundLinearLayout;
    LinearLayout progressLinear;
    LinearLayout.LayoutParams backgroundParams;
    LinearLayout.LayoutParams progressParams;

    private int radius;
    private int padding;
    private float max;
    private float progress;
    private int colorBackground;
    private int colorProgress;
    private boolean isReverse;
    private int parentHeight;
    private int parentWidth;
    private int test;
    private ImageView slideImage;
    private ImageView backgroundImage;
    private boolean click = false;
    private TextView sliderTExt;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupStyleable(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupStyleable(context, attrs);
    }


    //custom view simaghle da sigane
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        progressParams.width = (int) ((parentWidth / DEFAULT_MAX_PROGRESS) * progress);
        progressParams.width = (int) ((parentWidth / 2));
        //progressParams.height = (int) (parentHeight - (dp2px(padding) * 2));
        progressParams.gravity = Gravity.CENTER_VERTICAL;

        //slideImage.setLayoutParams(new LinearLayout.LayoutParams(parentHeight / 2, parentHeight / 2));

        test = parentWidth - (parentWidth / 2);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //region
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setupStyleable(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.slider_backgound, this, true);

        slideImage = new ImageView(context);
        slideImage.setImageResource(R.drawable.lift_broken);

        sliderTExt=new TextView(context);
        sliderTExt.setTextSize(10);
        sliderTExt.setText("ლიფტი დაზიანებულია");

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerProgress);

        radius = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_Radius, DEFAULT_PROGRESS_RADIUS);
        padding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_BackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));
        isReverse = typedArray.getBoolean(R.styleable.RoundCornerProgress_rcReverse, false);
        max = typedArray.getFloat(R.styleable.RoundCornerProgress_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.RoundCornerProgress_Progress, DEFAULT_PROGRESS);

        int colorBackgroundDefault = context.getResources().getColor(R.color.colorAccent);
        colorBackground = typedArray.getColor(R.styleable.RoundCornerProgress_BackgroundColor, colorBackgroundDefault);
        int colorProgressDefault = context.getResources().getColor(R.color.colorPrimary);
        colorProgress = typedArray.getColor(R.styleable.RoundCornerProgress_ProgressColor, colorProgressDefault);
        typedArray.recycle();

        init(context);
    }
//endregiond

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init(final Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        final TextView textView1= (TextView) findViewById(R.id.text_1);
        final TextView textView2= (TextView) findViewById(R.id.text_2);
        final ImageView imageView1= (ImageView) findViewById(R.id.image_1);
        final ImageView imageView2= (ImageView) findViewById(R.id.image_2);
        progressLinear=new LinearLayout(context);

        int k=(imageView1.getLayoutParams().height);

        slideImage.setLayoutParams(new ViewGroup.LayoutParams(k,k));

        backgroundDrawable = createGradientDrawable(colorBackground);
        progreeColor = createGradientDrawable(colorProgress);

        layout.setBackground(backgroundDrawable);


        progressLinear = new LinearLayout(context);

        progressParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        progressParams.setMargins((int) dp2px(padding), 0, 0, 0);
        progressLinear.setGravity(Gravity.CENTER_VERTICAL);
        progressLinear.setPadding(20,0,0,0);

        //backgroundLinearLayout.setLayoutParams(backgroundParams);
        progressLinear.setLayoutParams(progressParams);

        //backgroundLinearLayout.setGravity(Gravity.LEFT);

        //backgroundLinearLayout.setBackground(backgroundDrawable);

        progressLinear.setBackground(progreeColor);
        progressLinear.addView(slideImage);
        progressLinear.addView(sliderTExt);
        progreeColor.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        backgroundDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});//cotati naklebi unda iyos progresis kutxe
        layout.addView(progressLinear);

        //addView(backgroundLinearLayout);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click) {
                    click = true;
                    Toast.makeText(context, "click_right", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", test-dp2px(2));
                    oa.setDuration(200);
                    oa.start();
                    textView1.setVisibility(VISIBLE);
                    imageView1.setVisibility(VISIBLE);
                    textView2.setVisibility(INVISIBLE);
                    imageView2.setVisibility(INVISIBLE);
                    slideImage.setImageResource(R.drawable.sos_white);
                    sliderTExt.setText("ადამიანია გაჭედილი");
                }else {
                    click = false;
                    Toast.makeText(context, "click_left", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", 0+dp2px(2));
                    oa.setDuration(200);
                    oa.start();
                    textView1.setVisibility(INVISIBLE);
                    imageView1.setVisibility(INVISIBLE);
                    textView2.setVisibility(VISIBLE);
                    imageView2.setVisibility(VISIBLE);
                    slideImage.setImageResource(R.drawable.lift_broken);
                    sliderTExt.setText("ლიფტია დაზიანებული");
                }
            }
        });
        progressLinear.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeTop() {
                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
                final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", test-dp2px(2));
                oa.setDuration(200);
                oa.start();
                textView1.setVisibility(VISIBLE);
                imageView1.setVisibility(VISIBLE);
                textView2.setVisibility(INVISIBLE);
                imageView2.setVisibility(INVISIBLE);
                slideImage.setImageResource(R.drawable.sos_white);
                sliderTExt.setText("ადამიანია გაჭედილი");
            }

            public void onSwipeLeft() {
                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
                final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", 0+dp2px(2));
                oa.setDuration(200);
                oa.start();
                textView1.setVisibility(INVISIBLE);
                imageView1.setVisibility(INVISIBLE);
                textView2.setVisibility(VISIBLE);
                imageView2.setVisibility(VISIBLE);
                slideImage.setImageResource(R.drawable.lift_broken);
                sliderTExt.setText("ლიფტია დაზიანებული");
            }

            public void onSwipeBottom() {
                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrollView(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                super.onScrollView(e1, e2, distanceX, distanceY);
            }
        });
    }

    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
