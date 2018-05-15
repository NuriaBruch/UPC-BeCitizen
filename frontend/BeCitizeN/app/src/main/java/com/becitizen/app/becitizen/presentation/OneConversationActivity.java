package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Message;

import java.util.List;

public class OneConversationActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private ConversationAdapter mMessageAdapter;

    List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_conversation);

        //messageList = ControllerMsgPresentation.getConversation();

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new ConversationAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
