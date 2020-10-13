package com.example.facedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.vision.FaceAttrInfo;
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
            copyBigDataToSD("demo.png");
            copyBigDataToSD("VFDet.mnn");
            copyBigDataToSD("VFKeypoint.mnn");
            copyBigDataToSD("VFRecog.mnn");
            copyBigDataToSD("VFAttribute.mnn");
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

            Vision ob = new Vision();

            //face detect
            String faceModel = sdPath + "VFDet.mnn";                        // 模型路径
            ob.FaceInit(faceModel, thd_num, score_thd, use_openCL);         // 初始化
            FaceInfo fi = ob.FaceDetect(bytes);                             // 调用

            // 输出FaceInfo信息
            Log.i("x1:", String.valueOf(fi.x1));                        //左上x坐标值
            Log.i("y1:", String.valueOf(fi.y1));                        //左上y坐标值
            Log.i("x2:", String.valueOf(fi.y2));                        //右下x坐标值
            Log.i("y2:", String.valueOf(fi.y2));                        //右下y坐标值
            Log.i("score:", String.valueOf(fi.score));                  //人脸得分


            byte[] face;
            InputStream fin_f = new FileInputStream(new File(sdPath + "demo.png"));
            face = new byte[fin_f.available()];
            fin_f.read(face);
            fin_f.close();

            //face compare
            String keypointModel = "/storage/emulated/0/facesdk/VFKeypoint.mnn";
            String recogModel = "/storage/emulated/0/facesdk/VFRecog.mnn";
            ob.FaceCompareInit(keypointModel, recogModel, thd_num, use_openCL);      //初始化
            boolean match = ob.FaceCompare(face, face, 0.8f);                   // 调用
            Log.i("match:", String.valueOf(match));                             // true 表示比对成功

            //face att
            String attModel = "/storage/emulated/0/facesdk/VFAttribute.mnn";
            ob.FaceAttrInit(attModel, thd_num, use_openCL);                          // 初始化
            FaceAttrInfo fa = ob.FaceAttrRecog(face);                                // 调用
            Log.i("no_eye_covered:", String.valueOf(fa.no_eye_covered));        // 眼没遮挡
            Log.i("left_eye_covered:", String.valueOf(fa.left_eye_covered));    // 左眼遮挡
            Log.i("right_eye_covered:", String.valueOf(fa.right_eye_covered));  // 右眼遮挡
            Log.i("eye_squinted:", String.valueOf(fa.eye_squinted));            // 是否眯眼
            Log.i("glasses_weared:", String.valueOf(fa.glasses_weared));        // 是否戴眼镜
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