package com.example.visiontest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);

//         String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mnn";
//         String modelPath = copyModel2SD(this, "ultra", path);
// //        String mnnPath = modelPath + File.separator + "RFB-320.mnn";
//         String mnnPath = modelPath + File.separator + "pose.png";
//         String ttt = modelPath + File.separator + "pose.jpg";
        try {
            copyBigDataToSD("pose.png");
            copyBigDataToSD("RFB-320.mnn");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File sdDir = Environment.getExternalStorageDirectory();//get model store dir
        String sdPath = sdDir.toString() + "/facesdk/";

//        Log.i("path", path);
//        Log.i("modelPath", modelPath);
//        Log.i("mnnPath", mnnPath);
        int width = 320;
        int height = 240;
        int thread_num = 2;
        boolean use_openCL = false;

        stringFromJNI(sdPath, width, height, thread_num, false);
        VisionDetect(sdPath);
        tv.setText("xxxxxxx");
    }

    private void copyBigDataToSD(String strOutFileName)  throws IOException{
        Log.i("start copy file ", strOutFileName);
        File sdDir = Environment.getExternalStorageDirectory();
        String newPath = sdDir.toString()+ File.separator  + "facesdk";
        File file = new File(newPath);
        file.mkdirs();

        String tmpFile = newPath + File.separator + strOutFileName;
        File aaa = new File(tmpFile);
        FileOutputStream myOutput = new FileOutputStream(aaa);

        InputStream myInput;
        myInput = this.getAssets().open(strOutFileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
        Log.i("end copy file ", strOutFileName);
    }

    private String copyModel2SD(Context mContext, String model, String path) {
        String modelPath = path + File.separator + model;
        File file = new File(modelPath);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            copyAssets(mContext, model, modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelPath;
    }

    public void copyAssets(Context mContext, String oldPath, String newPath) throws IOException {
        String fileNames[] = mContext.getAssets().list(oldPath);
        if (fileNames.length > 0) {
            File file = new File(newPath);
            file.mkdirs();
            for (String fileName : fileNames) {
                copyAssets(mContext, oldPath + File.separator + fileName, newPath + File.separator + fileName);
            }
        } else {
            InputStream is = mContext.getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String mnnPath, int width, int height, int thread_num, boolean use_openCL);
    public native boolean VisionInit(String mnnPath, int width, int height, int thread_num, boolean use_openCL);
    public native void VisionDetect(String mnnPath);
}
