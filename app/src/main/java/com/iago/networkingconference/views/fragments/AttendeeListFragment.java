package com.iago.networkingconference.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.iago.networkingconference.NetworkingConferenceApplication;
import com.iago.networkingconference.R;
import com.iago.networkingconference.database.DataFetchHelper;
import com.iago.networkingconference.models.Attendee;
import com.iago.networkingconference.models.CustomFields;
import com.iago.networkingconference.network.DataFetched;
import com.iago.networkingconference.views.SearchString;
import com.iago.networkingconference.views.listitems.PopupListItem;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.iago.networkingconference.services.UpdateAttendeeListService.DATA_FETCHED;

public class AttendeeListFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    public static final String ATTENDEE_ID = "ATTENDEE_ID";

    @Inject
    DataFetchHelper dataFetchHelper;

    @Inject
    EventBus eventBus;

    private String[] options = new String[] {
        "Name",
        "Position",
        "Company"
    };

    private ProgressBar progressBar;
    private ListView listView;
    private Spinner spinner;
    private int selectedSpinnerPosition = 0;
    private Set<PopupListItem> tagSet = new HashSet<>();
    private SearchString searchStr = new SearchString("");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_list, container, false);
        spinner = view.findViewById(R.id.sort_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.item_spinner, options);

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        listView = view.findViewById(R.id.attendee_list_view);
        listView.setOnItemClickListener(this);
        progressBar = view.findViewById(R.id.attendee_list_progress);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkingConferenceApplication.getComponent().inject(this);
    }

    private void fetchData(int orderBy) {
        String defaultOrderByCol = "fullName";
        String additionalOrderCol = null;
        switch (orderBy) {
            case 1: {
                additionalOrderCol = "position";
                break;
            }
            case 2: {
                additionalOrderCol = "company";
                break;
            }
        }

        dataFetchHelper
            .fetchByTags(additionalOrderCol, defaultOrderByCol,
                            searchStr.getSearchStr(), tagSet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<Attendee>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<Attendee> attendees) {
                    if (getContext() != null) {
                        Timber.d("data set size: %d", attendees.size());
                        listView.setAdapter(
                                new AttendeeAdapter(
                                        getContext(),
                                        R.layout.item_attendee_list,
                                        attendees
                                )
                        );
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e, "error fetching data");
                }

                @Override
                public void onComplete() {
                    enableUI(true);
                }
            });
    }

    private void fetchDataWithTags(int orderBy, Set<PopupListItem> tagSet) {
        String defaultOrderByCol = "fullName";
        String additionalOrderCol = null;
        switch (orderBy) {
            case 1: {
                additionalOrderCol = "position";
                break;
            }
            case 2: {
                additionalOrderCol = "company";
                break;
            }
        }

        dataFetchHelper
                .fetchByTags(additionalOrderCol, defaultOrderByCol,
                        searchStr.getSearchStr(), tagSet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Attendee>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Attendee> attendees) {
                        if (getContext() != null && attendees != null) {
                            Timber.d("data set size with tags: %d", attendees.size());
                            listView.setAdapter(
                                    new AttendeeAdapter(
                                            getContext(),
                                            R.layout.item_attendee_list,
                                            attendees
                                    )
                            );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "error fetching data");
                    }

                    @Override
                    public void onComplete() {
                        enableUI(true);
                    }
                });
    }

    private void enableUI(boolean enable) {
        listView.setVisibility(enable ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(enable ? View.GONE : View.VISIBLE);
        spinner.setEnabled(enable);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventBus.register(this);

        if (Hawk.contains(DATA_FETCHED)) {
            fetchData(0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        eventBus.unregister(this);
    }

    @Subscribe
    public void onDataFetched(DataFetched dataFetched) {
        fetchData(0);
    }

    @Subscribe
    public void onTagsSelected(Set<PopupListItem> tagSet) {
        this.tagSet = tagSet;
        enableUI(false);
        Timber.d("tagSet size: %d", tagSet.size());
        if (tagSet.isEmpty()) {
            fetchData(selectedSpinnerPosition);
        } else {
            fetchDataWithTags(selectedSpinnerPosition, tagSet);
        }
    }

    @Subscribe
    public void onSearchStringChanged(SearchString searchString) {
        if (!searchStr.equals(searchString)) {
            this.searchStr = searchString;
            enableUI(false);
            if (tagSet.isEmpty()) {
                fetchData(selectedSpinnerPosition);
            } else {
                fetchDataWithTags(selectedSpinnerPosition, tagSet);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (Hawk.contains(DATA_FETCHED)) {
            if (tagSet == null || tagSet.isEmpty()) {
                fetchData(position);
            } else {
                fetchDataWithTags(position, tagSet);
            }
            selectedSpinnerPosition = position;
            enableUI(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listView.getAdapter() != null) {
            Attendee attendee = (Attendee) listView.getAdapter().getItem(position);
            AttendeeDetailsFragment detailsFragment = new AttendeeDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ATTENDEE_ID, attendee.getId());
            detailsFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .addToBackStack("stack")
                    .add(R.id.fragment_container, detailsFragment)
                    .commitAllowingStateLoss();
        }
    }

    class AttendeeAdapter extends ArrayAdapter<Attendee> {

        private LayoutInflater inflater;

        AttendeeAdapter(@NonNull Context context, int resource, @NonNull List<Attendee> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_attendee_list, parent, false);
            }

            TextView nameView = convertView.findViewById(R.id.attendee_full_name);
            TextView positionView = convertView.findViewById(R.id.attendee_position);
            TextView companyView = convertView.findViewById(R.id.attendee_company);
            ImageView imageView = convertView.findViewById(R.id.attendee_profile_image);

            Attendee attendee = getItem(position);
            if (attendee.getCustomFields() != null) {
                CustomFields customFields = attendee.getCustomFields();
                nameView.setText(customFields.getFullName());
                positionView.setText(customFields.getPosition());
                companyView.setText("@" + customFields.getCompany());
            }

            if (attendee.getMedia() != null) {
                Picasso.get().load(attendee.getMedia().getTinyUrl()).into(imageView);
            }

            return convertView;
        }
    }
}
