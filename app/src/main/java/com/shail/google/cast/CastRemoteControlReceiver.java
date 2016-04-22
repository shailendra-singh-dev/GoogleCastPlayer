package com.shail.google.cast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class CastRemoteControlReceiver extends BroadcastReceiver {

  private static final String TAG = "CastRemoteControlReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
      KeyEvent keyEvent = (KeyEvent) intent.getExtras().
          get(Intent.EXTRA_KEY_EVENT);
      if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
        return;

      switch (keyEvent.getKeyCode()) {
        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
          Log.d(TAG, "Play/Pause was clicked");
          break;
        case KeyEvent.KEYCODE_MEDIA_NEXT:
          Log.d(TAG, "Next was clicked");
          break;
        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
          Log.d(TAG, "Previous was clicked");
          break;
        default:
          Log.d(TAG, keyEvent.getKeyCode() + " was clicked");
      }
    }
  }

}
