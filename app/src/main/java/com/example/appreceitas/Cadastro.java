package com.example.appreceitas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Cadastro extends AppCompatActivity {

    private EditText nomeEditText, emailEditText, dataNascimentoEditText, senhaEditText;
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
        dataNascimentoEditText = findViewById(R.id.dataNascimentoEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        cadastrarButton = findViewById(R.id.cadastrarButton);
        loginLinkTextView = findViewById(R.id.loginLinkTextView);

        cadastrarButton.setOnClickListener(v -> {
            String nome = nomeEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String dataNascimento = dataNascimentoEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || dataNascimento.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.verificarEmail(email)) {
                Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.cadastrarUsuario(nome, email, senha);
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cadastro.this, MainActivity.class));
                finish();
            }
        });

        loginLinkTextView.setOnClickListener(v -> {
            startActivity(new Intent(Cadastro.this, MainActivity.class));
            finish();
        });
    }
}
