package io.github.project_travel_mate.tripdiary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.project_travel_mate.R;

public class TripDiaryActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1001;

    private RecyclerView mRecyclerView;
    private TripDiaryEntryAdapter mAdapter;
    private List<TripDiaryEntry> mEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_diary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(getString(R.string.trip_diary));

        mRecyclerView = findViewById(R.id.diary_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TripDiaryEntryAdapter(this, mEntries);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.add_entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        findViewById(R.id.export_pdf_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportPdf();
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            final Uri imageUri = data.getData();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.add_review);

            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();
                    mEntries.add(new TripDiaryEntry(imageUri, text));
                    mAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
        }
    }

    private void exportPdf() {
        if (mEntries.isEmpty()) {
            Toast.makeText(this, R.string.no_entries, Toast.LENGTH_SHORT).show();
            return;
        }
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        int pageNumber = 1;
        for (TripDiaryEntry entry : mEntries) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), entry.getImageUri());
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 400, 300, true);
                canvas.drawBitmap(scaled, 50, 50, paint);
            } catch (IOException e) {
                // ignore
            }
            paint.setTextSize(14f);
            canvas.drawText(entry.getText(), 50, 380, paint);
            document.finishPage(page);
            pageNumber++;
        }
        File pdfDir = new File(Environment.getExternalStorageDirectory(), "TravelMate");
        if (!pdfDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            pdfDir.mkdirs();
        }
        File file = new File(pdfDir, "TripDiary.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, getString(R.string.pdf_saved, file.getAbsolutePath()), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_pdf, Toast.LENGTH_SHORT).show();
        }
        document.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TripDiaryActivity.class);
    }
}
