package br.com.softctrl.contact.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by timoshenko on 2/19/16.
 * E-Mail carlos.lopes@pdcase.com.br
 */
public final class PermissionUtil {

    /**
     * @param activity
     * @param permission
     * @return
     */
    public static final synchronized boolean isPermissonGranted(final Context activity, final String permission) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity,
                permission));
    }
}
