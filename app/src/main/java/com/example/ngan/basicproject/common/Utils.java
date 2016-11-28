package com.example.ngan.basicproject.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public class Utils {

    private static final String REGEX_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL, Pattern.CASE_INSENSITIVE);


    /**
     * Write byte[] to File
     *
     * @param data     bytes
     * @param filePath String
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void writeBytesIntoFile(byte[] data, String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();
    }

    /**
     * Write Bitmap into File
     *
     * @param bm       Bitmap
     * @param filePath String
     */
    public static void writeBitmapIntoFile(Bitmap bm, String filePath) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
        // PNG is a lossless format, the compression factor (100) is ignored
        out.close();
    }

    /**
     * Close keyboard when tap outside
     *
     * @param context context
     * @param view    view
     */
    public static void setupKeyboard(final Context context, final View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager)
                            context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int i = 0; i < group.getChildCount(); ++i) {
                setupKeyboard(context, group.getChildAt(i));
            }
        }
    }

    /**
     * Check email syntax
     *
     * @param email String
     * @return TRUE if correct
     */
    public static boolean checkEmailSyntax(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && PATTERN_EMAIL.matcher(email).matches();
    }


    /**
     * Create Animation
     *
     * @param v View
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void animateWobble(View v) {
        ObjectAnimator animator = createBaseWobble(v);
        animator.setFloatValues(-10, 10);
        animator.start();
    }

    /**
     * Create Animation
     *
     * @param v View
     * @return ObjectAnimator
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static ObjectAnimator createBaseWobble(final View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        ObjectAnimator animator = new ObjectAnimator();
        animator.setDuration(100);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(5);
        animator.setPropertyName("rotation");
        animator.setTarget(v);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setLayerType(View.LAYER_TYPE_NONE, null);
                v.setRotation(0);
            }
        });
        return animator;
    }

    /**
     * Align Center Text View
     *
     * @param view View
     */
    public static void alignCenterTextView(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setGravity(Gravity.CENTER);

            ViewGroup.LayoutParams params = textView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            textView.setLayoutParams(params);

            View parent = (View) textView.getParent();
            if (parent instanceof LinearLayout) {
                ((LinearLayout) parent).setGravity(Gravity.CENTER);
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;

            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                alignCenterTextView(viewGroup.getChildAt(i));
            }
        }
    }

    public static Dialog showInfoDialog(Context context,
                                        @StringRes int title, @StringRes int message,
                                        @StringRes int btnLabel, DialogInterface.OnClickListener btnListener,
                                        DialogInterface.OnShowListener onShowListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != 0) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setNegativeButton(btnLabel, btnListener);
        builder.setCancelable(false);

        Dialog dialog = builder.create();
        if (title == 0) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setOnShowListener(onShowListener);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;
    }

    public static Dialog showConfirmDialog(Context context,
                                           @StringRes int title, @StringRes int message,
                                           @StringRes int leftLabel, DialogInterface.OnClickListener leftListener,
                                           @StringRes int rightLabel, DialogInterface.OnClickListener rightListener,
                                           DialogInterface.OnShowListener onShowListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != 0) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setNegativeButton(leftLabel, leftListener);
        builder.setPositiveButton(rightLabel, rightListener);
        builder.setCancelable(false);

        Dialog dialog = builder.create();
        if (title == 0) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setOnShowListener(onShowListener);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;
    }

    public static Dialog showConfirmDialog(Context context,
                                           @StringRes int title, SpannableString message,
                                           @StringRes int leftLabel, DialogInterface.OnClickListener leftListener,
                                           @StringRes int rightLabel, DialogInterface.OnClickListener rightListener,
                                           DialogInterface.OnShowListener onShowListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != 0) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setNegativeButton(leftLabel, leftListener);
        builder.setPositiveButton(rightLabel, rightListener);
        builder.setCancelable(false);

        Dialog dialog = builder.create();
        if (title == 0) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setOnShowListener(onShowListener);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;
    }

    public static Dialog showProgressDialog(Context context,
                                            @StringRes int title,
                                            @StringRes int message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(context.getString(message));
        dialog.show();

        return dialog;
    }

    public static Dialog showProgressDialog(Context context,
                                            @StringRes int message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(message));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        return dialog;
    }

    /**
     * Show Toast Message
     *
     * @param id int
     */
    public static void showMessage(@StringRes int id, Context context) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }


    /**
     * disable back Button
     *
     * @param rootView View
     */
    public static void handlerBackButton(View rootView) {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * animate wrong input
     *
     * @param mImg ImageView
     */
    public static void notifyInput(ImageView mImg) {
        //  mImg.setImageResource(R.drawable.bg_sign_icon_email_error);
        Utils.animateWobble(mImg);
    }

    public static void addFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag, AppCompatActivity mActivity) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    public static void replaceFragment(@IdRes int containerViewId,
                                       @NonNull Fragment fragment,
                                       String fragmentTag,
                                       @Nullable String backStackStateName, AppCompatActivity mActivity) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                // .addToBackStack(backStackStateName)
                .commit();
    }
}
