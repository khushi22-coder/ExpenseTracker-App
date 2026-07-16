package com.apexplanet.expensetracker.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.apexplanet.expensetracker.MainActivity;
import com.apexplanet.expensetracker.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "expense_channel";
    private static final String CHANNEL_NAME = "Expense Notifications";
    private static final int NOTIFICATION_ID = 1;

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    // Create notification channel (required for Android 8+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(
                    "Notifications for expense tracking"
            );

            NotificationManager manager =
                    context.getSystemService(
                            NotificationManager.class
                    );
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // Show notification when expense is added
    public void showExpenseAddedNotification(
            String title, double amount, String type) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        String emoji = type.equals("INCOME") ? "💰" : "💸";
        String message = emoji + " " + title +
                " - ₹" + String.format("%.2f", amount);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Transaction Added!")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        try {
            notificationManager.notify(
                    NOTIFICATION_ID, builder.build()
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    // Show daily reminder notification
    public void showDailyReminderNotification() {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Daily Reminder 📊")
                        .setContentText(
                                "Don't forget to track your expenses today!"
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        try {
            notificationManager.notify(2, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}