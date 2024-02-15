package com.example.tablayoutviewpager.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tablayoutviewpager.Adapter.SwiperAdapter;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.ActivityPreViewBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PreViewActivity extends AppCompatActivity {



    ActivityPreViewBinding binding;
    private ViewPager2 viewPager2;
    private List<Pictures> list;
    public static int REQUEST_CODE=1001;

    private static String STORAGE_PERMISSION;
    private static String READ_PERMISSION;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreViewBinding.inflate(getLayoutInflater());
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
        }
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            STORAGE_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES;
        }else {
            STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
        }



        viewPager2 = findViewById(R.id.viewPager);
        list = getIntent().<Pictures>getParcelableArrayListExtra("picturesList");
        int position = getIntent().getIntExtra("position", -1);

        viewPager2.setAdapter(new SwiperAdapter(this, list));
        viewPager2.setCurrentItem(position);
        setSupportActionBar(binding.topAppBar);
        if (getSupportActionBar() != null && list != null && !list.isEmpty()) {
            getSupportActionBar().setTitle(list.get(position).getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (getSupportActionBar() != null && list != null && !list.isEmpty()) {
                    getSupportActionBar().setTitle(list.get(position).getName());
                }
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager2.getCurrentItem();
                RecyclerView.Adapter adapter = viewPager2.getAdapter();
                if (adapter instanceof SwiperAdapter) {
                    SwiperAdapter swiperAdapter = (SwiperAdapter) adapter;
                    Pictures currentPicture = swiperAdapter.getItem(currentPosition);
                    int imageResourceId = getResources().getIdentifier(currentPicture.getImageUrl(), "drawable", getPackageName());
                    imageName = currentPicture.getName();
                    shareImage(imageResourceId);
                }
            }
        });

    }

    private void shareImage(int resourceId) {
        Bitmap orignalebitmap = BitmapFactory.decodeResource(getResources(), resourceId);

        Bitmap bitmap = orignalebitmap.copy(orignalebitmap.getConfig(), true);
        Drawable appIcon = null;
        try {
            appIcon = getPackageManager().getApplicationIcon(getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        Bitmap iconBitmap;
        if (appIcon instanceof BitmapDrawable) {
            iconBitmap = ((BitmapDrawable) appIcon).getBitmap();
        } else {
            iconBitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(iconBitmap);
            appIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            appIcon.draw(canvas);
        }
        int desiredIconSize = 34;
        iconBitmap = Bitmap.createScaledBitmap(iconBitmap, desiredIconSize, desiredIconSize, true);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(iconBitmap);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        TextView textView = new TextView(this);
        textView.setText(getString(R.string.app_name));
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(8);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
        Bitmap linearLayoutBitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(linearLayoutBitmap);
        linearLayout.draw(canvas);

        canvas = new Canvas(bitmap);
        canvas.drawBitmap(linearLayoutBitmap, 0, bitmap.getHeight() - linearLayoutBitmap.getHeight() , null);



        File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to share image", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", imagePath);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
           saveImageToGallery();
        } else {
                ActivityCompat.requestPermissions(this, new String[]{STORAGE_PERMISSION,READ_PERMISSION}, REQUEST_CODE);
        }
    }

    private void saveImageToGallery() {
        String wallpaperDirectory = "WallpaperApp";

        int currentPosition = viewPager2.getCurrentItem();
        RecyclerView.Adapter adapter = viewPager2.getAdapter();
        if (adapter instanceof SwiperAdapter) {
            SwiperAdapter swiperAdapter = (SwiperAdapter) adapter;
            Pictures currentPicture = swiperAdapter.getItem(currentPosition);
            int imageResourceId = getResources().getIdentifier(currentPicture.getImageUrl(), "drawable", getPackageName());
            imageName = currentPicture.getName() + ".jpg";

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
            Bitmap bitmap = originalBitmap.copy(originalBitmap.getConfig(), true);

            // Get the app icon
            Drawable appIcon = null;
            try {
                appIcon = getPackageManager().getApplicationIcon(getPackageName());
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap iconBitmap;
            if (appIcon instanceof BitmapDrawable) {
                iconBitmap = ((BitmapDrawable) appIcon).getBitmap();
            } else {
                iconBitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(iconBitmap);
                appIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                appIcon.draw(canvas);
            }
            int desiredIconSize = 34;
            iconBitmap = Bitmap.createScaledBitmap(iconBitmap, desiredIconSize, desiredIconSize, true);

            int padding = 10;

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(iconBitmap);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            TextView textView = new TextView(this);
            textView.setText(getString(R.string.app_name));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(8);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
            Bitmap linearLayoutBitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(linearLayoutBitmap);
            linearLayout.draw(canvas);

            canvas = new Canvas(bitmap);
            canvas.drawBitmap(linearLayoutBitmap, padding, bitmap.getHeight() - linearLayoutBitmap.getHeight() - padding, null);


            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File wallpaperApp = new File(pictureDirectory, wallpaperDirectory);
            wallpaperApp.mkdirs();

            // Save the modified bitmap to a file
            File imageFile = new File(wallpaperApp, imageName);
            try {
                OutputStream outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                Toast.makeText(this, "Image Saved to Gallery", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    private void saveImageToGallery() {
//        String wallpaperDirectory = "WallpaperApp";
//
//        int currentPosition = viewPager2.getCurrentItem();
//        RecyclerView.Adapter adapter = viewPager2.getAdapter();
//        if (adapter instanceof SwiperAdapter) {
//            SwiperAdapter swiperAdapter = (SwiperAdapter) adapter;
//            Pictures currentPicture = swiperAdapter.getItem(currentPosition);
//            int imageResourceId = getResources().getIdentifier(currentPicture.getImageUrl(), "drawable", getPackageName());
//            imageName = currentPicture.getName() + ".jpg";
//
//            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
//            Bitmap bitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
//            Drawable appIcon = null;
//            try {
//                appIcon = getPackageManager().getApplicationIcon(getPackageName());
//            } catch (PackageManager.NameNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//            Bitmap iconBitmap;
//            if (appIcon instanceof BitmapDrawable) {
//                iconBitmap = ((BitmapDrawable) appIcon).getBitmap();
//            } else {
//                iconBitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(iconBitmap);
//                appIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//                appIcon.draw(canvas);
//            }
//
//            int desiredIconSize  = 34;
//
//            iconBitmap = Bitmap.createScaledBitmap(iconBitmap, desiredIconSize, desiredIconSize, true);
//
//            int padding = 10;
//            int iconX = padding;
//            int iconY = bitmap.getHeight() - iconBitmap.getHeight() - padding;
//
//            Canvas canvas = new Canvas(bitmap);
//
//            String appName = getString(R.string.app_name);
//
//            LinearLayout linearLayout = new LinearLayout(this);
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(iconX + iconBitmap.getWidth() + padding, iconY + iconBitmap.getHeight() / 2, 0, 0);
//            linearLayout.setLayoutParams(layoutParams);
//            ImageView imageView = new ImageView(this);
//            imageView.setImageBitmap(iconBitmap);
//            linearLayout.addView(imageView);
//
//            TextView textView = new TextView(this);
//            textView.setText(appName);
//            textView.setTextColor(Color.WHITE);
//            textView.setTextSize(10);
//            linearLayout.addView(textView);
//
//
//            linearLayout.measure(canvas.getWidth(), canvas.getHeight());
//            linearLayout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
//            linearLayout.draw(canvas);
//
//            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            File wallpaperApp = new File(pictureDirectory, wallpaperDirectory);
//            wallpaperApp.mkdirs();
//
//            // Save the modified bitmap to a file
//            File imageFile = new File(wallpaperApp, imageName);
//            try {
//                OutputStream outputStream = new FileOutputStream(imageFile);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                outputStream.flush();
//                outputStream.close();
//                Toast.makeText(this, "Image Saved to Gallery", Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }




//    private void saveImageToGallery() {
//        String wallPaperDirestory = "WallpaperApp";
//        int currentPosition = viewPager2.getCurrentItem();
//        RecyclerView.Adapter adapter = viewPager2.getAdapter();
//        if (adapter instanceof SwiperAdapter) {
//            SwiperAdapter swiperAdapter = (SwiperAdapter) adapter;
//            Pictures currentPicture = swiperAdapter.getItem(currentPosition);
//            int imageResourceId = getResources().getIdentifier(currentPicture.getImageUrl(), "drawable", getPackageName());
//            imageName = currentPicture.getName()+ ".jpg";
//
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
//
//            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            File wallpaperApp = new File(pictureDirectory,wallPaperDirestory);
//            wallpaperApp.mkdirs();
//
//
//            File imageFile = new File(wallpaperApp,imageName);
//
//            try {
//
//                OutputStream outputStream = new FileOutputStream(imageFile);
//                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//                outputStream.flush();
//                outputStream.close();
//                Toast.makeText(this, "Image Saved to Gallery", Toast.LENGTH_SHORT).show();
//            }catch (Exception e){
//                e.printStackTrace();
//                Toast.makeText(PreViewActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permssion Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission required to save picture", Toast.LENGTH_SHORT).show();
            }
        }else {
            checkStoragePermission();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}

//    private Bitmap viewToBitmap(View view)
//    {
//        Bitmap result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(result);
//        view.draw(canvas);
//
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setAntiAlias(true);
//        paint.setTextSize(14);
//        paint.setTextAlign(Paint.Align.RIGHT);
//        canvas.drawText("Hello Android!", view.getWidth(), 14, paint); // draw watermark at top right corner
//        return result;
//    }

//    private void saveDrawableImageToGallery(int resourceId) {
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
// saveImage(bitmap);
//        Glide.with(this)
//                .asBitmap()
//                .load(resourceId)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        String savedImagePath = saveImage(resource);
//                        if (savedImagePath != null) {
//                            Toast.makeText(PreViewActivity.this, "Image saved to " + savedImagePath, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(PreViewActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                        // Not implemented
//                    }
//                });
//    }

//    private String saveImage(Bitmap bitmap) {
//        try {
//            // Create a copy of the original bitmap to avoid modifying it directly
//            Bitmap copyBitmap = bitmap.copy(bitmap.getConfig(), true);
//
//            // Get the app icon
//            Drawable appIcon = getPackageManager().getApplicationIcon(getPackageName());
//            Bitmap iconBitmap = drawableToBitmap(appIcon);
//
//            // Calculate the position to draw the app icon (e.g., top left corner)
//            int padding = 20; // padding in pixels
//            int iconSize = 20; // size of the app icon in pixels
//            int x = padding;
//            int y = padding;
//
//            // Draw the app icon onto the copied bitmap
//            Canvas canvas = new Canvas(copyBitmap);
//            canvas.drawBitmap(iconBitmap, x, y, null);
//
//            // Get the app name
//            String appName = getString(R.string.app_name);
//
//            // Set the text properties
//            Paint paint = new Paint();
//            paint.setColor(Color.WHITE);
//            paint.setTextSize(40); // text size in pixels
//
//            // Calculate the position to draw the app name (e.g., bottom left corner)
//            x = padding;
//            y = copyBitmap.getHeight() - padding;
//
//            // Draw the app name onto the copied bitmap
//            canvas.drawText(appName, x, y, paint);
//
//            // Save the modified bitmap to the Pictures directory
//            File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            File wallpaperApp = new File(picturesDirectory, "WallpaperApp");
//            if (!wallpaperApp.exists()) {
//                wallpaperApp.mkdirs();
//            }
//            File imageFile = new File(wallpaperApp, imageName);
//
//            OutputStream outputStream = new FileOutputStream(imageFile);
//            copyBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            // Show toast message
//            Toast.makeText(PreViewActivity.this, "Image saved to Gallery", Toast.LENGTH_SHORT).show();
//
//            return imageFile.getAbsolutePath();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(PreViewActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }

//    private String saveImage(Bitmap bitmap) {
//
//        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File wallpaperApp = new File(picturesDirectory,"WallpaperApp");
//        // If the directory doesn't exist, create it
//        if (!wallpaperApp.exists()) {
//            wallpaperApp.mkdirs();
//        }
//        File imageFile = new File(wallpaperApp, imageName);
//
//        try {
//            OutputStream outputStream = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
////            addImageToGallery(imageFile.getAbsolutePath(),imageName);
//            Toast.makeText(PreViewActivity.this, "Image saved to Gallery" , Toast.LENGTH_SHORT).show();
//            return imageFile.getAbsolutePath();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(PreViewActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//    private void addImageToGallery(String imagePath,String name) {
//        try {
//            MediaStore.Images.Media.insertImage(getContentResolver(), imagePath,name, "Description");
//            Toast.makeText(this, "Image added to gallery", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to add image to gallery", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private Bitmap drawableToBitmap(Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        return bitmap;
//    }