package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import adapters.ChatMessageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import objects.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.message_list)
    RecyclerView mMessageList;
    @BindView(R.id.input_message)
    EditText mInputMessage;
    @BindView(R.id.button_send)
    ImageButton mButtonSend;
    @BindView(R.id.button_sos)
    ImageButton mButtonSos;

    private ChatMessageAdapter mAdapter;
    private final List<ChatMessage> mMessages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_chat);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mAdapter = new ChatMessageAdapter(mMessages);
        mMessageList.setLayoutManager(new LinearLayoutManager(this));
        mMessageList.setAdapter(mAdapter);
    }

    @OnClick(R.id.button_send)
    void onSendClicked() {
        String text = mInputMessage.getText().toString().trim();
        if (text.isEmpty()) {
            return;
        }
        addMessage(text, true);
        mInputMessage.setText("");
        respond(text);
    }

    @OnClick(R.id.button_sos)
    void onSosClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"));
        startActivity(intent);
    }

    private void respond(String text) {
        String lower = text.toLowerCase(Locale.getDefault());
        String response;
        if (lower.contains("eat")) {
            response = "Try looking for restaurants in the Travel tab.";
        } else if (lower.contains("hello")) {
            response = "Hello! How can I help you today?";
        } else {
            response = "Sorry, I don't have an answer for that right now.";
        }
        addMessage(response, false);
    }

    private void addMessage(String text, boolean fromUser) {
        mMessages.add(new ChatMessage(text, fromUser));
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        mMessageList.scrollToPosition(mMessages.size() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ChatActivity.class);
    }
}
