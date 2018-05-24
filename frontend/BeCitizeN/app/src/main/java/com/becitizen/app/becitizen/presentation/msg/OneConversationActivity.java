package com.becitizen.app.becitizen.presentation.msg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;

import java.util.Date;

public class OneConversationActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;

    private ImageView profilePictureView;
    private TextView usernameView;

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

        /* EXTRAS

          "id": 0,
          "username": "string",
          "profilePicture": 0,
          "lastMessageTime": "string"
         */

        int conversationId = getIntent().getExtras().getInt("id", -1);
        String username = getIntent().getExtras().getString("username", null);
        int profilePicture = getIntent().getExtras().getInt("profilePicture", -1);
        String lastTime = getIntent().getExtras().getString("lastMessageTime", null);
        Date lastMessageTime = new Date(lastTime);

        conversation = new Conversation(conversationId, profilePicture, username, lastMessageTime);
        Log.d("Conversation", conversation.toString());

        // TODO: set username, profilePicture, lastMessage while loading
        if (profilePicture >= 1 && profilePicture <= 8) profilePictureView.setImageResource(getResources().getIdentifier("userprofile" + profilePicture, "drawable", null));
        if (username != null) usernameView.setText(username);

        if (conversationId != -1) {
            conversation.setMessages(ControllerMsgPresentation.getInstance().getMessages(conversationId));

            mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
            mMessageAdapter = new MessageAdapter(this, conversation.getMessages());
            mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
            mMessageRecycler.setAdapter(mMessageAdapter);
        } else {
            // TODO: Error
        }

        // TODO: send new message
    }
}
