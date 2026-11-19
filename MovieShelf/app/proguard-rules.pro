# Reguły ProGuard — na razie minimalne (debug nie używa minifikacji).
# Gson / Retrofit: zachowujemy pola modeli JSON przy ewentualnym release.
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
