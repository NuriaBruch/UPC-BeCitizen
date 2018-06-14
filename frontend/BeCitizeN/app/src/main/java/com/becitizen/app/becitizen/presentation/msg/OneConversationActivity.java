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
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.SideMenuActivity;
import com.becitizen.app.becitizen.presentation.controllers.ControllerMsgPresentation;

import de.hdodenhof.circleimageview.CircleImageView;

public class OneConversationActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;

    private CircleImageView profilePictureView;
    private TextView nameView, usernameView;
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Toolbar","Clicked");
                goBack();
            }
        });

        profilePictureView = (CircleImageView) findViewById(R.id.image_message_profile);
        nameView = (TextView) findViewById(R.id.text_message_name);
        usernameView = (TextView) findViewById(R.id.text_message_username);
        appbar = (Toolbar) findViewById(R.id.toolbar_conv);

        int conversationId = getIntent().getExtras().getInt("id", -1);
        String name = getIntent().getExtras().getString("name", null);
        String username = getIntent().getExtras().getString("username", null);
        int profilePicture = getIntent().getExtras().getInt("profilePicture", -1);

        conversation = new Conversation(conversationId, name, username, profilePicture);
        Log.d("Conversation", conversation.toString());

        if (profilePicture >= 1 && profilePicture <= 8) profilePictureView.setImageResource(getImageId(profilePicture));
        if (name != null) nameView.setText(name);
        if (username != null) {
            usernameView.setText("@" + username);
            appbar.setOnClickListener(this);
        }

        if (conversationId != -1) {
            try {
                loadComments(conversationId);
            } catch (ServerException e) {
                e.printStackTrace();
                Toast.makeText(this, getResources().getString(R.string.errorLoadMessages), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.errorLoadMessages), Toast.LENGTH_LONG).show();
        }

        sendButton = (Button) findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(this);
        sendText = (EditText) findViewById(R.id.edittext_chatbox);
    }

    private void goBack() {
        super.onBackPressed();
    }

    private void loadComments(int conversationId) throws ServerException {
        conversation.setMessages(ControllerMsgPresentation.getInstance().getMessages(conversationId));
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageAdapter(this, conversation.getMessages());
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        Log.d("DEBUG", conversation.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_conv:
                viewProfile();
                break;
            case R.id.button_chatbox_send:
                if (sendText.getText().length()>0) sendMessage();
        }
    }

    private void sendMessage() {
        ControllerMsgPresentation.getInstance().newMessage(conversation.getId(), sendText.getText().toString());
        sendText.setText("");
        try {
            loadComments(conversation.getId());
        } catch (ServerException e) {
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.errorLoadMessages), Toast.LENGTH_LONG).show();
        }
    }

    private void viewProfile() {
        Intent i = new Intent(this, SideMenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "UserProfileActivity");
        bundle.putBoolean("loggeduser", false);
        bundle.putString("username", conversation.getUserName());
        i.putExtras(bundle);
        startActivity(i);
    }

    private int getImageId(int number) {
        switch (number) {
            case 1:
                return R.drawable.userprofile1;
            case 2:
                return R.drawable.userprofile2;
            case 3:
                return R.drawable.userprofile3;
            case 4:
                return R.drawable.userprofile4;
            case 5:
                return R.drawable.userprofile5;
            case 6:
                return R.drawable.userprofile6;
            case 7:
                return R.drawable.userprofile7;
            case 8:
                return R.drawable.userprofile8;
            default:
                return R.drawable.userprofile1;
        }
    }
}
