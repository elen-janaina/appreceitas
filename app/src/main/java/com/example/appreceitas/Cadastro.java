package com.example.appreceitas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Cadastro extends AppCompatActivity {

    private EditText nomeEditText, emailEditText, senhaEditText;
    private MaterialButton cadastrarButton;
    private TextView loginLinkTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbHelper = new DatabaseHelper(this);

        nomeEditText = findViewById(R.id.nomeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        cadastrarButton = findViewById(R.id.cadastrarButton);
        loginLinkTextView = findViewById(R.id.loginLinkTextView);

        cadastrarButton.setOnClickListener(v -> {
            String nome = nomeEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.verificarEmail(email)) {
                Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                long userId = dbHelper.cadastrarUsuario(nome, email, senha);
                if (userId != -1) {
                    Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    // Salvar o userId nas SharedPreferences para uso futuro
                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("logged_in_user_id", userId);
                    editor.apply();

                    startActivity(new Intent(Cadastro.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginLinkTextView.setOnClickListener(v -> {
            startActivity(new Intent(Cadastro.this, MainActivity.class));
            finish();
        });
    }
}


