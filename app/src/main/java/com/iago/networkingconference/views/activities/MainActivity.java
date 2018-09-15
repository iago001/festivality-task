package com.iago.networkingconference.views.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import com.iago.networkingconference.NetworkingConferenceApplication;
import com.iago.networkingconference.R;
import com.iago.networkingconference.database.DataFetchHelper;
import com.iago.networkingconference.network.DataFetched;
import com.iago.networkingconference.views.SearchString;
import com.iago.networkingconference.views.fragments.AttendeeDetailsFragment;
import com.iago.networkingconference.views.fragments.AttendeeListFragment;
import com.iago.networkingconference.views.listitems.PopupListHeader;
import com.iago.networkingconference.views.listitems.PopupListItem;
import com.iago.networkingconference.views.viewholders.PopupListHeaderViewHolder;
import com.iago.networkingconference.views.viewholders.PopupListItemViewHolder;
import com.orhanobut.hawk.Hawk;

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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.iago.networkingconference.services.UpdateAttendeeListService.DATA_FETCHED;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @Inject
    DataFetchHelper dataFetchHelper;

    @Inject
    EventBus eventBus;

    private List<PopupListHeader> tagsList;
    private Set<PopupListItem> tagSelectedSet = new HashSet<>();
    private PopupListAdapter popupListAdapter;
    private PopupWindow popupWindow;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkingConferenceApplication.getComponent().inject(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new AttendeeListFragment())
                .commitAllowingStateLoss();

        eventBus.register(this);

        if (Hawk.contains(DATA_FETCHED)) {
            fetchTagsData();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventBus.post(new SearchString(newText));
                return false;
            }
        });
        searchViewAndroidActionBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                eventBus.post(new SearchString(""));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        eventBus.unregister(this);
    }

    @Subscribe
    public void onDataFetch(DataFetched dataFetched) {
        fetchTagsData();
    }

    private void fetchTagsData() {
        dataFetchHelper
                .fetchTagsForFilter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PopupListHeader>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<PopupListHeader> tagsList) {
                        MainActivity.this.tagsList = tagsList;

                        popupListAdapter = new PopupListAdapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "error fetching tags");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.action_filter == item.getItemId()) {

            if (popupWindow == null || !popupWindow.isShowing()) {
                View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
                RecyclerView recyclerView = view.findViewById(R.id.popup_recycler_view);
                recyclerView.setAdapter(popupListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                popupWindow = new PopupWindow(view, WRAP_CONTENT, WRAP_CONTENT, false);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }

                        return false;
                    }
                });
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(findViewById(R.id.action_filter), 0, 0, Gravity.TOP);
            } else {
                popupWindow.dismiss();
            }
            return true;
        } else if (android.R.id.home == item.getItemId()) {
            getSupportFragmentManager().popBackStack();
            resetActionBar();
        }

        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PopupListItem listItem = (PopupListItem) buttonView.getTag();
        listItem.setChecked(isChecked);
        if (isChecked) {
            tagSelectedSet.add(listItem);
        } else {
            tagSelectedSet.remove(listItem);
        }
        eventBus.post(tagSelectedSet);
    }

    class PopupListAdapter extends RecyclerView.Adapter<PopupListHeaderViewHolder> {

        PopupListAdapter() {
        }

        @Override
        public int getItemViewType(int position) {
            return tagsList.get(position).getViewType();
        }

        @NonNull
        @Override
        public PopupListHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = getLayoutInflater().inflate(R.layout.item_popup_header, parent, false);
                return new PopupListHeaderViewHolder(view);
            } else {
                View view = getLayoutInflater().inflate(R.layout.item_popup_tags, parent, false);
                return new PopupListItemViewHolder(view, MainActivity.this);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull PopupListHeaderViewHolder holder, int position) {
            holder.bindData(tagsList.get(position));
        }

        @Override
        public int getItemCount() {
            return tagsList.size();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (menu != null){
            if (fragment instanceof AttendeeDetailsFragment) {
                menu.setGroupVisible(R.id.main_menu_group, false);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        resetActionBar();
    }

    private void resetActionBar() {
        if (menu != null && !isFinishing()) {
            menu.setGroupVisible(R.id.main_menu_group, true);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }
}
