# Panduan Pembagian Tugas & Alur Kolaborasi GitHub (Kelompok 4 Orang)
**Proyek:** Bus Booking System Android Application  

Dokumen ini berisi pembagian tugas, daftar file yang dikelola, alur kerja Git/GitHub, serta perintah terminal untuk masing-masing anggota agar kolaborasi berjalan rapi dan terhindar dari *merge conflict*.

---

## 1. Pembagian Tugas & File untuk 4 Anggota

###  Anggota 1: Modul Autentikasi & Akun Pengguna (Team Lead)
Fokus pada alur pendaftaran akun, login, verifikasi OTP, manajemen sesi, dan entitas data pengguna di Room Database.

- **File Java / Kotlin:**
  - `login.java`
  - `signup.java`
  - `Forgetpassword.java`
  - `updatepassword.java`
  - `setNewPassword.java`
  - `otp_verify.java`
  - `otp_verify_forgotpassword.java`
  - `updatedpasswordsucess.java`
  - `forgetpassword_sucessmsg.java`
  - `sessionmanager.java`
  - `data/UserEntity.java`
  - `data/UserDao.java`
- **File Layout XML:**
  - `activity_login.xml`
  - `activity_signup.xml`
  - `activity_forgetpassword.xml`
  - `activity_updatepassword.xml`
  - `activity_set_new_password.xml`
  - `activity_otp_verify.xml`
  - `activity_otp_verify_forgotpassword.xml`
  - `activity_updatedpasswordsucess.xml`
  - `activity_forgetpassword_sucessmsg.xml`
- **Branch Git:** `feature/auth-user`

---

###  Anggota 2: Modul Beranda, Navigasi & Pemesanan Bus
Fokus pada alur navigasi utama, tampilan beranda, pencarian rute/jadwal bus, dan pemesanan tamu (*guest booking*).

- **File Java / Kotlin:**
  - `welcomePage.java`
  - `Dashboard_home.java`
  - `dashboard_booking.java`
  - `dashboard_profile.java`
  - `guestbooking.java`
  - `verifyotp_guestbooking.java`
  - `guestwarning.java`
  - `splash_screen.java`
  - `walkthrough.java`
  - `slideradapter.java`
- **File Layout XML:**
  - `activity_welcome_page.xml`
  - `fragment_dashboard_home.xml`
  - `fragment_dashboard_booking.xml`
  - `fragment_dashboard_profile.xml`
  - `activity_guestbooking.xml`
  - `activity_verifyotp_guestbooking.xml`
  - `activity_guestwarning.xml`
  - `activity_splash_screen.xml`
  - `activity_walkthrough.xml`
  - `slide_layout.xml`
  - `getlogin.xml`
- **Menu & Navigasi:**
  - `navigation/navigation_manager.xml`
  - `menu/bottom_navigation_menu.xml`
  - `menu/bus_context_menu.xml`
  - `menu/profile_options_menu.xml`
- **Branch Git:** `feature/booking-dashboard`

---

###  Anggota 3: Modul Manajemen Kursi & Tiket
Fokus pada visualisasi denah kursi bus, pemilihan kursi, validasi kapasitas, konfirmasi tiket, dan riwayat pesanan.

- **File Java / Kotlin:**
  - `seatManagement.java`
  - `Confirmticket.java`
  - `bookedsuccesfully.java`
  - `trackbus.java`
  - `data/BookingEntity.java`
  - `data/BookingDao.java`
  - `data/AppDatabase.java`
- **File Layout XML:**
  - `activity_seat_management.xml`
  - `activity_confirmticket.xml`
  - `activity_bookedsuccesfully.xml`
  - `activity_trackbus.xml`
  - `seathintdialoge.xml`
- **Branch Git:** `feature/seat-management`

---

###  Anggota 4: Modul Fitur Pendukung, Chatbot AI & Rideshare
Fokus pada layanan bantuan otomatis (*Chatbot*), fitur *rideshare* (tambah kendaraan & verifikasi), serta halaman informasi dan umpan balik.

- **File Java / Kotlin:**
  - `ui/mainchatbott.kt`
  - `ui/MessagingAdapter.kt`
  - `utils/BotResponse.kt`
  - `utils/Constants.kt`
  - `utils/SolveMath.kt`
  - `utils/Time.kt`
  - `data/Message.kt`
  - `rideshare_homepage.java`
  - `adding_vehicle.java`
  - `otpverify_rideshare.java`
  - `HelpActivity.java`
  - `feedback.java`
  - `contact_developer.java`
  - `Aboutus.java`
  - `whyonlinebooking.java`
- **File Layout XML:**
  - `activity_mainchatbott.xml`
  - `message_item.xml`
  - `fragment_rideshare_homepage.xml`
  - `activity_adding_vehicle.xml`
  - `activity_otpverify_rideshare.xml`
  - `activity_help.xml`
  - `activity_feedback.xml`
  - `activity_contact_developer.xml`
  - `activity_aboutus.xml`
  - `activity_whyonlinebooking.xml`
- **Menu:**
  - `menu/dropdown_vehicletype.xml`
- **Branch Git:** `feature/chatbot-rideshare`

---

## 2. Struktur Branch Repositori

```text
main (Cabang Produksi / Versi Final yang siap dinilai/dipresentasikan)
  │
  └── dev (Cabang Integrasi / Tempat semua fitur disatukan dan diuji coba)
        │
        ├── feature/auth-user          <-- Dikelola Anggota 1
        ├── feature/booking-dashboard  <-- Dikelola Anggota 2
        ├── feature/seat-management    <-- Dikelola Anggota 3
        └── feature/chatbot-rideshare  <-- Dikelola Anggota 4
```

---

## 3. Langkah-Langkah Eksekusi Git

### Tahap 1: Setup Awal oleh Ketua (1 Orang Saja)
1. Buka terminal di folder proyek ini.
2. Inisialisasi Git dan push seluruh proyek dasar:
   ```bash
   git init
   git add .
   git commit -m "Initial commit: clean bus booking app"
   git branch -M main
   git remote add origin https://github.com/USERNAME_KETUA/NAMA_REPO.git
   git push -u origin main
   ```
3. Buat branch `dev` dan push ke GitHub:
   ```bash
   git checkout -b dev
   git push -u origin dev
   ```
4. Tambahkan akun GitHub ketiga anggota lain sebagai kolaborator di:
   `Settings > Collaborators > Add people`.

---

### Tahap 2: Anggota Mengambil Proyek (Clone)
Setiap anggota meng-clone repositori ke komputer masing-masing:
```bash
git clone https://github.com/USERNAME_KETUA/NAMA_REPO.git
cd NAMA_REPO
git checkout dev
```

---

### Tahap 3: Masing-Masing Anggota Membuat Branch Fitur
Setiap anggota membuat branch fitur dari `dev`, melakukan perubahan kode modulnya, lalu melakukan commit dan push:

#### Anggota 1:
```bash
git checkout dev
git checkout -b feature/auth-user
# Melakukan perubahan / peningkatan pada modul Autentikasi
git add .
git commit -m "Penyempurnaan validasi login dan registrasi"
git push -u origin feature/auth-user
```

#### Anggota 2:
```bash
git checkout dev
git checkout -b feature/booking-dashboard
# Melakukan perubahan / peningkatan pada modul Dashboard & Booking
git add .
git commit -m "Penyempurnaan navigasi dan dashboard booking"
git push -u origin feature/booking-dashboard
```

#### Anggota 3:
```bash
git checkout dev
git checkout -b feature/seat-management
# Melakukan perubahan / peningkatan pada modul Seat & Confirm Ticket
git add .
git commit -m "Penyempurnaan pemilihan kursi dan konfirmasi tiket"
git push -u origin feature/seat-management
```

#### Anggota 4:
```bash
git checkout dev
git checkout -b feature/chatbot-rideshare
# Melakukan perubahan / peningkatan pada modul Chatbot & Rideshare
git add .
git commit -m "Integrasi respon chatbot dan form rideshare"
git push -u origin feature/chatbot-rideshare
```

---

### Tahap 4: Menggabungkan Kode (*Pull Request / PR*)
1. Buka halaman repositori di GitHub.
2. Klik tombol **Compare & pull request** pada branch yang baru saja di-push.
3. Pastikan target branch ditujukan ke **`dev`** (bukan ke `main`).
4. Anggota lain memeriksa (*Code Review*), lalu klik **Merge pull request**.
5. Setelah seluruh 4 fitur digabungkan di branch `dev` dan dites jalan tanpa error, Ketua membuat PR terakhir dari `dev` ke **`main`**.

---

## 4. Aturan Penting Agar Tidak Terjadi *Merge Conflict*
1. **Selalu Tarik Update Terbaru:** Sebelum mulai coding atau membuat branch baru, jalankan:
   ```bash
   git checkout dev
   git pull origin dev
   ```
2. **File Bersama yang Rawan Konflik:**
   - `AndroidManifest.xml`
   - `res/values/strings.xml`
   - `res/values/colors.xml`
   - `app/build.gradle`
   *Jika ingin menambahkan activity baru atau string baru, koordinasikan terlebih dahulu dengan tim.*
3. **Jangan Push File Konfigurasi Lokal:**
   Pastikan file `local.properties` dan folder `build/`, `.gradle/` tetap terdaftar di `.gitignore` dan tidak di-push ke GitHub.
