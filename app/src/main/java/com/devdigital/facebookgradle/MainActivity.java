package com.devdigital.facebookgradle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devdigital.facebookgradle.core.CoreActivity;
import com.devdigital.pickcrop.callbacks.DefaultCallback;
import com.devdigital.pickcrop.core.GlibImage;
import com.devdigital.pickcrop.core.ImageHelper;
import com.devdigital.pickcrop.core.UCropHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends CoreActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ImageHelper.RuntimePermissionCallback, UCropHelper.UCropImageCallback {

    /* Object, Veriable Declaraction     */
    private LoginButton btnFacebookLogin;
    private CallbackManager callbackManager;
    private static final String PERMISSION = "publish_actions";
    private Button btnPostPhoto;
    private ImageHelper imageHelper;
    private UCropHelper uCropHelper;
    private TextView txtUserName;
    private Button btnPost;
    private ShareDialog shareDialog;
    //    private SimpleDraweeView imgSharePhoto;
    private ImageView imgSharePhoto;
    private Button btnTakePhoto;
    private Button btnPickPhoto;
    private Bitmap bitmap;

    /* END */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initUI();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void initUI() {
//        Log.v("Here is Key", "Copy it====>    " + CoreApplication.printKeyHash(this));
//        loadFragment(new LoginFragment());
        imageHelper = new ImageHelper(MainActivity.this, MainActivity.this);
        uCropHelper = new UCropHelper(MainActivity.this, MainActivity.this);

        txtUserName = (TextView) findViewById(R.id.txt_username);
        btnPost = (Button) findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);
        btnPostPhoto = (Button) findViewById(R.id.btn_post_photo);
        btnPostPhoto.setOnClickListener(this);
//        imgSharePhoto = (SimpleDraweeView) findViewById(R.id.img_share_photo);
        imgSharePhoto = (ImageView) findViewById(R.id.img_share_photo);
        btnPickPhoto = (Button) findViewById(R.id.btn_pick_photo);
        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        btnPickPhoto.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);
        initFacebookButton();

    }

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {

        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String backStackName = fragment.getClass().getSimpleName();
        fragmentTransaction.replace(R.id.containter, fragment);
        fragmentTransaction.addToBackStack(backStackName);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        imageHelper.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, GlibImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, GlibImage.ImageSource source, int type) {
                // Handle the image
                // imagefile is the location of your image.
                // for example
                /*Picasso.with(this)
                .load(photoUri)
                .fit()
                .centerCrop()
                .into(imageView);*/
                // or Pass image for cropping
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                imgSharePhoto.setImageURI(Uri.fromFile(imageFile));
//                uCropHelper.startCropping(Uri.fromFile(imageFile));
            }

            @Override
            public void onCanceled(GlibImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == GlibImage.ImageSource.CAMERA) {
                    File photoFile = GlibImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                postStatus();
                break;
            case R.id.btn_post_photo:
                postPhoto();
                break;
            case R.id.btn_take_photo:
                imageHelper.pick(true);
                break;
            case R.id.btn_pick_photo:
                imageHelper.pick(false);
                break;
        }
    }

    private void initFacebookButton() {
        btnFacebookLogin = (LoginButton) findViewById(R.id.btn_facebook_button);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ON Success", "********* SUCCESS ****************");
                txtUserName.setText("==> " + loginResult.getAccessToken().getUserId());
                Toast.makeText(MainActivity.this, "Hello Success", Toast.LENGTH_SHORT).show();
                Profile profile = Profile.getCurrentProfile();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("ON EROORR", "********* ERROR ****************");
                Toast.makeText(MainActivity.this, "Errorororororor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postStatus() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Hello Facebook Gradle")
                .setContentDescription("Greetins Dev Digital Facebook Gradle")
                .setContentUrl(Uri.parse("http://devdigital.appteam.com"))
                .build();

        shareDialog.show(linkContent);
        if (profile != null) {
            ShareApi.share(linkContent, shareCallback);
        }
    }

    private void postPhoto() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        if (bitmap == null) {
            Toast.makeText(MainActivity.this, "Choose Image First", Toast.LENGTH_SHORT).show();
        } else {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
            ArrayList<SharePhoto> photos = new ArrayList<>();
            photos.add(sharePhoto);

            SharePhotoContent sharePhotoContent =
                    new SharePhotoContent.Builder().setPhotos(photos).build();

//        if (ShareDialog.canShow(SharePhotoContent.class)) {
            ShareApi.share(sharePhotoContent, shareCallback);
            shareDialog.show(sharePhotoContent);


            // We need to get new permissions, then complete the action when we get called back.
            LoginManager.getInstance().logInWithPublishPermissions(
                    this,
                    Arrays.asList(PERMISSION));
//        }
        }

    }

    @Override
    protected void onDestroy() {
        ImageHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPermissionGranted(boolean isCamera) {
        if (isCamera) {
            ImageHelper.onTakePhotoClick();
        } else {
            ImageHelper.onPickFromGaleryClicked();
        }
    }

    @Override
    public void handleCropSuccess(Uri resultUri) {

    }

    @Override
    public void handleCropError(String message) {

    }
}
