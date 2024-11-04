package fr.benjosss.nothinggalleryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int span_size = 3;
    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> mediaFiles;
    TextView gallery_number;
    ImageButton span_size_btn;

    private static final int MY_READ_MEDIA_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery_number = findViewById(R.id.gallery_number);
        recyclerView = findViewById(R.id.recyclerview_gallery_image);
        span_size_btn = findViewById(R.id.span_size_btn);

        // Vérifier les permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
                        MY_READ_MEDIA_PERMISSION_CODE);
            } else {
                loadMedia(span_size);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_READ_MEDIA_PERMISSION_CODE);
            } else {
                loadMedia(span_size);
            }
        }

        // Ajouter un écouteur de clic pour changer le nombre de colonnes
        span_size_btn.setOnClickListener(v -> changeSpanSize());
    }

    @SuppressLint("SetTextI18n")
    private void loadMedia(int span_size) {
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, span_size));

        mediaFiles = ImageGallery.listOfMedia(this); // Obtenez images et vidéos
        galleryAdapter = new GalleryAdapter(this, mediaFiles, path -> Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show());

        recyclerView.setAdapter(galleryAdapter);
        gallery_number.setText(mediaFiles.size() + " fichiers");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_READ_MEDIA_PERMISSION_CODE) {
            boolean permissionGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = false;
                    break;
                }
            }
            if (permissionGranted) {
                Toast.makeText(this, "Autorisation d'accès aux photos et vidéos accordée", Toast.LENGTH_SHORT).show();
                loadMedia(span_size);
            } else {
                Toast.makeText(this, "Autorisation d'accès aux photos et vidéos refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Fonction pour changer le nombre de colonnes
    private void changeSpanSize() {
        // Alterner entre 3 et 4 colonnes
        span_size = (span_size == 3) ? 4 : 3;

        // Recharger le média avec le nouveau span_size
        loadMedia(span_size);

        // Message pour l'utilisateur
        Toast.makeText(this, "Nombre de colonnes : " + span_size, Toast.LENGTH_SHORT).show();
    }
}
