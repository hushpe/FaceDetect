package com.example.facedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.vision.FaceInfo;
import com.example.vision.Vision;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // 拷贝模型与图片到sdcard
            copyBigDataToSD("pose.png");
            copyBigDataToSD("VFDet.mnn");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File sdDir          = Environment.getExternalStorageDirectory();
        String sdPath       = sdDir.toString() + "/facesdk/";
        int thd_num         = 2;
        float score_thd     = 0.7f;
        boolean use_openCL  = false;


        try {
            //读取图片
            byte[] bytes;
            InputStream fin = new FileInputStream(new File(sdPath + "pose.png"));
            bytes = new byte[fin.available()];
            fin.read(bytes);
            fin.close();

            //face detect
            String faceModel = sdPath + "VFDet.mnn";                            // 模型路径
            Vision ob = new Vision();
            ob.FaceInit(faceModel, thd_num, score_thd, use_openCL);         // 初始化
            FaceInfo fi = ob.FaceDetect(bytes);                             // 调用, 成功后在sdcard的facesdk路径下查看人脸信息保存的调试图片，out.png

            // 输出FaceInfo信息
            Log.i("x1:", String.valueOf(fi.x1));
            Log.i("y1:", String.valueOf(fi.y1));
            Log.i("x2:", String.valueOf(fi.y2));
            Log.i("y2:", String.valueOf(fi.y2));
            Log.i("score:", String.valueOf(fi.score));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
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
}