package com.shreebrahmanitravels.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Dashboard_home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentActivity myContext;

    public Dashboard_home() {
        // Required empty public constructor
    }

    public static Dashboard_home newInstance(String param1, String param2) {
        Dashboard_home fragment = new Dashboard_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dashboard_home, container, false);

        // ===== CONTROLS =====
        final TextView fromText = v.findViewById(R.id.fromText);
        final TextView toText = v.findViewById(R.id.toText);
        final TextView setdatedate = v.findViewById(R.id.setdatedate);
        final View swapBtn = v.findViewById(R.id.swapBtn);
        final View hariIniBtn = v.findViewById(R.id.hariIniBtn);
        final View besokBtn = v.findViewById(R.id.besokBtn);
        final Button serchbustn = v.findViewById(R.id.serchbustn);

        // Swap Origin & Destination
        if (swapBtn != null && fromText != null && toText != null) {
            swapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharSequence temp = fromText.getText();
                    fromText.setText(toText.getText());
                    toText.setText(temp);
                }
            });
        }

        // Quick Date: Hari ini
        if (hariIniBtn != null && setdatedate != null) {
            hariIniBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", new Locale("id", "ID"));
                    setdatedate.setText(sdf.format(new Date()));
                }
            });
        }

        // Quick Date: Besok
        if (besokBtn != null && setdatedate != null) {
            besokBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", new Locale("id", "ID"));
                    setdatedate.setText(sdf.format(c.getTime()));
                }
            });
        }

        // Cari Bus Button -> Opens dashboard_booking (Search Results)
        if (serchbustn != null) {
            serchbustn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dashboard_booking bookingFragment = new dashboard_booking();
                    Bundle bundle = new Bundle();
                    if (fromText != null) bundle.putString("from", fromText.getText().toString());
                    if (toText != null) bundle.putString("to", toText.getText().toString());
                    if (setdatedate != null) bundle.putString("date", setdatedate.getText().toString());
                    bookingFragment.setArguments(bundle);

                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                                .replace(R.id.fragmant_container, bookingFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        // Back pressed callback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah Anda yakin ingin keluar?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.create().show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            myContext = (FragmentActivity) context;
        }
    }
}