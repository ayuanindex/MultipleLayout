package com.ayuan.demo;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class App extends Application {
    private static Toast toast;
    private static ProgressDialog progressDialog;
    private static Context context;

    public interface ChuLi {
        void jump();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    public static Context getContext() {
        return context;
    }

    public static void showToast(String mes) {
        toast.setText(mes);
        toast.show();
    }

    public static void showDialog(FragmentActivity activity, String msg) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("提示");
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void disDialog(final FragmentActivity activity, final ChuLi chuLi) {
        if (progressDialog != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(800);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chuLi.jump();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }
}
