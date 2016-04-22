package com.shail.google.cast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.samples.democastplayer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CastQueueListFragment extends ListFragment implements CastQueueItemAdapter.Listener {

    private static final String MEDIA_SELECTION_DIALOG_TAG = "media_selection";
    private static final int REQUEST_CODE_EDIT_ITEM = 1;

    private MediaCastSelectionListDialog mMediaSelectionDialog;
    private List<MediaQueueItem> mInsertedItems;

    @Override
     public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInsertedItems = new ArrayList<>();
        addQueueHeader();
    }

    @Override
    public void onSelectedItemChanged(int position) {
        int headerCount = getListView().getHeaderViewsCount();
        int footerCount = getListView().getFooterViewsCount();
        int count = getListView().getChildCount();
        int targetPosition = headerCount + position;

        for (int i = headerCount; i < count - footerCount; ++i) {
            View v = getListView().getChildAt(i);
            RadioButton button = (RadioButton) v.findViewById(R.id.radio_button);
            button.setChecked(targetPosition == i);
        }
    }

    @Override
    public void onItemClicked(int position) {
        MediaQueueItem item = ((CastQueueItemAdapter) getListAdapter()).getItem(position);

        Intent intent = new Intent(getActivity(), EditItemsQueueActivity.class);
        intent.putExtra(EditItemsQueueActivity.EXTRA_ITEM, item.toJson().toString());
        intent.putExtra(EditItemsQueueActivity.EXTRA_POSITION, position);

        startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
    }

    public MediaQueueItem[] getInsertedItems() {
        return mInsertedItems.toArray(new MediaQueueItem[mInsertedItems.size()]);
    }

    private void addQueueHeader() {
        TextView header = new TextView(getActivity());
        header.setText(R.string.media_queue_title);
        header.setPadding(0, 50, 0, 20);
        header.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        header.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        getListView().addHeaderView(header);
    }

    public void onAddNewItemButtonClicked() {
        if (mMediaSelectionDialog == null) {
            mMediaSelectionDialog = new MediaCastSelectionListDialog(getActivity()) {
                @Override
                protected void onItemSelected(final MediaInfo item) {
                    onItemAdded(item);
                }
            };
        }
        mMediaSelectionDialog.show(getFragmentManager(), MEDIA_SELECTION_DIALOG_TAG);
    }

    private void onItemAdded(MediaInfo item) {
        MediaQueueItem queueItem = new MediaQueueItem.Builder(item).build();
        CastQueueItemAdapter castQueueItemAdapter = ((CastQueueItemAdapter) getListAdapter());
        int insertBeforeItemId = MediaQueueItem.INVALID_ITEM_ID;
        int selectedPosition = castQueueItemAdapter.getSelectedPosition();
        if (selectedPosition >= 0 && selectedPosition < castQueueItemAdapter.getCount()) {
            insertBeforeItemId = castQueueItemAdapter.getItem(selectedPosition).getItemId();
        }
        if (insertBeforeItemId == MediaQueueItem.INVALID_ITEM_ID) {
            castQueueItemAdapter.add(queueItem);
        } else {
            int indexToInsert = -1;
            for (int i = 0; i < castQueueItemAdapter.getCount(); i++) {
                if (castQueueItemAdapter.getItem(i).getItemId() == insertBeforeItemId) {
                    indexToInsert = i;
                    break;
                }
            }
            if (indexToInsert != -1) {
                castQueueItemAdapter.insert(queueItem, indexToInsert);
            }
            castQueueItemAdapter.setSelectedPosition(selectedPosition + 1);
        }
        castQueueItemAdapter.notifyDataSetChanged();
        mInsertedItems.add(queueItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_EDIT_ITEM) {
            int position = data.getIntExtra(EditItemsQueueActivity.EXTRA_POSITION, -1);
            if (position < 0) {
                return;
            }
            String itemString = data.getStringExtra(EditItemsQueueActivity.EXTRA_ITEM);
            MediaQueueItem newItem;
            try {
                newItem = new MediaQueueItem.Builder(new JSONObject(itemString)).build();
            } catch (JSONException e) {
                return;
            }
            MediaQueueItem oldItem = ((CastQueueItemAdapter) getListAdapter()).getItem(position);
            ((CastQueueItemAdapter) getListAdapter()).remove(oldItem);
            ((CastQueueItemAdapter) getListAdapter()).insert(newItem, position);
        }
    }
}
