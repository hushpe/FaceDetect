step 1. 添加libs   库文件; 添加so对应的cpp转java接口, 在app/src/main/java/com/example/vision中
step 2. 添加assets 资源文件
step 3. 在 AndroidManifest.xml 中添加sd卡权限, 同时运行时确保在运行机上开启sd卡权限
step 4. 在 build.gradle 中 添加编译过滤, 以及 libs 库路径
        ndk {
            abiFilters 'arm64-v8a', 'armeabi-v7a'
        }
        sourceSets {
            main {
                jniLibs.srcDirs = ['libs']
            }
        }
step 5. 调用方法在 app/src/main/java/com/example/facedetect/MainActivity.java 中 38～89行