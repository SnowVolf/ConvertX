-keepattributes SourceFile
-dontwarn java.util.**
-dontwarn java.time.**
-dontwarn javax.**
-dontwarn com.squareup.**
-dontwarn rx.**
-dontwarn com.sun.tools.**
-dontwarn com.sun.misc.**
-dontwarn com.sun.source.**
-dontwarn com.squareup.**
-dontwarn com.google.auto.**
-dontwarn com.google.common.**
-dontwarn com.google.j2objc.**

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
   static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
   static **$* *;
}
-keepclassmembers class <2>$<3> {
   kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
   public static ** INSTANCE;
}
-keepclassmembers class <1> {
   public static <1> INSTANCE;
   kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault