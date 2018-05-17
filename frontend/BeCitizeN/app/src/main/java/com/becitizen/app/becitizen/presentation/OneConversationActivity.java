package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.domain.entities.Message;

import java.util.List;

public class OneConversationActivity extends Fragment {

    private View rootView;

    private RecyclerView mMessageRecycler;
    private ConversationAdapter mMessageAdapter;

    private Conversation conversation;
    private List<Message> messageList;

    public OneConversationActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_one_conversation, container, false);

        conversation = ControllerMsgPresentation.getInstance().getConversation();
        messageList = conversation.getMessages();

        Log.i("MESSAGES", messageList.toString());

        mMessageRecycler = (RecyclerView) rootView.findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new ConversationAdapter(getContext(), messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);

        return rootView;
    }
}
