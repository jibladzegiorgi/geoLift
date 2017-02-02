package ge.idevelopers.Lifti.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

import ge.idevelopers.Lifti.app.connections.RetrofitApi;
import ge.idevelopers.Lifti.app.connections.RetrofitSingleTone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView ivCal;
    private EditText etInput;
    private LinearLayout llInput;
    private ImageView ivCal2;
    private EditText etInputNumber;
    private LinearLayout llInputNumber;
    private Button button;
    private Button btn;
    private LinearLayout activity_main;
    private DrawerLayout fRoot;
    private RadioButton btnStuck;
    private RadioButton btnBroken;
    private RelativeLayout rightDrawer;
    private Toolbar mToolbar;


    LocationManager locationManager;
    LocationListener locationListener;
    String provider;
    public static final String INPUTNUMBER = "ნომერი უნდა შედგებოდეს 9 ციფრისგან !";
    public static final String NO_NUMBER = "შეიყვანეთ ნომერი !";
    public static final String NOINTERNET = "ჩართეთ ინტერნეტი";
    public static final String NOINTERNET_POSSITIVE = "თავიდან სცადე";
    public static final String DIAX = "დიახ";
    public static final String LAST_NUMBER = "last number";

    double latitude;
    double longtitude;

    boolean requested = false;
    boolean updated = false;

    boolean gps_enabled = false;
    boolean network_enabled = false;
    public byte st_bk = 0;
    private RadioGroup radioGroup;
    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    Integer[] imgid = {
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
    };



    public void onMenuClicked(View view) {
        fRoot.openDrawer(rightDrawer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        askForPermission(Manifest.permission.CALL_PHONE, CALL);
    }
    //menu

    //menu

    private void initView() {
        ivCal = (ImageView) findViewById(R.id.ivCal);
        etInput = (EditText) findViewById(R.id.etInput);
        llInput = (LinearLayout) findViewById(R.id.llInput);
        ivCal2 = (ImageView) findViewById(R.id.ivCal2);
        etInputNumber = (EditText) findViewById(R.id.etInputNumber);
        llInputNumber = (LinearLayout) findViewById(R.id.llInputNumber);
        // button = (Button) findViewById(R.id.button);
        btn = (Button) findViewById(R.id.btnGamodzaxeba);
        activity_main = (LinearLayout) findViewById(R.id.activity_main);
        fRoot = (DrawerLayout) findViewById(R.id.fRoot);
 /*       radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        btnBroken = (RadioButton) findViewById(R.id.bntBroken);
        btnStuck = (RadioButton) findViewById(R.id.btnStuck);*/
        rightDrawer = (RelativeLayout) findViewById(R.id.rightDrawer);

       /* locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);



        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            makeUseOfNewLocation(location);
        } else {
            Toast.makeText(this, "Failed to send location", Toast.LENGTH_SHORT).show();
        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }*/

      /*  sliderBtn = (SlideButton) findViewById(R.id.sliderBtn);
        sliderBtn.setOnClickListener(this);*/
    }

    private void makeUseOfNewLocation(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();

    }

    private void sendSos() {
        if (etInput.getText().toString().equals("")) {
            makeDialog(NO_NUMBER, DIAX);
        } else if (etInput.getText().length() != 9) {
            makeDialog(INPUTNUMBER, DIAX);
        } else {
            SendClas goObject = new SendClas(latitude, longtitude, etInput.getText().toString(), etInputNumber.getText().toString(), st_bk);
            Log.i("l", goObject.toString());
            //=========================================================================
            RetrofitApi api = RetrofitSingleTone.getInstance().getRetrofitApi();
            final Call<SendClas> send = api.send(goObject);
            send.enqueue(new Callback<SendClas>() {
                @Override
                public void onResponse(Call<SendClas> call, Response<SendClas> response) {
                }

                @Override
                public void onFailure(Call<SendClas> call, Throwable t) {

                }
            });
            SentDialog("მოთხოვნა გაიგზავნა, გთხოვთ დაელოდოთ", DIAX);


            //=========================================================================
        }
    }

    public void callOperator() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "557002838"));
        startActivity(intent);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }

    public void checkLocation() {
        final MainActivity context = this;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("თუ გსურთ მოთხოვნი გადაგზავნა ჩართეთ GPS");
            dialog.setPositiveButton("open location settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            AlertDialog alert = dialog.create();

            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.parseColor("#007AFF"));
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.parseColor("#007AFF"));
        }
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;


    }

    public void makeDialog(String message, String positiveButtonText) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this)
                .setTitle("შეცდომა !")
                .setMessage(message)
                .setIcon(R.drawable.crashcallicon)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                    }
                });
//        builder1.show();

        AlertDialog alert = builder1.create();

        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#007AFF"));

    }

    public void SentDialog(String message, String positiveButtonText) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                    }
                });
//        builder1.show();

        AlertDialog alert = builder1.create();

        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#007AFF"));

    }

    public void sosClick(View view) {

        hideKeyboard();

        if (!checkInternetConnection()) {
            makeDialog(NOINTERNET, NOINTERNET_POSSITIVE);
            return;
        }
        if (etInput.getText().toString().equals("")) {
            makeDialog(NO_NUMBER, DIAX);
        } else if (etInput.getText().length() != 9) {
            makeDialog(INPUTNUMBER, DIAX);
        } else {
            // LiftiApplication.getInstance().setSharedString(LAST_NUMBER, etInput.getText().toString());

            //=========================================================================
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("ნამდვილად გსურთ ოპერატორი ამ ნომერზე " + etInput.getText().toString() + " დაგიკავშირდეთ ?");
            dialog.setPositiveButton("კი", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    sendSos();
                }
            });
            dialog.setNegativeButton("არა", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            AlertDialog alert = dialog.create();

            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.parseColor("#007AFF"));
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.parseColor("#007AFF"));
            //=========================================================================
        }

        //=========================================================================
    }

    public void checkSettings() {
        //=========================================================================
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {


            //=========================================================================
            if (checkInternetConnection()) {
                initLocationManager();
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkLocation();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            78);
                }
            } else {
                makeDialog(NOINTERNET, NOINTERNET_POSSITIVE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CHANGE_NETWORK_STATE},
                    77);
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gamodzaxeba(View v) {
        if (btnBroken.isChecked()) {
            st_bk = 1;
            Calendar currentTime = Calendar.getInstance();
            Calendar schoolTime = Calendar.getInstance();
            Calendar schoolClosedTime = Calendar.getInstance();
            schoolTime.set(Calendar.HOUR_OF_DAY, 10);
            schoolTime.set(Calendar.MINUTE, 0);
            schoolTime.set(Calendar.SECOND, 0);
            schoolTime.set(Calendar.MILLISECOND, 0);
            schoolClosedTime.set(Calendar.HOUR_OF_DAY, 19);
            //schoolClosedTime.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY,Calendar.SUNDAY);
            schoolClosedTime.set(Calendar.MINUTE, 0);
            schoolClosedTime.set(Calendar.SECOND, 0);
            schoolClosedTime.set(Calendar.MILLISECOND, 0);
            if (currentTime.after(schoolTime) && currentTime.before(schoolClosedTime)) {
                if (!checkInternetConnection()) {
                    makeDialog(NOINTERNET, NOINTERNET_POSSITIVE);
                    return;
                } else {
                    sendSos();
                }
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("გსურთ ოპერატორთან დარეკვა?");
                dialog.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        callOperator();
                    }
                });
                dialog.setNegativeButton("არა", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
                AlertDialog alert = dialog.create();

                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.parseColor("#007AFF"));
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.parseColor("#007AFF"));
            }

        } else if (btnStuck.isChecked()) {
            st_bk = 0;
            Calendar currentTime = Calendar.getInstance();
            Calendar schoolTime = Calendar.getInstance();
            Calendar schoolClosedTime = Calendar.getInstance();
            schoolTime.set(Calendar.HOUR_OF_DAY, 10);
            schoolTime.set(Calendar.MINUTE, 0);
            schoolTime.set(Calendar.SECOND, 0);
            schoolTime.set(Calendar.MILLISECOND, 0);
            schoolClosedTime.set(Calendar.HOUR_OF_DAY, 19);
            //schoolClosedTime.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY,Calendar.SUNDAY);
            schoolClosedTime.set(Calendar.MINUTE, 0);
            schoolClosedTime.set(Calendar.SECOND, 0);
            schoolClosedTime.set(Calendar.MILLISECOND, 0);
            if (currentTime.after(schoolTime) && currentTime.before(schoolClosedTime)) {

                if (!checkInternetConnection()) {
                    makeDialog(NOINTERNET, NOINTERNET_POSSITIVE);
                    return;
                } else {
                    sendSos();
                }
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("გსურთ ოპერატორთან დარეკვა?");
                dialog.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        callOperator();
                    }
                });
                dialog.setNegativeButton("არა", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
                AlertDialog alert = dialog.create();

                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.parseColor("#007AFF"));
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.parseColor("#007AFF"));
            }

        }
    }

    public void initLocationManager() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //makeUseOfNewLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

}
