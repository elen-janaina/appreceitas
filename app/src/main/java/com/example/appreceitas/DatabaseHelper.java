package com.example.appreceitas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usuarios_receitas.db";
    private static final int DATABASE_VERSION = 2; // Incrementando a versão do banco de dados

    // Tabela de Usuários
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NOME = "nome";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_SENHA = "senha";

    // Tabela de Receitas
    private static final String TABLE_RECEITAS = "receitas";
    private static final String COLUMN_RECEITA_ID = "id";
    private static final String COLUMN_RECEITA_USER_ID = "user_id";
    private static final String COLUMN_RECEITA_TITULO = "titulo";
    private static final String COLUMN_RECEITA_INGREDIENTES = "ingredientes";
    private static final String COLUMN_RECEITA_RECHEIO = "recheio";
    private static final String COLUMN_RECEITA_PREPARO = "preparo";
    private static final String COLUMN_RECEITA_IMAGEM_URI = "imagem_uri";

    // Criação da Tabela de Usuários
    private static final String CREATE_TABLE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + "(" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NOME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_SENHA + " TEXT)";

    // Criação da Tabela de Receitas
    private static final String CREATE_TABLE_RECEITAS =
            "CREATE TABLE " + TABLE_RECEITAS + "(" +
                    COLUMN_RECEITA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECEITA_USER_ID + " INTEGER, " +
                    COLUMN_RECEITA_TITULO + " TEXT, " +
                    COLUMN_RECEITA_INGREDIENTES + " TEXT, " +
                    COLUMN_RECEITA_RECHEIO + " TEXT, " +
                    COLUMN_RECEITA_PREPARO + " TEXT, " +
                    COLUMN_RECEITA_IMAGEM_URI + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_RECEITA_USER_ID + ") REFERENCES " +
                    TABLE_USUARIOS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_RECEITAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    // Métodos para Usuários
    public long cadastrarUsuario(String nome, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NOME, nome);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_SENHA, senha);
        long id = db.insert(TABLE_USUARIOS, null, values);
        db.close();
        return id;
    }

    public boolean verificarEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + "=?",
                new String[]{email},
                null, null, null
        );
        boolean emailExiste = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return emailExiste;
    }

    public long verificarSenhaCorreta(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        long userId = -1;
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_SENHA + "=?",
                new String[]{email, senha},
                null, null, null
        );
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }

    // Métodos para Receitas
    public long adicionarReceita(long userId, Receita receita) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEITA_USER_ID, userId);
        values.put(COLUMN_RECEITA_TITULO, receita.getTitulo());
        values.put(COLUMN_RECEITA_INGREDIENTES, receita.getIngredientes());
        values.put(COLUMN_RECEITA_RECHEIO, receita.getRecheio());
        values.put(COLUMN_RECEITA_PREPARO, receita.getPreparo());
        values.put(COLUMN_RECEITA_IMAGEM_URI, receita.getImagemUri());
        long id = db.insert(TABLE_RECEITAS, null, values);
        db.close();
        return id;
    }

    public List<Receita> getReceitasPorUsuario(long userId) {
        List<Receita> receitas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_RECEITAS,
                new String[]{COLUMN_RECEITA_TITULO, COLUMN_RECEITA_INGREDIENTES, COLUMN_RECEITA_RECHEIO, COLUMN_RECEITA_PREPARO, COLUMN_RECEITA_IMAGEM_URI},
                COLUMN_RECEITA_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEITA_TITULO));
                String ingredientes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEITA_INGREDIENTES));
                String recheio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEITA_RECHEIO));
                String preparo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEITA_PREPARO));
                String imagemUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEITA_IMAGEM_URI));
                receitas.add(new Receita(titulo, ingredientes, recheio, preparo, imagemUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return receitas;
    }

    public int atualizarReceita(Receita receita, long receitaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEITA_TITULO, receita.getTitulo());
        values.put(COLUMN_RECEITA_INGREDIENTES, receita.getIngredientes());
        values.put(COLUMN_RECEITA_RECHEIO, receita.getRecheio());
        values.put(COLUMN_RECEITA_PREPARO, receita.getPreparo());
        values.put(COLUMN_RECEITA_IMAGEM_URI, receita.getImagemUri());
        int rowsAffected = db.update(TABLE_RECEITAS, values, COLUMN_RECEITA_ID + "=?", new String[]{String.valueOf(receitaId)});
        db.close();
        return rowsAffected;
    }

    public int deletarReceita(long receitaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_RECEITAS, COLUMN_RECEITA_ID + "=?", new String[]{String.valueOf(receitaId)});
        db.close();
        return rowsAffected;
    }
}


