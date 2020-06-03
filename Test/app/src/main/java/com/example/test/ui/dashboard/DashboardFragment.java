package com.example.test.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.LoggedActivity;
import com.example.test.LoginActivity;
import com.example.test.R;
import com.example.test.SessionManager;

import java.util.HashMap;

public class DashboardFragment extends Fragment {


    private DashboardViewModel dashboardViewModel;

    TextView textView9;

    TextView textView12;

    Button button2;

    SessionManager session;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }

        });

        textView9 = (TextView) root.findViewById(R.id.textView9);
        textView12 = (TextView) root.findViewById(R.id.textView12);

        final LoggedActivity activity = (LoggedActivity)getActivity();

        textView9.setText(activity.getUsername());

        textView12.setText(activity.getEmail());

        button2 = (Button) root.findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                session = activity.getSession();

                session.logoutUser();
            }
        });

        return root;

    }


}