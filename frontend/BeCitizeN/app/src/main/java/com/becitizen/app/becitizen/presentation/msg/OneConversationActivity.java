package com.becitizen.app.becitizen.presentation.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.SideMenuActivity;

import java.util.Date;

public class OneConversationActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;

    private ImageView profilePictureView;
    private TextView usernameView;
    private Toolbar appbar;
    private Button sendButton;
    private EditText sendText;

    private Conversation conversation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_conversation);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chats);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Toolbar","Clicked");
            }
        });

        profilePictureView = (ImageView) findViewById(R.id.image_message_profile);
        usernameView = (TextView) findViewById(R.id.text_message_name);
        appbar = (Toolbar) findViewById(R.id.toolbar_conv);

        int conversationId = getIntent().getExtras().getInt("id", -1);
        String username = getIntent().getExtras().getString("username", null);
        int profilePicture = getIntent().getExtras().getInt("profilePicture", -1);
        String lastTime = getIntent().getExtras().getString("lastMessageTime", null);
        Date lastMessageTime = new Date(lastTime);

        conversation = new Conversation(conversationId, profilePicture, username, lastMessageTime);
        Log.d("Conversation", conversation.toString());

        // TODO: set username, profilePicture, lastMessage while loading
        if (profilePicture >= 1 && profilePicture <= 8) profilePictureView.setImageResource(getResources().getIdentifier("userprofile" + profilePicture, "drawable", null));
        if (username != null) {
            usernameView.setText(username);

            appbar.setOnClickListener(this);
        }



        if (conversationId != -1) {
            try {
                conversation.setMessages(ControllerMsgPresentation.getInstance().getMessages(conversationId));
                mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
                mMessageAdapter = new MessageAdapter(this, conversation.getMessages());
                mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
                mMessageRecycler.setAdapter(mMessageAdapter);
                Log.d("DEBUG", conversation.toString());
            } catch (ServerException e) {
                e.printStackTrace();
                Toast.makeText(this, getResources().getString(R.string.errorLoadMessages), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.errorLoadMessages), Toast.LENGTH_LONG).show();
        }

        // TODO: send new message
        sendButton = (Button) findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(this);
        sendText = (EditText) findViewById(R.id.edittext_chatbox);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_conv:
                viewProfile();
                break;
            case R.id.button_chatbox_send:
                sendMessage();
        }
    }

    private void sendMessage() {
        ControllerMsgPresentation.getInstance().newMessage(conversation.getId(), sendText.getText().toString());
        sendText.setText("");
    }

    private void viewProfile() {
        Intent i = new Intent(this, SideMenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "UserProfile");
        bundle.putBoolean("loggeduser", false);
        bundle.putString("username", conversation.getUserName());
        i.putExtras(bundle);
        startActivity(i);
    }
}
