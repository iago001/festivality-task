package com.iago.networkingconference.views.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.iago.networkingconference.R;
import com.iago.networkingconference.views.listitems.PopupListHeader;
import com.iago.networkingconference.views.listitems.PopupListItem;

public class PopupListHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView labelView;

    public PopupListHeaderViewHolder(View itemView) {
        super(itemView);

        labelView = itemView.findViewById(R.id.item_popup_label);
    }

    public void bindData(PopupListHeader header) {
        int resource = 0;
        switch (header.getLabel()) {
            case "attendeeLookingFor" : {
                resource = R.string.attendee_looking_for;
                break;
            }
            case "attendeeProviding" : {
                resource = R.string.attendee_providing;
                break;
            }
            case "attendeeType" : {
                resource = R.string.attendee_type;
                break;
            }
            case "positionType" : {
                resource = R.string.position_type;
                break;
            }
            case "industryTags" : {
                resource = R.string.industry_tags;
                break;
            }
            case "industryComplimentaryTags" : {
                resource = R.string.industry_comp_tags;
                break;
            }
            default: {
                labelView.setText(header.getLabel());
                return;
            }
        }
        labelView.setText(resource);
    }
}
