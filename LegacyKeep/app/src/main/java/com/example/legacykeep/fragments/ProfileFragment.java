package com.example.legacykeep.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.UserProfileDTO;
import com.example.legacykeep.LocaleHelper;
import com.example.legacykeep.R;
import com.example.legacykeep.WelcomeActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment profilu użytkownika, który wyświetla dane użytkownika, umożliwia edycję zdjęcia profilowego,
 * zmianę języka aplikacji oraz przełączanie trybu ciemnego.
 *
 * <p>Klasa odpowiada za:
 * <ul>
 *   <li>Pobieranie i wyświetlanie danych profilu z backendu,</li>
 *   <li>Zmianę zdjęcia profilowego przez wybór z galerii lub zrobienie zdjęcia aparatem,</li>
 *   <li>Zarządzanie uprawnieniami do odczytu pamięci i korzystania z aparatu,</li>
 *   <li>Obsługę wylogowania użytkownika,</li>
 *   <li>Zmianę języka aplikacji oraz przełączanie trybu ciemnego z zapisem w preferencjach.</li>
 * </ul>
 */
public class ProfileFragment extends Fragment {

    /* -------------------- constants -------------------- */

    /**
     * Kod żądania uprawnienia do odczytu pamięci zewnętrznej (galerii).
     */
    private static final int STORAGE_PERMISSION_REQUEST = 2;

    /**
     * Kod żądania uprawnienia do korzystania z aparatu.
     */
    private static final int CAMERA_PERMISSION_REQUEST  = 3;

    /* -------------------- views -------------------- */

    /**
     * Tekstowa kontrolka wyświetlająca imię i nazwisko użytkownika.
     */
    private TextView profileName;

    /**
     * Tekstowa kontrolka wyświetlająca adres email użytkownika.
     */
    private TextView profileEmail;

    /**
     * Obraz wyświetlający zdjęcie profilowe użytkownika.
     */
    private ImageView profileImage;

    /**
     * Przełącznik trybu ciemnego (dark mode).
     */
    private SwitchCompat darkModeSwitch;

    /* -------------------- pickers -------------------- */

    /**
     * Launcher aktywności do wyboru obrazu z galerii.
     */
    private ActivityResultLauncher<Intent> galleryLauncher;

    /**
     * Launcher aktywności do wykonania zdjęcia aparatem.
     */
    private ActivityResultLauncher<Intent> cameraLauncher;
/**
     * Tworzy i inicjalizuje widok fragmentu profilu użytkownika.
     *
     * <p>Metoda odpowiedzialna za:
     * <ul>
     *   <li>Inflację layoutu fragmentu (fragment_profile.xml),</li>
     *   <li>Inicjalizację widoków (ImageView, TextView, Switch, itp.),</li>
     *   <li>Wczytanie zapisanych danych użytkownika (nazwa, email) z SharedPreferences i ustawienie ich w widokach,</li>
     *   <li>Obsługę przycisku wylogowania — czyszczenie SharedPreferences i powrót do ekranu powitalnego,</li>
     *   <li>Wyświetlanie dialogu wyboru języka aplikacji i zmiana języka z ponownym odtworzeniem aktywności,</li>
     *   <li>Inicjalizację przełącznika trybu ciemnego oraz reakcję na jego zmianę z zapisem stanu w SharedPreferences i ustawieniem trybu nocnego,</li>
     *   <li>Inicjalizację launcherów do wyboru zdjęć (kamera, galeria) przez wywołanie {@link #initPickers()},</li>
     *   <li>Obsługę kliknięcia w obraz profilowy, które wywołuje dialog wyboru źródła obrazu,</li>
     *   <li>Pobranie danych profilu użytkownika z backendu przez wywołanie {@link #fetchUserProfile()}.</li>
     * </ul>
     *
     * @param inflater Inflater służący do rozdmuchiwania layoutów XML na obiekty View.
     * @param container Kontener, do którego zostanie dołączony fragment (może być null).
     * @param savedInstanceState Zapisany stan fragmentu, jeśli istnieje (może być null).
     * @return Zainicjalizowany i skonfigurowany widok fragmentu profilu.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /* ---- view-binding ---- */
        profileImage   = view.findViewById(R.id.profileImage);
        profileName    = view.findViewById(R.id.profileName);
        profileEmail   = view.findViewById(R.id.profileEmail);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        LinearLayout logoutRow   = view.findViewById(R.id.logoutRow);
        LinearLayout languageRow = view.findViewById(R.id.languageRow);

        /* ---- prefs ---- */
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);

        profileName.setText(prefs.getString("username", "Unknown User"));
        profileEmail.setText(prefs.getString("email",    "Unknown Email"));

        /* ---- logout ---- */
        logoutRow.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent i = new Intent(requireContext(), WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            Toast.makeText(requireContext(),
                    "Logged out successfully", Toast.LENGTH_SHORT).show();
        });

        /* ---- language picker ---- */
        languageRow.setOnClickListener(v -> {
            final String[] langs = {"English", "Polski"};
            final String[] codes = {"en",      "pl"};

            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Select Language")
                    .setItems(langs, (d, which) -> {
                        prefs.edit().putString("app_language", codes[which]).apply();
                        LocaleHelper.setLocale(requireContext(), codes[which]);
                        requireActivity().recreate();
                    }).show();
        });

        /* ---- dark-mode switch ---- */
        boolean darkEnabled = prefs.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(darkEnabled);
        darkModeSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
        });

        /* ---- pickers ---- */
        initPickers();
        profileImage.setOnClickListener(v -> showImageSourceOptions());

        /* ---- profile data from backend ---- */
        fetchUserProfile();

        return view;
    }

    /**
     * Inicjalizuje launchery do obsługi wyboru obrazu z galerii oraz wykonania zdjęcia aparatem.
     *
     * <p>Metoda rejestruje dwa obiekty {@link ActivityResultLauncher} wykorzystujące kontrakt
     * {@link ActivityResultContracts.StartActivityForResult}, które obsługują wyniki zwrócone przez Intenty:
     * <ul>
     *   <li><b>galleryLauncher</b> — obsługuje wynik wyboru obrazu z galerii.
     *       Po poprawnym wyborze ładuje wybrany obraz do widoku {@code profileImage} przy użyciu biblioteki Glide.</li>
     *   <li><b>cameraLauncher</b> — obsługuje wynik wykonania zdjęcia aparatem.
     *       Po poprawnym wykonaniu zdjęcia:
     *       <ul>
     *         <li>pobiera bitmapę ze zwróconych danych,</li>
     *         <li>konwertuje ją na URI wywołując metodę {@link #getImageUriFromBitmap(Context, Bitmap)},</li>
     *         <li>jeśli URI jest prawidłowe, wywołuje metodę {@link #uploadProfilePicture(Uri)}, aby przesłać zdjęcie na serwer,</li>
     *         <li>w przeciwnym razie wyświetla komunikat o błędzie.</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p>Ta metoda powinna być wywołana w cyklu życia fragmentu lub aktywności, aby przygotować launchery przed ich użyciem.
     */
    private void initPickers() {

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == AppCompatActivity.RESULT_OK && res.getData() != null) {
                        Uri uri = res.getData().getData();
                        if (uri != null) Glide.with(this).load(uri).into(profileImage);
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == AppCompatActivity.RESULT_OK && res.getData() != null) {
                        Bitmap bmp = (Bitmap) res.getData().getExtras().get("data");
                        Uri uri    = getImageUriFromBitmap(requireContext(), bmp);
                        if (uri != null) uploadProfilePicture(uri);
                        else Toast.makeText(requireContext(),
                                "Failed to capture image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Wyświetla dialog wyboru źródła obrazu: kamera lub galeria.
     *
     * <p>Po wybraniu opcji metoda sprawdza, czy aplikacja posiada wymagane uprawnienia:
     * <ul>
     *   <li>Dla kamery: {@code Manifest.permission.CAMERA}</li>
     *   <li>Dla galerii: {@code Manifest.permission.READ_EXTERNAL_STORAGE}</li>
     * </ul>
     *
     * <p>Jeśli uprawnienie nie zostało nadane, wywołuje zapytanie o przyznanie uprawnień.
     * W przeciwnym wypadku uruchamia odpowiednią metodę {@code openCamera()} lub {@code openGallery()}.
     *
     * <p>Dialog wyświetla tytuł "Select Image Source" i dwie opcje do wyboru.
     */
    private void showImageSourceOptions() {
        String[] opts = {"Camera", "Gallery"};
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(opts, (dialog, which) -> {
                    if (which == 0) { // wybór kamery
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Brak uprawnień do kamery — prosimy użytkownika o ich przyznanie
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST);
                        } else {
                            // Uprawnienia już są — otwieramy kamerę
                            openCamera();
                        }
                    } else { // wybór galerii
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Brak uprawnień do odczytu pamięci — prosimy użytkownika o ich przyznanie
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_REQUEST);
                        } else {
                            // Uprawnienia już są — otwieramy galerię
                            openGallery();
                        }
                    }
                }).show();
    }

    /**
     * Otwiera galerię zdjęć, pozwalając użytkownikowi wybrać obraz z pamięci urządzenia.
     *
     * <p>Tworzy Intent z akcją ACTION_PICK i URI wskazującym na kolekcję obrazów w pamięci zewnętrznej.
     * Następnie uruchamia launcher, który obsłuży wynik wyboru.
     */
    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(i);
    }

    /**
     * Otwiera aplikację aparatu, pozwalając użytkownikowi zrobić zdjęcie.
     *
     * <p>Tworzy Intent z akcją ACTION_IMAGE_CAPTURE i uruchamia launcher, który obsłuży wynik wykonania zdjęcia.
     */
    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(i);
    }

    /**
     * Konwertuje przekazany obiekt {@link Bitmap} na URI wskazujące na zapisany obraz w galerii.
     *
     * <p>Bitmap jest kompresowany do formatu JPEG o jakości 100, następnie zapisywany do systemowej galerii
     * za pomocą {@link MediaStore.Images.Media#insertImage}. Metoda zwraca URI do zapisanego obrazu.
     *
     * @param ctx Kontekst aplikacji, wymagany do uzyskania ContentResolvera.
     * @param bmp Obiekt {@link Bitmap}, który ma zostać zapisany.
     * @return URI wskazujące na zapisany obraz w systemowej galerii lub null, jeśli zapis się nie powiódł.
     */
    private Uri getImageUriFromBitmap(Context ctx, Bitmap bmp) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        String path = MediaStore.Images.Media.insertImage(
                ctx.getContentResolver(), bmp, "CapturedImage", null);
        return Uri.parse(path);
    }

    /**
     * Wysyła nowy obraz profilowy użytkownika na serwer jako multipart/form-data.
     *
     * <p>Proces działania:
     * <ol>
     *   <li>Pobiera token autoryzacyjny zapisany w SharedPreferences.</li>
     *   <li>Jeśli token jest pusty (użytkownik nie jest zalogowany), wyświetla komunikat i przerywa działanie.</li>
     *   <li>Próbuje odczytać zawartość pliku wskazanego przez URI do tablicy bajtów.</li>
     *   <li>Tworzy RequestBody z typu "image/*" oraz danych obrazu.</li>
     *   <li>Opakowuje RequestBody w MultipartBody.Part z nazwą pola "profilePicture" i nazwą pliku "profile.jpg".</li>
     *   <li>Wysyła żądanie HTTP do API aktualizacji zdjęcia profilowego z nagłówkiem autoryzacji Bearer.</li>
     *   <li>W przypadku sukcesu wyświetla komunikat i odświeża dane profilu użytkownika.</li>
     *   <li>W przypadku błędu lub niepowodzenia wyświetla stosowny komunikat Toast.</li>
     *   <li>W przypadku problemu z odczytem pliku obrazowego (IOException) również wyświetla komunikat o błędzie.</li>
     * </ol>
     *
     * @param uri URI wskazujące na obraz profilowy do przesłania (np. z galerii lub kamery).
     */
    private void uploadProfilePicture(Uri uri) {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("authToken", null);

        // Sprawdzenie, czy użytkownik jest zalogowany
        if (token == null) {
            Toast.makeText(requireContext(),
                    "You must be logged in to update your profile picture",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Odczyt wszystkich bajtów obrazu z podanego URI
            byte[] data = readBytesFromInputStream(
                    requireContext().getContentResolver().openInputStream(uri));

            // Utworzenie RequestBody z danymi obrazu i typem MIME "image/*"
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), data);

            // Utworzenie MultipartBody.Part dla pola formularza "profilePicture" z nazwą pliku "profile.jpg"
            MultipartBody.Part part =
                    MultipartBody.Part.createFormData("profilePicture", "profile.jpg", body);

            // Wywołanie API do aktualizacji zdjęcia profilowego
            ApiService api = ApiClient.getApiService();
            api.updateProfilePicture("Bearer " + token, part)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                // Sukces - wyświetlenie komunikatu i odświeżenie profilu
                                Toast.makeText(requireContext(),
                                        "Profile picture updated successfully",
                                        Toast.LENGTH_SHORT).show();
                                fetchUserProfile();
                            } else {
                                // Niepowodzenie odpowiedzi serwera
                                Toast.makeText(requireContext(),
                                        "Failed to update profile picture",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            // Błąd sieci lub inny problem przy wywołaniu API
                            Toast.makeText(requireContext(),
                                    "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            // Problem z odczytem danych obrazu z URI
            Toast.makeText(requireContext(),
                    "Failed to read image data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Wczytuje wszystkie bajty z podanego strumienia wejściowego do tablicy bajtów.
     *
     * @param in Strumień wejściowy {@link InputStream}, z którego będą czytane dane.
     * @return Tablica bajtów zawierająca wszystkie dane odczytane ze strumienia.
     * @throws IOException Jeśli podczas odczytu ze strumienia wystąpi błąd.
     *
     * <p>Opis działania:
     * <ul>
     *   <li>Tworzy bufor {@link ByteArrayOutputStream}, do którego będzie zapisywał odczytane dane.</li>
     *   <li>Tworzy tymczasową tablicę bajtów o rozmiarze 1024 bajtów jako bufor odczytu.</li>
     *   <li>W pętli odczytuje dane ze strumienia do tymczasowego bufora aż do końca strumienia (czyli {@code in.read()} zwraca -1).</li>
     *   <li>Zapisuje każdą porcję danych do ByteArrayOutputStream.</li>
     *   <li>Po zakończeniu zwraca wszystkie odczytane bajty jako tablicę.</li>
     * </ul>
     */
    private byte[] readBytesFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int len;
        while ((len = in.read(tmp)) != -1) {
            buf.write(tmp, 0, len);
        }
        return buf.toByteArray();
    }

/* =========================================================
   permissions callback
   ========================================================= */

    /**
     * Callback wywoływany po odpowiedzi użytkownika na zapytanie o uprawnienia.
     *
     * <p>Metoda sprawdza, czy użytkownik udzielił uprawnienia do dostępu do pamięci
     * lub kamery, a następnie wywołuje odpowiednie metody do otwarcia galerii lub kamery.
     * W przypadku odmowy wyświetla komunikat o braku uprawnień.
     *
     * @param req          Kod żądania uprawnienia (np. STORAGE_PERMISSION_REQUEST, CAMERA_PERMISSION_REQUEST).
     * @param perms        Tablica nazw uprawnień, o które proszono.
     * @param grantResults Tablica wyników przyznania uprawnień (PackageManager.PERMISSION_GRANTED lub PERMISSION_DENIED).
     */
    @Override
    public void onRequestPermissionsResult(int req,
                                           @NonNull String[] perms,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(req, perms, grantResults);

        // Sprawdza, czy żądanie dotyczyło dostępu do pamięci i czy zostało przyznane
        if (req == STORAGE_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery(); // Otwiera galerię zdjęć
        }
        // Sprawdza, czy żądanie dotyczyło dostępu do kamery i czy zostało przyznane
        else if (req == CAMERA_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera(); // Otwiera kamerę
        }
        // Jeśli uprawnienie nie zostało przyznane, wyświetla komunikat o odmowie
        else {
            Toast.makeText(requireContext(),
                    "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Metoda asynchronicznie pobiera dane profilu użytkownika z serwera i aktualizuje UI.
     *
     * <p>Proces działania:
     * <ol>
     *   <li>Odczytuje token uwierzytelniający zapisany w SharedPreferences.</li>
     *   <li>Jeśli token jest pusty (użytkownik nie jest zalogowany), wyświetla komunikat i przerywa działanie.</li>
     *   <li>Wywołuje endpoint API do pobrania profilu użytkownika z nagłówkiem autoryzacji Bearer.</li>
     *   <li>W przypadku poprawnej odpowiedzi uzupełnia pola widoku profilowego: nazwę, email, zdjęcie.</li>
     *   <li>Jeśli odpowiedź jest nieudana lub wystąpił błąd, wyświetla odpowiedni komunikat Toast.</li>
     * </ol>
     *
     * <p>Używane biblioteki i klasy:
     * <ul>
     *   <li>{@link SharedPreferences} - do przechowywania i odczytu tokenu autoryzacyjnego.</li>
     *   <li>{@link Toast} - do wyświetlania krótkich komunikatów dla użytkownika.</li>
     *   <li>{@link ApiService} i {@link ApiClient} - do wywołania REST API.</li>
     *   <li>{@link Glide} - do ładowania obrazu profilowego z URL.</li>
     * </ul>
     *
     * <p>Ważne uwagi:
     * <ul>
     *   <li>Metoda zakłada, że jest wywoływana w kontekście fragmentu lub activity, które implementuje metodę {@code requireContext()} oraz ma dostęp do pól UI: {@code profileName}, {@code profileEmail}, {@code profileImage}.</li>
     *   <li>Sprawdzanie {@code isAdded()} zabezpiecza przed aktualizacją UI, jeśli fragment został odłączony.</li>
     * </ul>
     */
    private void fetchUserProfile() {
        // Odczyt tokenu autoryzacji z SharedPreferences o nazwie "LegacyKeepPrefs"
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("authToken", null);

        // Jeśli token jest pusty, oznacza to, że użytkownik nie jest zalogowany
        if (token == null) {
            // Wyświetlenie komunikatu informującego o konieczności zalogowania się
            Toast.makeText(requireContext(),
                    "You must be logged in to view your profile",
                    Toast.LENGTH_SHORT).show();
            return; // Przerwanie dalszego działania metody
        }

        // Inicjalizacja serwisu API do wywołania endpointu
        ApiService api = ApiClient.getApiService();

        // Wywołanie asynchroniczne endpointu getUserProfile z nagłówkiem autoryzacji "Bearer <token>"
        api.getUserProfile("Bearer " + token)
                .enqueue(new Callback<UserProfileDTO>() {
                    @Override
                    public void onResponse(Call<UserProfileDTO> call, Response<UserProfileDTO> response) {
                        // Sprawdzenie czy fragment jest nadal dołączony do activity
                        if (!isAdded()) return;

                        // Sprawdzenie, czy odpowiedź jest poprawna oraz zawiera ciało odpowiedzi
                        if (response.isSuccessful() && response.body() != null) {
                            UserProfileDTO userProfile = response.body();

                            // Ustawienie danych w elementach UI: nazwa i email użytkownika
                            profileName.setText(userProfile.getName());
                            profileEmail.setText(userProfile.getEmail());

                            // Jeśli użytkownik posiada zdjęcie profilowe, załadowanie go z URL
                            if (userProfile.getProfilePicture() != null) {
                                Glide.with(requireContext())
                                        .load(userProfile.getProfilePicture())        // URL zdjęcia
                                        .placeholder(R.drawable.ic_profile)             // obraz zastępczy podczas ładowania
                                        .error(R.drawable.ic_profile)                   // obraz w przypadku błędu ładowania
                                        .skipMemoryCache(true)                           // nie korzystaj z cache pamięci
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)      // nie korzystaj z cache dysku
                                        .into(profileImage);                             // docelowy ImageView
                            }
                        } else {
                            // Jeśli odpowiedź nie jest udana, wyświetl komunikat o niepowodzeniu
                            Toast.makeText(requireContext(),
                                    "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfileDTO> call, Throwable t) {
                        // Sprawdzenie, czy fragment jest nadal dołączony do activity
                        if (!isAdded()) return;

                        // Wyświetlenie komunikatu o błędzie podczas połączenia lub innego problemu
                        Toast.makeText(requireContext(),
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
