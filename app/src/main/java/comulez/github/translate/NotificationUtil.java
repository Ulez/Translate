package comulez.github.translate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public class NotificationUtil {

    private static int NOTIFY_ID = 11;

    public static void showNormalNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText("点击打开翻译");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setColor(Color.rgb(121, 85, 72));
        builder.setPriority(Notification.PRIORITY_MIN);
        builder.setOngoing(true);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contextIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(contextIntent);
        long[] vibrate = {0, 50, 0, 0};
        builder.setVibrate(vibrate);
        Notification notification = builder.build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFY_ID, notification);
    }

    //弹出Notification
    private void showNotification(String message, Class<?> cls, Context context) {
        Notification mNotification;
        Intent i = new Intent(context, cls);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);
        mNotification = builder.build();
        mNotification.tickerText = message;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    private static String TB_ALERT = "tb_alert";
    protected CompositeSubscription mCompositeSubscription;

    public void addSubscription(Subscription s) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(s);
    }
}
