package com.example.appreceitas;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReceitaStorage {

    private Context context;

    public ReceitaStorage(Context context) {
        this.context = context;
    }

    public void salvarReceitas(String emailUsuario, List<Receita> receitas) {
        Gson gson = new Gson();
        String json = gson.toJson(receitas);
        String filename = "receitas_" + emailUsuario + ".json";

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Receita> carregarReceitas(String emailUsuario) {
        String filename = "receitas_" + emailUsuario + ".json";
        List<Receita> receitas = new ArrayList<>();

        try (FileInputStream fis = context.openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha);
            }

            String json = sb.toString();

            Type tipoLista = new TypeToken<ArrayList<Receita>>(){}.getType();
            receitas = new Gson().fromJson(json, tipoLista);

            if (receitas == null) {
                receitas = new ArrayList<>();
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receitas;
    }
}
