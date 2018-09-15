package com.iago.networkingconference.views.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.iago.networkingconference.R;
import com.iago.networkingconference.views.listitems.PopupListHeader;
import com.iago.networkingconference.views.listitems.PopupListItem;

public class PopupListItemViewHolder extends PopupListHeaderViewHolder {

    private CheckBox checkBox;

    public PopupListItemViewHolder(View itemView, CompoundButton.OnCheckedChangeListener listener) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.item_popup_tags_checkbox);
        checkBox.setOnCheckedChangeListener(listener);
    }

    public void bindData(PopupListHeader header) {
        super.bindData(header);
        PopupListItem listItem = (PopupListItem) header;
        checkBox.setTag(listItem);
        checkBox.setChecked(listItem.isChecked());
    }
}
