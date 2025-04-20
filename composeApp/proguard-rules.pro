-optimizationpasses 10
-flattenpackagehierarchy ''
-repackageclasses ''
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# Statically turn off all debugging facilities and assertions
-assumenosideeffects class kotlinx.coroutines.DebugKt {
    boolean getASSERTIONS_ENABLED();
    boolean getDEBUG();
    boolean getRECOVER_STACK_TRACES();
}

# We assume that Main Dispatcher is always present, good luck debugging this is prod
-assumenosideeffects class kotlinx.coroutines.internal.MainDispatchersKt {
    boolean SUPPORT_MISSING;
}

-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkExpressionValueIsNotNull(...);
    public static void checkNotNullExpressionValue(...);
    public static void checkParameterIsNotNull(...);
    public static void checkNotNullParameter(...);
    public static void checkFieldIsNotNull(...);
    public static void checkReturnedValueIsNotNull(...);
}