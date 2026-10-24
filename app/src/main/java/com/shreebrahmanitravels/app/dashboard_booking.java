package com.shreebrahmanitravels.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.BookingEntity;
import com.shreebrahmanitravels.app.data.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dashboard_booking extends Fragment {

    TextView date, day_month, busroutetext, recentbooktext, dateonticket, busrouteonticket, seatnumberonticket;
    String currentday, currentmonth;
    String day_Monthstr;
    int date_, month_, year_;
    Button serchbusbtn, trackbus;
    Boolean bookingsecondtime = false;
    TextInputLayout outbox;
    View ticket;
    ProgressBar progressBar;
    RadioGroup radioGroupBusClass;

    public dashboard_booking() {
        // Required empty public constructor
    }

    public static dashboard_booking newInstance(String param1, String param2) {
        dashboard_booking fragment = new dashboard_booking();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_booking, container, false);

        final String username = new sessionmanager(requireContext()).getuserdetail();

        progressBar = v.findViewById(R.id.progressbarseatbooking);
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);

        ticket = v.findViewById(R.id.relativeticketview);
        date = v.findViewById(R.id.setdatedate);
        day_month = v.findViewById(R.id.day_year);
        busroutetext = v.findViewById(R.id.editTextonseatbooking);
        serchbusbtn = v.findViewById(R.id.serchbustn);
        recentbooktext = v.findViewById(R.id.recentbookedtext);
        dateonticket = v.findViewById(R.id.dateonticket);
        busrouteonticket = v.findViewById(R.id.busroutetextonticketedit);
        seatnumberonticket = v.findViewById(R.id.seatonticketedit);
        trackbus = v.findViewById(R.id.trackbusbtn);
        outbox = v.findViewById(R.id.outboxofbusrouteonbooking);
        radioGroupBusClass = v.findViewById(R.id.radioGroupBusClass);

        // Register Context Menu on bus ticket card
        if (ticket != null) {
            registerForContextMenu(ticket);
            ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), Confirmticket.class);
                    String busno = (busroutetext != null && busroutetext.getText() != null && !busroutetext.getText().toString().isEmpty())
                            ? busroutetext.getText().toString() : "SB1";
                    intent.putExtra("busnumber", busno);
                    startActivity(intent);
                }
            });
        }

        // Radio Button & Radio Group functionality
        if (radioGroupBusClass != null) {
            radioGroupBusClass.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.radioAllClass) {
                        Toast.makeText(getContext(), "Menampilkan semua kelas bus", Toast.LENGTH_SHORT).show();
                    } else if (checkedId == R.id.radioExecutive) {
                        Toast.makeText(getContext(), "Filter: Kelas Executive", Toast.LENGTH_SHORT).show();
                    } else if (checkedId == R.id.radioSleeper) {
                        Toast.makeText(getContext(), "Filter: Kelas Sleeper", Toast.LENGTH_SHORT).show();
                    } else if (checkedId == R.id.radioEconomy) {
                        Toast.makeText(getContext(), "Filter: Kelas Economy", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Setup Date
        Calendar calendartommorw = Calendar.getInstance();
        calendartommorw.add(Calendar.DAY_OF_YEAR, 1);
        final int date_tommorow = calendartommorw.get(Calendar.DATE);
        String tommorow_day = (String) DateFormat.format("EEE", calendartommorw);
        String tommorow_month = (String) DateFormat.format("MMM", calendartommorw);
        String cmbinemonth = tommorow_day + ",\n" + tommorow_month;

        Calendar calendar = Calendar.getInstance();
        date_ = calendar.get(Calendar.DATE);
        month_ = calendar.get(Calendar.MONTH);
        year_ = calendar.get(Calendar.YEAR);
        currentmonth = (String) DateFormat.format("MMM", new Date());
        currentday = (String) DateFormat.format("EEE", new Date());
        day_Monthstr = currentday + ",\n" + currentmonth;

        if (date != null) date.setText(String.valueOf(date_tommorow));
        if (day_month != null) day_month.setText(cmbinemonth);

        // Load user and booking details from Room Database
        loadBookingDataFromRoom(username);

        // Search bus button
        if (serchbusbtn != null) {
            serchbusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), Confirmticket.class);
                    String busno = (busroutetext != null && busroutetext.getText() != null && !busroutetext.getText().toString().isEmpty())
                            ? busroutetext.getText().toString() : "SB1";
                    intent.putExtra("busnumber", busno);
                    startActivity(intent);
                }
            });
        }

        if (trackbus != null) {
            trackbus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), trackbus.class));
                }
            });
        }

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

    // Context Menu Creation for bus card
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.bus_context_menu, menu);
        menu.setHeaderTitle("Pilihan Layanan Bus");
    }

    // Context Menu Item Selection
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.context_detail) {
            showBusDetailDialog();
            return true;
        } else if (id == R.id.context_share) {
            shareBusRoute();
            return true;
        } else if (id == R.id.context_favorite) {
            Toast.makeText(getContext(), "⭐ Sinar Jaya Shuttle berhasil ditambahkan ke Favorit!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showBusDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Detail Bus: Sinar Jaya Shuttle")
                .setMessage("Operator: Sinar Jaya Shuttle\nKelas: Shuttle Executive 1+1\nRute: Terminal Pulo Gebang -> Surapati (Suci)\nJadwal: 15:30 - 17:30 WIB (Durasi 2 jam)\nHarga: Rp 120.000 / kursi\nFasilitas: AC, Reclining Seat, USB Charger, Bagasi, Air Mineral")
                .setPositiveButton("Pesan Sekarang", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), Confirmticket.class);
                        intent.putExtra("busnumber", "SB1");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tutup", null)
                .show();
    }

    private void shareBusRoute() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rute Bus Sinar Jaya Shuttle");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Ayo pesan tiket bus Sinar Jaya Shuttle! Rute Pulo Gebang ke Surapati (Bandung) mulai Rp 120.000 lewat aplikasi RideShare.");
        startActivity(Intent.createChooser(shareIntent, "Bagikan Informasi Rute Bus"));
    }

    private void loadBookingDataFromRoom(final String username) {
        if (username == null || username.equals("Guest user")) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                final UserEntity user = db.userDao().getUserByUsername(username);
                final BookingEntity lastBooking = db.bookingDao().getLastBooking(username);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null && user.getBus_no() != null && busroutetext != null) {
                                busroutetext.setText(user.getBus_no());
                            }
                            if (lastBooking != null && lastBooking.isBooked()) {
                                if (recentbooktext != null) recentbooktext.setVisibility(View.VISIBLE);
                                if (dateonticket != null) dateonticket.setText(lastBooking.getDate());
                                if (busrouteonticket != null) busrouteonticket.setText("Rute: " + lastBooking.getBusRoute());
                                if (seatnumberonticket != null) seatnumberonticket.setText("Kursi: " + lastBooking.getSeatId());
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
