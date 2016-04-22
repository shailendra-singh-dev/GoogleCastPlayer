package com.shail.google.cast;

import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.samples.democastplayer.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CastQueueInsertItemsActivity extends BaseQueueActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.queue_insert_items_activity,
                R.string.queue_insert_activity_title);

        mQueueAdapter = new CastQueueItemAdapter(this, android.R.layout.simple_spinner_item,
                mCastQueueListFragment, true, false, true);
        mQueueAdapter.populateAdapterWithIntent(getIntent());
        mCastQueueListFragment.setListAdapter(mQueueAdapter);

        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCastQueueListFragment.onAddNewItemButtonClicked();
            }
        });
    }

    @Override
    protected void onOkButtonClick() {
        Intent intent = new Intent();
        MediaQueueItem selectedItem = mQueueAdapter.getSelectedItem();
        int insertBeforeItemId =
                (selectedItem != null) ? selectedItem.getItemId() : MediaQueueItem.INVALID_ITEM_ID;
        intent.putExtra(EXTRA_INSERT_BEFORE_ITEM_ID, insertBeforeItemId);
        MediaQueueItem[] insertedItems = mCastQueueListFragment.getInsertedItems();
        int count = insertedItems.length;
        String[] itemStrings = new String[count];
        for (int i = 0; i < count; ++i) {
            itemStrings[i] = insertedItems[i].toJson().toString();
        }
        intent.putExtra(EXTRA_ITEMS, itemStrings);
        setResult(RESULT_OK, intent);
        finish();
    }
}
