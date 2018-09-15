package com.iago.networkingconference.di;

import com.iago.networkingconference.UtilModule;
import com.iago.networkingconference.database.DatabaseModule;
import com.iago.networkingconference.network.NetworkModule;
import com.iago.networkingconference.services.UpdateAttendeeListService;
import com.iago.networkingconference.views.activities.MainActivity;
import com.iago.networkingconference.views.fragments.AttendeeDetailsFragment;
import com.iago.networkingconference.views.fragments.AttendeeListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {UtilModule.class, NetworkModule.class, DatabaseModule.class})
public interface NetworkingConferenceComponent {

    void inject(MainActivity mainActivity);

    void inject(UpdateAttendeeListService service);

    void inject(AttendeeListFragment attendeeListFragment);

    void inject(AttendeeDetailsFragment attendeeDetailsFragment);
}
