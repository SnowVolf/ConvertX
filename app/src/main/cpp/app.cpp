// Write C++ code here.

#include <jni.h>
#include <string>
#include <iostream>
#include <sstream>
#include <vector>
#include <algorithm>
#include <iterator>
#include <cctype>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <openssl/bio.h>
#include <openssl/evp.h>
#include <openssl/buffer.h>

using namespace std;

extern "C" {

JNIEXPORT jstring JNICALL
Java_ru_svolf_convertx_utils_algorhitms_Decoder_encodeBase64(JNIEnv *env, jobject instance, jstring input_) {
    const char *input = env->GetStringUTFChars(input_, 0);

    BIO *bio, *b64;
    BUF_MEM *bufferPtr;

    b64 = BIO_new(BIO_f_base64());
    bio = BIO_new(BIO_s_mem());
    bio = BIO_push(b64, bio);

    BIO_set_flags(bio, BIO_FLAGS_BASE64_NO_NL);
    BIO_write(bio, input, strlen(input));
    BIO_flush(bio);
    BIO_get_mem_ptr(bio, &bufferPtr);
    BIO_set_close(bio, BIO_NOCLOSE);
    BIO_free_all(bio);

    jstring result = env->NewStringUTF(bufferPtr->data);

    env->ReleaseStringUTFChars(input_, input);

    return result;
}

}