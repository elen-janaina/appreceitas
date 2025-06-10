package com.example.appreceitas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GaleriaReceitasActivity extends AppCompatActivity {

    private static final int REQUEST_NOVA_RECEITA = 1001;

    EditText searchInput;
    ImageView searchButton;
    private List<Receita> receitasFiltradas;

    ImageButton fab;
    LinearLayout recipeGallery;

    private long loggedInUserId;
    private DatabaseHelper dbHelper;
    private List<Receita> receitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_receitas);

        fab = findViewById(R.id.fab);
        recipeGallery = findViewById(R.id.recipeGallery);
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        loggedInUserId = preferences.getLong("logged_in_user_id", -1);

        if (loggedInUserId == -1) {
            Intent intent = new Intent(GaleriaReceitasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        loadAndDisplayRecipes();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(GaleriaReceitasActivity.this, NovaReceitaActivity.class);
            startActivityForResult(intent, REQUEST_NOVA_RECEITA);
        });

        searchButton.setOnClickListener(v -> {
            String texto = searchInput.getText().toString().toLowerCase();
            filtrarReceitas(texto);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar as receitas sempre que a atividade for retomada
        loadAndDisplayRecipes();
    }

    private void loadAndDisplayRecipes() {
        receitas = dbHelper.getReceitasPorUsuario(loggedInUserId);
        if (receitas == null) {
            receitas = new ArrayList<>();
        }
        receitasFiltradas = new ArrayList<>(receitas);
        recipeGallery.removeAllViews(); // Limpar a galeria antes de recarregar
        for (Receita r : receitasFiltradas) {
            addReceitaCard(r);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NOVA_RECEITA && resultCode == RESULT_OK) {
            // Não é necessário lidar com o 'nova_receita' aqui, pois onResume() irá recarregar
            // a lista do banco de dados.
        }
    }

    private void addReceitaCard(Receita receita) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        int padding = dpToPx(12);
        card.setPadding(padding, padding, padding, padding);
        card.setBackgroundResource(R.drawable.card_background);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dpToPx(16));
        card.setLayoutParams(params);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(180)));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (receita.getImagemUri() != null && !receita.getImagemUri().isEmpty()) {
            Uri uri = Uri.parse(receita.getImagemUri());
            Glide.with(this).load(uri).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_placeholder);
        }
        card.addView(imageView);

        TextView titulo = new TextView(this);
        titulo.setText(receita.getTitulo());
        titulo.setTextSize(20f);
        titulo.setPadding(0, dpToPx(8), 0, dpToPx(8));
        titulo.setTextColor(getResources().getColor(android.R.color.black));
        card.addView(titulo);

        TextView detalhes = new TextView(this);
        detalhes.setText(
                "Ingredientes:\n" + receita.getIngredientes() +
                        "\n\nRecheio:\n" + receita.getRecheio() +
                        "\n\nModo de Preparo:\n" + receita.getPreparo()
        );
        detalhes.setVisibility(View.GONE);
        detalhes.setPadding(0, 0, 0, dpToPx(8));
        card.addView(detalhes);

        card.setOnClickListener(v -> {
            if (detalhes.getVisibility() == View.GONE) {
                detalhes.setVisibility(View.VISIBLE);
            } else {
                detalhes.setVisibility(View.GONE);
            }
        });

        recipeGallery.addView(card);
    }

    private void filtrarReceitas(String texto) {
        receitasFiltradas.clear();
        for (Receita r : receitas) {
            if (r.getTitulo().toLowerCase().contains(texto)) {
                receitasFiltradas.add(r);
            }
        }

        recipeGallery.removeAllViews();
        for (Receita r : receitasFiltradas) {
            addReceitaCard(r);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}

