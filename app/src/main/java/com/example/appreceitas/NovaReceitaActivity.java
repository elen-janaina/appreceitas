package com.example.appreceitas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class NovaReceitaActivity extends AppCompatActivity {

    private EditText tituloEditText, ingredientesEditText, recheioEditText, preparoEditText;
    private ImageView imagemReceitaImageView;
    private Button salvarButton, selecionarImagemButton;
    private String imagemUriSelecionada = null;
    private DatabaseHelper dbHelper;
    private long loggedInUserId;

    private final ActivityResultLauncher<String> seletorImagemLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagemUriSelecionada = uri.toString();
                    imagemReceitaImageView.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_receita);

        tituloEditText = findViewById(R.id.inputTitulo);
        ingredientesEditText = findViewById(R.id.inputIngredientes);
        recheioEditText = findViewById(R.id.inputRecheio);
        preparoEditText = findViewById(R.id.inputPreparo);
        imagemReceitaImageView = findViewById(R.id.imagemReceita);
        selecionarImagemButton = findViewById(R.id.btnSelecionarImagem);
        salvarButton = findViewById(R.id.btnSalvar);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        loggedInUserId = preferences.getLong("logged_in_user_id", -1);

        if (loggedInUserId == -1) {
            // Se não houver userId logado, redirecionar para a tela de login
            Intent intent = new Intent(NovaReceitaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        selecionarImagemButton.setOnClickListener(v -> {
            seletorImagemLauncher.launch("image/*");
        });

        salvarButton.setOnClickListener(v -> {
            String titulo = tituloEditText.getText().toString().trim();
            String ingredientes = ingredientesEditText.getText().toString().trim();
            String recheio = recheioEditText.getText().toString().trim();
            String preparo = preparoEditText.getText().toString().trim();

            if (titulo.isEmpty() || ingredientes.isEmpty() || preparo.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            Receita novaReceita = new Receita(titulo, ingredientes, recheio, preparo, imagemUriSelecionada);

            long newRecipeId = dbHelper.adicionarReceita(loggedInUserId, novaReceita);

            if (newRecipeId != -1) {
                Toast.makeText(this, "Receita salva com sucesso!", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar receita!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


