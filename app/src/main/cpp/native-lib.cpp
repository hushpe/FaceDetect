#include <jni.h>
#include <string>
#include <iostream>

#include <android/log.h>

#include "UltraFace.hpp"
#include "opencv2/opencv.hpp"

#define TAG "Vision"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

std::shared_ptr<UltraFace> ult;

std::string getFilePath(JNIEnv *env, jstring filePath) {
    if (NULL == filePath) {
        return nullptr;
    }
    const char *file_path = env->GetStringUTFChars(filePath, 0);
    if (NULL == file_path) {
        return nullptr;
    }
    return file_path;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_visiontest_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject thiz,
        jstring mnn_path,
        jint resize_width,
        jint resize_height,
        jint num_thread,
        jboolean open_cl) {
    std::string hello = "Hello from C++";
    std::cout << "stringFromJNI: " << mnn_path << std::endl;
    LOGD("stringFromJNI :  %d, %d, %d, %d\n", resize_width, resize_height, num_thread, open_cl);

    const char *faceDetectionModelPath = env->GetStringUTFChars(mnn_path, 0);
    if (NULL == faceDetectionModelPath) {
        std::cout << "aaaaaaa " << std::endl;
    }

    std::string tFaceModelDir = faceDetectionModelPath;

    std::string model = tFaceModelDir + "/RFB-320.mnn";
    int width = 320;
    int height = 240;
    int num = 2;
    float score_threshold = 0.7;
    float iou_threshold = 0.45;

//    std::string mnnPath = getFilePath(env, mnn_path);

    ult = std::make_shared<UltraFace>();
    ult->init(model, width, height, num, score_threshold, iou_threshold);


    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_visiontest_MainActivity_VisionInit(
        JNIEnv* env,
        jobject thiz,
        jstring mnn_path,
        jint resize_width,
        jint resize_height,
        jint num_thread,
        jboolean open_cl) {
    const char *faceDetectionModelPath = env->GetStringUTFChars(mnn_path, 0);
    if (NULL == faceDetectionModelPath) {
        std::cout << "aaaaaaa " << std::endl;
    }

    std::string tFaceModelDir = faceDetectionModelPath;

    std::string model = tFaceModelDir + "/RFB-320.mnn";
    int width = 320;
    int height = 240;
    int num = 2;
    float score_threshold = 0.98;
    float iou_threshold = 0.3;

    ult = std::make_shared<UltraFace>();
    ult->init(model, width, height, num, score_threshold, iou_threshold);

    return true;
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_visiontest_MainActivity_VisionDetect(
        JNIEnv* env,
        jobject thiz,
        jstring mnn_path) {
    const char *faceDetectionModelPath = env->GetStringUTFChars(mnn_path, 0);
    if (NULL == faceDetectionModelPath) {
        std::cout << "aaaaaaa " << std::endl;
    }
std::cout << "tttttttt " << std::endl;
    std::string tFaceModelDir = faceDetectionModelPath;

    std::string img = tFaceModelDir + "/pose.png";
    std::string out_img = tFaceModelDir + "/out.png";

    cv::Mat mat = cv::imread(img);
    ult->detect(mat, out_img);
}