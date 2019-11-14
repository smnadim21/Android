public class Tools {

    
  
  public static void showSnackbar(Activity activity, final String mainTextStringId, final String actionStringId,
                                    View.OnClickListener listener) {
        Snackbar.make(
                activity.findViewById(android.R.id.content),
                mainTextStringId,
                Snackbar.LENGTH_SHORT)
                .setAction(actionStringId, listener).show();
    }
  
  
  
      public static void setKeepMeAlive(Context context, int mills) {
        Log.e("setKeepMeAlive", "set");
        setKeepMeAliveStatus(true);
        Intent alarmIntent = new Intent(context, KeepMeAliveReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, KEEP_ME_ALIVE_FLAG, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);/*
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);*/
        setLastKeepMeAliveTime(System.currentTimeMillis());
        manager.setRepeating(AlarmManager.RTC_WAKEUP, getLastKeepMeAliveTime(), mills, pendingIntent);
    }

    public static void cancelKeepMeAlive(Context context) {
        setKeepMeAliveStatus(false);
        Log.e("cancelcancelKeepMeAlive", "cancelled");
        Intent alarmIntent = new Intent(context, KeepMeAliveReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, KEEP_ME_ALIVE_FLAG, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }
  
  
    private static double meterDistanceBetweenPoints(double lat_a, double lng_a) {

        LoginData loginData = new Gson().fromJson(LocalData.getEXAMPLE(), LoginData.class);
        double lat_b = loginData.getLatitude();
        double lng_b = loginData.getLongitude();

        double r = loginData.getGeo_radious();

        float pk = (float) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }


    private static boolean insideChamber(Location location) {
        return meterDistanceBetweenPoints(location.getLatitude(), location.getLongitude()) <= getSession().getGeo_radious();
    }


    public static boolean isGooglePlayServicesAvailable(Activity context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            Dialog dialog = googleApiAvailability.getErrorDialog(context, resultCode, 0);
            if (dialog != null) {
                dialog.show();
            }
            return false;
        }
        return true;
    }

    public static void sendNotification(Context context, String msg, Integer id) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "");
        Intent resultIntent = new Intent(context, Home.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round));
        mBuilder.setContentTitle("");
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg));
        mBuilder.setContentText(msg);
        mBuilder.setSound(defaultSoundUri);
        mBuilder.setVibrate(new long[]{1000, 0, 1000, 0});
        mBuilder.setContentIntent(resultPendingIntent);
        // mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1000", "abc", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            //notificationChannel.setVibrationPattern(new long[]{1000,0, 1000,0});
            assert mNotificationManager != null;
            mBuilder.setChannelId("1000");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(id, mBuilder.build());

    }
  
   public static void getCurrentLocation(Context context) {
        final FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        final long time = 1000;
        fusedLocationProviderClient.requestLocationUpdates(new LocationRequest()
                        .setFastestInterval(time)
                        .setInterval(time)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                , new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // super.onLocationResult(locationResult);

                        if (locationResult != null) {
                            Location location = locationResult.getLastLocation();
                            Log.e("LOCATION Updt", location.toString());
                            Log.e("LOCATION diff", meterDistanceBetweenPoints((float) location.getLatitude(), (float) location.getLongitude()) + "");

                            fusedLocationProviderClient.removeLocationUpdates(this);
                        } else {
                            Log.e("LOCATION Updt", "no updates");
                        }

                        //mLastLocation = locationResult.getLastLocation();

                    }

                    @Override
                    public void onLocationAvailability(LocationAvailability locationAvailability) {
                        super.onLocationAvailability(locationAvailability);
                    }
                }, null);
    }
    
    
    
       public static boolean checkGPSStatus(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}
