package com.shail.google.cast;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.cast.samples.democastplayer.R;

public class CastQueueRemoveItemsActivity extends BaseQueueActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.queue_update_items_activity,
                R.string.queue_remove_activity_title);

        mQueueAdapter = new CastQueueItemAdapter(this, android.R.layout.simple_spinner_item,
                mCastQueueListFragment, false, true, false);
        mQueueAdapter.populateAdapterWithIntent(getIntent());
        mCastQueueListFragment.setListAdapter(mQueueAdapter);
    }

    @Override
    protected void onOkButtonClick() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ITEM_IDS, mQueueAdapter.getCheckedItemIds());
        setResult(RESULT_OK, intent);
        finish();
    }
}
