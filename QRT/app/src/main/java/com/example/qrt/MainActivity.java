package com.example.qrt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrt.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button buttonSelect;
    private Button buttonCopy;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSelect = findViewById(R.id.button_select);
        buttonCopy = findViewById(R.id.button_copy);
        textViewResult = findViewById(R.id.text_view_result);

        // ZXing integrator'ü başlat
        IntentIntegrator integrator = new IntentIntegrator(this);

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ZXing QR kodu tarayıcısını başlat
                integrator.initiateScan();
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // QR kodu içeriğini panoya kopyala
                String qrContent = textViewResult.getText().toString();
                if (!qrContent.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("QR Kodu", qrContent);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "QR kodu içeriği kopyalandı", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "QR kodu içeriği bulunamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            // ZXing QR kodu tarayıcısından gelen sonucu işle
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    // QR kodu içeriği bulundu
                    textViewResult.setText(result.getContents());
                } else {
                    // QR kodu bulunamadı
                    textViewResult.setText("QR kodu bulunamadı");
                }
            } else {
                // QR kodu tarayıcıdan geçersiz bir sonuç döndü
                textViewResult.setText("QR kodu tarayıcıdan geçersiz bir sonuç döndü");
            }
        }
    }


}
