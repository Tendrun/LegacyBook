// app/src/main/java/com/example/legacykeep/fragments/CreatePostFragment.java
package com.example.legacykeep.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.CreatePostRequest;
import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;
import com.example.legacykeep.viewmodel.SharedPostViewModel;
import com.google.gson.Gson;

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
 * Fragment odpowiedzialny za tworzenie nowego posta.
 * Pozwala użytkownikowi na dodanie opisu, zdjęcia, nagrania głosowego oraz lokalizacji.
 */
public class CreatePostFragment extends Fragment {

    // ViewModel do współdzielenia danych między fragmentami
    private SharedPostViewModel viewModel;

    // Pole do wpisania opisu posta
    private EditText descriptionField;

    // Przycisk do przesyłania posta
    private Button uploadPhotoButton, recordVoiceButton, pickLocationButton, submitPostButton;

    // Przycisk do odtwarzania dźwięku
    private Button playAudioButton;

    // Przycisk do zatrzymywania nagrywania
    private Button stopRecordingButton;

    // Kod żądania do wyboru obrazu z galerii
    private static final int GALLERY_REQUEST_CODE = 1001;

    // Kod żądania dla wyboru pliku audio
    private static final int AUDIO_REQUEST_CODE = 2001;

    // Kod żądania o uprawnienie mikrofonu
    private static final int REQUEST_MICROPHONE_PERMISSION = 3001;

    // Nagrywanie dźwięku
    private MediaRecorder mediaRecorder;

    // Plik nagranego dźwięku
    private File audioFile;

    // URI wybranego zdjęcia
    private Uri selectedImageUri = null;

    // URI wybranego pliku audio
    private Uri selectedAudioUri = null;

    // Odtwarzacz audio
    private MediaPlayer mediaPlayer;

    // URI nagranego audio
    private Uri recordedAudioUri = null;

    /**
     * Tworzy i zwraca widok hierarchii interfejsu użytkownika dla tego fragmentu.
     *
     * @param inflater           Obiekt używany do rozwijania plików XML do widoków.
     * @param container          Rodzic, do którego zostanie dodany interfejs użytkownika fragmentu.
     * @param savedInstanceState Stan zapisany wcześniej (jeśli dotyczy).
     * @return Widok interfejsu użytkownika dla fragmentu.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    /**
     * Wywoływana po utworzeniu widoku fragmentu.
     * Inicjalizuje komponenty interfejsu użytkownika oraz przypisuje akcje do przycisków:
     * - Wybór zdjęcia z galerii
     * - Rozpoczęcie nagrywania dźwięku
     * - Zatrzymanie nagrywania
     * - Odtworzenie nagranego dźwięku
     * - Wysłanie posta
     *
     * @param view Widok fragmentu po rozwinięciu z pliku XML.
     * @param savedInstanceState Stan zapisany wcześniej (jeśli dotyczy).
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicjalizacja widoków
        descriptionField = view.findViewById(R.id.editTextDescription);
        uploadPhotoButton = view.findViewById(R.id.buttonUploadPhoto);
        recordVoiceButton = view.findViewById(R.id.buttonRecordVoice);
        playAudioButton = view.findViewById(R.id.buttonPlayAudio);
        submitPostButton = view.findViewById(R.id.buttonSubmitPost);
        stopRecordingButton = view.findViewById(R.id.buttonStopRecording); // Upewnij się, że przycisk istnieje w layoucie

        // Obsługa przycisków
        uploadPhotoButton.setOnClickListener(v -> openGallery());         // Otwórz galerię zdjęć
        recordVoiceButton.setOnClickListener(v -> startAudioRecording()); // Rozpocznij nagrywanie
        stopRecordingButton.setOnClickListener(v -> stopAudioRecording()); // Zatrzymaj nagrywanie
        playAudioButton.setOnClickListener(v -> playRecordedAudio());     // Odtwórz nagranie
        submitPostButton.setOnClickListener(v -> submitPost());           // Wyślij post
    }


    /**
     * Otwiera aplikację galerii urządzenia w celu wybrania zdjęcia.
     * Wykorzystuje intencję ACTION_PICK z URI do zewnętrznej pamięci (zdjęcia).
     * Wynik operacji odbierany jest w metodzie onActivityResult().
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    /**
     * Przesyła nowy post do serwera przy użyciu API.
     * Post może zawierać treść, opcjonalnie zdjęcie oraz nagranie dźwiękowe.
     *
     * <p>Etapy działania:
     * <ol>
     *     <li>Walidacja treści (nie może być pusta)</li>
     *     <li>Sprawdzenie obecności tokenu uwierzytelniającego w SharedPreferences</li>
     *     <li>Przygotowanie części żądania (JSON, obraz, audio)</li>
     *     <li>Wywołanie endpointu API przy użyciu Retrofit</li>
     *     <li>Obsługa odpowiedzi lub błędów</li>
     * </ol>
     */
    private void submitPost() {
        // Pobranie i walidacja treści opisu posta
        String content = descriptionField.getText().toString().trim();
        if (content.isEmpty()) {
            descriptionField.setError("Content cannot be empty");
            return;
        }

        // Odczyt tokenu autoryzacyjnego z preferencji użytkownika
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to create a post", Toast.LENGTH_SHORT).show();
            return;
        }

        // Utworzenie obiektu JSON z treścią posta
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent(content);
        RequestBody postRequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                new Gson().toJson(createPostRequest)
        );

        // Opcjonalnie: przygotowanie części obrazka, jeśli został wybrany
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            imagePart = prepareFilePart("image", selectedImageUri);
        }

        // Opcjonalnie: przygotowanie części audio, jeśli zostało nagrane
        MultipartBody.Part audioPart = null;
        if (recordedAudioUri != null) {
            audioPart = prepareFilePart("audio", recordedAudioUri);
        }

        // Inicjalizacja klienta API i wysyłka żądania
        ApiService apiService = ApiClient.getApiService();
        Call<PostModel> call = apiService.createPost("Bearer " + authToken, postRequestBody, imagePart, audioPart);

        // Obsługa odpowiedzi z API
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    Log.i("CreatePost", "Post created successfully");
                    descriptionField.setText(""); // Wyczyść pole opisu po sukcesie
                } else {
                    Log.e("CreatePost", "Failed to create post. HTTP Status Code: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("CreatePost", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("CreatePost", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                // Obsługa błędu połączenia lub innych wyjątków
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Przygotowuje część pliku do przesłania w żądaniu multipart/form-data.
     * Odczytuje zawartość pliku z podanego URI, konwertuje na tablicę bajtów,
     * a następnie tworzy obiekt MultipartBody.Part, który można przesłać w Retrofit.
     *
     * @param partName Nazwa części formularza (np. "image" lub "audio").
     * @param fileUri  URI pliku do przesłania.
     * @return MultipartBody.Part zawierający dane pliku lub null, jeśli wystąpił błąd odczytu.
     */
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(fileUri);
            byte[] fileBytes = readBytesFromInputStream(inputStream);

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileBytes);
            return MultipartBody.Part.createFormData(partName, "file", requestBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Odczytuje wszystkie bajty z podanego InputStream i zwraca je jako tablicę bajtów.
     *
     * @param inputStream Strumień wejściowy do odczytania.
     * @return Tablica bajtów z odczytanymi danymi.
     * @throws IOException W przypadku problemów z odczytem strumienia.
     */
    private byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * Obsługuje wyniki zwracane z innych aktywności uruchomionych za pomocą startActivityForResult.
     *
     * <p>Sprawdza, czy wynik pochodzi z wyboru zdjęcia z galerii lub nagrania audio i odpowiednio
     * zapisuje URI wybranego pliku oraz aktualizuje interfejs użytkownika.
     *
     * @param requestCode Kod żądania użyty przy wywołaniu startActivityForResult.
     * @param resultCode Kod rezultatu zwrócony przez aktywność (np. RESULT_OK).
     * @param data       Intent zawierający dane zwrócone z aktywności (np. URI pliku).
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Pobierz URI wybranego zdjęcia
            if (selectedImageUri != null) {
                ImageView imageView = getView().findViewById(R.id.selectedImageView); // Podgląd zdjęcia w UI
                imageView.setImageURI(selectedImageUri);
            }
        }
        if (requestCode == AUDIO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            recordedAudioUri = data.getData(); // Pobierz URI nagranego audio
            Toast.makeText(requireContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Rozpoczyna nagrywanie dźwięku z mikrofonu.
     *
     * <p>Sprawdza uprawnienia do nagrywania audio, jeśli ich brak to prosi użytkownika o ich przyznanie.
     * Po uzyskaniu uprawnień inicjalizuje MediaRecorder, tworzy tymczasowy plik do zapisu i rozpoczyna nagrywanie.
     * Informuje użytkownika o stanie nagrywania oraz odpowiednio pokazuje/ukrywa przyciski sterujące nagrywaniem.
     */
    private void startAudioRecording() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Brak uprawnień - prośba o przyznanie
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE_PERMISSION);
        } else {
            try {
                // Utworzenie tymczasowego pliku na nagranie w katalogu cache aplikacji
                File outputDir = requireContext().getCacheDir();
                audioFile = File.createTempFile("audio_record", ".3gp", outputDir);

                // Konfiguracja MediaRecorder
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

                // Start nagrywania
                mediaRecorder.prepare();
                mediaRecorder.start();

                Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_SHORT).show();

                // Aktualizacja widoczności przycisków
                stopRecordingButton.setVisibility(View.VISIBLE);
                recordVoiceButton.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to start recording", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Odtwarza nagrane wcześniej audio, jeśli URI do pliku audio jest dostępne.
     *
     * <p>Jeśli MediaPlayer nie został jeszcze zainicjalizowany, tworzy nową instancję, ustawia źródło audio
     * na nagrany plik i przygotowuje odtwarzacz. Następnie rozpoczyna odtwarzanie.
     * W przypadku błędów informuje użytkownika o problemie.
     * Jeśli brak nagranego audio, wyświetla komunikat o braku pliku do odtworzenia.
     */
    private void playRecordedAudio() {
        if (recordedAudioUri != null) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(recordedAudioUri.getPath());
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
                Toast.makeText(requireContext(), "Playing audio", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to play audio", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No audio recorded", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Zwalnia zasoby MediaPlayer przy niszczeniu fragmentu.
     *
     * <p>Metoda wywoływana automatycznie w cyklu życia fragmentu.
     * Zapewnia zwolnienie zasobów odtwarzacza multimediów, aby zapobiec wyciekom pamięci.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Obsługuje wynik zapytania o uprawnienia systemowe.
     *
     * <p>Sprawdza, czy żądanie dotyczy uprawnienia do mikrofonu. Jeśli uprawnienia zostały
     * przyznane, wywołuje metodę uruchamiającą nagrywarkę dźwięku. W przeciwnym razie
     * informuje użytkownika o konieczności przyznania uprawnień.
     *
     * @param requestCode  Kod żądania uprawnień.
     * @param permissions  Tablica żądanych uprawnień.
     * @param grantResults Tablica wyników przyznania uprawnień.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MICROPHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienia zostały nadane
                launchAudioRecorder();
            } else {
                // Uprawnienia zostały odrzucone
                Toast.makeText(requireContext(), "Microphone permission is required to record audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Uruchamia zewnętrzną aplikację do nagrywania dźwięku.
     *
     * <p>Sprawdza, czy na urządzeniu jest dostępna aplikacja obsługująca akcję nagrywania dźwięku.
     * Jeśli tak, wywołuje ją za pomocą startActivityForResult, w przeciwnym razie wyświetla
     * stosowny komunikat dla użytkownika.
     */
    private void launchAudioRecorder() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, AUDIO_REQUEST_CODE);
        } else {
            Toast.makeText(requireContext(), "No app available to record audio", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Zatrzymuje nagrywanie audio i zapisuje nagrany plik.
     *
     * <p>Jeśli MediaRecorder jest aktywny, zatrzymuje nagrywanie, zwalnia zasoby i ustawia
     * URI do nagranego pliku audio. Informuje użytkownika o ścieżce zapisanego pliku.
     * Aktualizuje widoczność przycisków sterujących nagrywaniem.
     */
    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            // Zapisz URI nagranego audio
            recordedAudioUri = Uri.fromFile(audioFile);
            Toast.makeText(requireContext(), "Recording saved: " + audioFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            // Aktualizacja widoczności przycisków
            stopRecordingButton.setVisibility(View.GONE);
            recordVoiceButton.setVisibility(View.VISIBLE);
        }
    }

}
