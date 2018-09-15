package com.iago.networkingconference.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iago.networkingconference.NetworkingConferenceApplication;
import com.iago.networkingconference.R;
import com.iago.networkingconference.database.DataFetchHelper;
import com.iago.networkingconference.models.Attendee;
import com.iago.networkingconference.models.CustomFields;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.iago.networkingconference.views.fragments.AttendeeListFragment.ATTENDEE_ID;

public class AttendeeDetailsFragment extends Fragment {

    @Inject
    DataFetchHelper dataFetchHelper;

    private TextView countryView;
    private TextView cityView;
    private TextView ageView;
    private TextView companyView;
    private TextView positionView;
    private TextView firstNameView;
    private TextView lastNameView;
    private TextView phoneView;
    private TextView emailView;
    private ImageView profileImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkingConferenceApplication.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_detail, container, false);

        firstNameView = view.findViewById(R.id.attendee_first_name);
        lastNameView = view.findViewById(R.id.attendee_last_name);
        positionView = view.findViewById(R.id.attendee_position);
        companyView = view.findViewById(R.id.attendee_company);
        profileImageView = view.findViewById(R.id.attendee_profile_image);
        ageView = view.findViewById(R.id.attendee_age);
        cityView = view.findViewById(R.id.attendee_city);
        countryView = view.findViewById(R.id.attendee_country);
        phoneView = view.findViewById(R.id.attendee_phone);
        emailView = view.findViewById(R.id.attendee_email);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            String attendeeId = bundle.getString(ATTENDEE_ID);
            dataFetchHelper
                    .fetchAttendeeDetails(attendeeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Attendee>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Attendee attendee) {
                            if (attendee.getCustomFields() != null) {
                                CustomFields customFields = attendee.getCustomFields();
                                firstNameView.setText(customFields.getFirstName());
                                lastNameView.setText(customFields.getLastName());
                                positionView.setText(customFields.getPosition());
                                companyView.setText(customFields.getCompany());
                                ageView.setText(String.valueOf(customFields.getAge()));
                                cityView.setText(customFields.getCity());
                                countryView.setText(customFields.getCountryCode());
                                phoneView.setText(customFields.getPhone());
                                emailView.setText(customFields.getEmail());
                            }

                            if (attendee.getMedia() != null) {
                                Picasso.get().load(attendee.getMedia()
                                        .getTinyUrl()).into(profileImageView);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
