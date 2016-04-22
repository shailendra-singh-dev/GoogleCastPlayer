package com.shail.google.cast;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.samples.democastplayer.R;

public class CastQueueUpdateItemsActivity extends BaseQueueActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.queue_update_items_activity,
                R.string.queue_update_activity_title);

        mQueueAdapter = new CastQueueItemAdapter(this, android.R.layout.simple_spinner_item,
                mCastQueueListFragment, false, false, true);
        mQueueAdapter.populateAdapterWithIntent(getIntent());
        mCastQueueListFragment.setListAdapter(mQueueAdapter);
    }

    @Override
    protected void onOkButtonClick() {
        int count = mQueueAdapter.getCount();
        if (count <= 0) {
            finish();
            return;
        }
        String[] itemStrings = new String[count];
        for (int i = 0; i < count; ++i) {
            MediaQueueItem item = mQueueAdapter.getItem(i);
            itemStrings[i] = item.toJson().toString();
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ITEMS, itemStrings);
        setResult(RESULT_OK, intent);
        finish();
    }
}
