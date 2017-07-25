package comulez.github.translate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ResultReceiver extends BroadcastReceiver {
    public ResultReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.getStringExtra(Constant.loading);

    }
}
