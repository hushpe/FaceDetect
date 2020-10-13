package com.example.vision;

public class Vision {
    static {
        System.loadLibrary("VisionLib");
    }

    public native boolean FaceInit(String mnnPath, int thread_num, float score_thd, boolean use_openCL);
    public native FaceInfo FaceDetect(byte[] image);

    public native boolean FaceCompareInit(String faceKeypointDetectMnnPath, String faceFeatureExtractMnnPath, int thread_num, boolean use_openCL);
    public native boolean FaceCompare(byte[] image1, byte[] image2, float thr);

    public native boolean FaceAttrInit(String mnnPath, int thread_num, boolean use_openCL);
    public native FaceAttrInfo FaceAttrRecog(byte[] image);
}


