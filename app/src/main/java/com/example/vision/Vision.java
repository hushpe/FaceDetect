package com.example.vision;

public class Vision {
    static {
        System.loadLibrary("VisionLib");
    }

    public native void stringFromJNI();

    public native boolean FaceInit(String mnnPath, int thread_num, float score_thd, boolean use_openCL);
    public native FaceInfo FaceDetect(byte[] byte_arr);
}


