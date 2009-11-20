/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_opengl_WindowsDirectInputDevice8 */

#ifndef _Included_org_lwjgl_opengl_WindowsDirectInputDevice8
#define _Included_org_lwjgl_opengl_WindowsDirectInputDevice8
#ifdef __cplusplus
extern "C" {
#endif
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_EXCLUSIVE
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_EXCLUSIVE 1L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_NONEXCLUSIVE
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_NONEXCLUSIVE 2L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_FOREGROUND
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_FOREGROUND 4L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_BACKGROUND
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_BACKGROUND 8L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_NOWINKEY
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DISCL_NOWINKEY 16L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_XAxis
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_XAxis 1L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_YAxis
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_YAxis 2L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_ZAxis
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_ZAxis 3L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Button
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Button 4L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Unknown
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Unknown 5L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DATA_SIZE
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DATA_SIZE 3L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_XAxis
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_XAxis 1L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_YAxis
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_YAxis 2L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_ZAxis
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_ZAxis 3L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Button
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Button 4L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Unknown
#define org_lwjgl_opengl_WindowsDirectInputDevice8_GUID_Unknown 5L
#undef org_lwjgl_opengl_WindowsDirectInputDevice8_DATA_SIZE
#define org_lwjgl_opengl_WindowsDirectInputDevice8_DATA_SIZE 3L
/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    setDataFormat
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_setDataFormat
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    setCooperativeLevel
 * Signature: (JJI)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_setCooperativeLevel
  (JNIEnv *, jobject, jlong, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    acquire
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_acquire
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    getDeviceState
 * Signature: (JLjava/nio/ByteBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_getDeviceState
  (JNIEnv *, jobject, jlong, jobject, jint, jint);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    getDeviceData
 * Signature: (JLjava/nio/ByteBuffer;ILjava/nio/IntBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_getDeviceData
  (JNIEnv *, jobject, jlong, jobject, jint, jobject, jint, jint);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    unacquire
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_unacquire
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    poll
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_poll
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    setBufferSize
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_setBufferSize
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    getEventSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_getEventSize
  (JNIEnv *, jobject);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    release
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_release
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_lwjgl_opengl_WindowsDirectInputDevice8
 * Method:    enumObjects
 * Signature: (JLorg/lwjgl/opengl/WindowsDirectInputDeviceObjectCallback;)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_WindowsDirectInputDevice8_enumObjects
  (JNIEnv *, jobject, jlong, jobject);

#ifdef __cplusplus
}
#endif
#endif