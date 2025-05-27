package com.example.appreceitas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, senhaEditText;
    private MaterialButton loginButton;
    private TextView textViewCadastro;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        loginButton = findViewById(R.id.loginButton);
        textViewCadastro = findViewById(R.id.textViewCadastro);

        loginButton.setOnClickListener(v -> realizarLogin());

        textViewCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(this, Cadastro.class);
            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.verificarEmail(email)) {
            Toast.makeText(this, "Email não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.verificarSenhaCorreta(email, senha)) {
            Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, GaleriaReceitasActivity.class);
        intent.putExtra("email_usuario", email);
        startActivity(intent);
        finish();
    }
}
