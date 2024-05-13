# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.bextdev.jasper.Jasper {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/bextdev/jasper/repack'
-flattenpackagehierarchy
-dontpreverify
