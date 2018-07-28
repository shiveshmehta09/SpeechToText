package com.mehta.shivesh.speechtotext.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Shivesh
 */
public class PermissionsUtils {

    /**
     * Create an array from a given permissions.
     * @param permissions String array of permissions
     * @return permissions String array
     */
    public static String[] asArray(String[] permissions){
        if (permissions.length == 0) {
            throw new IllegalArgumentException("There is no given permission");
        }

        final String[] dest = new String[permissions.length];
        for (int i = 0, len = permissions.length; i < len; i++) {
            dest[i] = permissions[i];
        }
        return dest;
    }

    /**
     * Check that given permissions have been granted
     * @param grantResult
     * @return
     */
    public static boolean hasGranted(int grantResult) {
        return grantResult == PERMISSION_GRANTED;
    }

    /**
     * Check that all given permissions have been granted by verifying
     * that each entry in the given array is of the value
     *
     * @param grantResults
     * @return
     */
    public static boolean hasGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (!hasGranted(result)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the Context has access to a given permission.
     * Always returns true on platforms below M.
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        if (isMarshMellow()) {
            return permissionHasGranted(context, permission);
        }
        return true;
    }

    /**
     * Returns true if the Context has access to all given permissions.
     * Always returns true on platforms below M.
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasSelfPermissions(Context context, String[] permissions) {
        if (!isMarshMellow()) {
            return true;
        }

        for (String permission : permissions) {
            if (!permissionHasGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Requests permissions to be granted to this application.
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestAllPermissions(@NonNull Activity activity, @NonNull String[] permissions, int requestCode) {
        if (isMarshMellow()) {
            internalRequestPermissions(activity, permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void internalRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException("Given activity is null.");
        }
        activity.requestPermissions(permissions, requestCode);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private static boolean permissionHasGranted(Context context, String permission) {
        return hasGranted(context.checkSelfPermission(permission));
    }

    public static boolean isMarshMellow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
