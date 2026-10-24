package com.shreebrahmanitravels.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class dashboard_profile extends Fragment {
    String name, username, contact, route, usernametop, branch, currentyear, pendingfees, stand;
    TextView usernameonprofiletxt, nametxt, usernametxt, branchtxt, standtxt, contacttxt, routetxt, yeartxt, feestxt;
    Button logoutbtn;
    Toolbar toolbar;

    public dashboard_profile() {
        // Required empty public constructor
    }

    public static dashboard_profile newInstance(String param1, String param2) {
        dashboard_profile fragment = new dashboard_profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sessionmanager sessionManager = new sessionmanager(getContext());
        if (sessionManager.getuserdetail() == null || sessionManager.getuserdetail().equals("Guest user")) {
            View v = inflater.inflate(R.layout.getlogin, container, false);
            Button signup = v.findViewById(R.id.signupbuttoninvite);
            Button login = v.findViewById(R.id.loginbtninvite);

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), signup.class);
                    new sessionmanager(requireContext()).logoutuserfromsession();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), login.class);
                    new sessionmanager(requireContext()).logoutuserfromsession();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            return v;
        }

        View v = inflater.inflate(R.layout.fragment_dashboard_profile, container, false);

        toolbar = v.findViewById(R.id.toolbarProfile);
        if (toolbar != null) {
            toolbar.inflateMenu(R.menu.profile_options_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return handleMenuAction(item.getItemId());
                }
            });
        }

        usernameonprofiletxt = v.findViewById(R.id.usernameonprofiletab);
        nametxt = v.findViewById(R.id.editablename);
        usernametxt = v.findViewById(R.id.usernameedit);
        branchtxt = v.findViewById(R.id.branchedit);
        standtxt = v.findViewById(R.id.standedit);
        contacttxt = v.findViewById(R.id.contactedit);
        routetxt = v.findViewById(R.id.busrouteedit);
        yeartxt = v.findViewById(R.id.yearedit);
        feestxt = v.findViewById(R.id.pendingfeesedit);
        logoutbtn = v.findViewById(R.id.logoutonprofile);

        if (logoutbtn != null) {
            logoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLogoutDialog();
                }
            });
        }

        loadUserDetails();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah Anda yakin ingin keluar aplikasi?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requireActivity().finish();
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (handleMenuAction(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean handleMenuAction(int itemId) {
        if (itemId == R.id.menu_logout) {
            showLogoutDialog();
            return true;
        } else if (itemId == R.id.menu_help) {
            Intent intent = new Intent(getContext(), HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sessionmanager sessionmanager_obj = new sessionmanager(getContext());
                        sessionmanager_obj.logoutuserfromsession();

                        Intent intent = new Intent(getContext(), login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    private void loadUserDetails() {
        final sessionmanager sessionmanagerobj = new sessionmanager(requireContext());
        final String loginusername = sessionmanagerobj.getuserdetail();

        if (loginusername == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                final UserEntity user = db.userDao().getUserByUsername(loginusername);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null) {
                                name = user.getName() != null ? user.getName() : "-";
                                username = user.getUsername() != null ? user.getUsername() : "-";
                                contact = user.getPhoneNo() != null ? user.getPhoneNo() : "-";
                                route = user.getBus_no() != null ? user.getBus_no() : "-";
                                branch = user.getBranch() != null ? user.getBranch() : "-";
                                currentyear = user.getCurrentyear() != null ? user.getCurrentyear() : "-";
                                stand = user.getStand() != null ? user.getStand() : "-";
                                pendingfees = user.getPendingfees() != null ? user.getPendingfees() : "0";

                                usernametop = name + "\n" + username;
                                usernameonprofiletxt.setText(usernametop);

                                nametxt.setText(": " + name);
                                usernametxt.setText(": " + username);
                                branchtxt.setText(": " + branch);
                                standtxt.setText(": " + stand);
                                contacttxt.setText(": " + contact);
                                routetxt.setText(": " + route);
                                yeartxt.setText(": " + currentyear);
                                feestxt.setText(": Rp " + pendingfees);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}