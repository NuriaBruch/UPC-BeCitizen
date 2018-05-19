package com.becitizen.app.becitizen.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllConversationsActivity extends Fragment {

    public static final String EXTRA_TAB_NAME = "tab_name";
    private String mTabName;

    public AllConversationsActivity() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_all_conversations, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        getUserChats();
    }

    private void initViews()
    {
        initRecyclerView();

    }

    private RecyclerView mRecyclerView;
    private ConversationAdapter mAdapter;

    private void initRecyclerView()
    {
        mRecyclerView= (RecyclerView) getView().findViewById(R.id.chatsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getUserChats()
    {

        List<Conversation> conversations = new ArrayList<>();
        conversations.add(new Conversation(1, 1, "ram", new Date(18,5,19,12,45, 30)));
        conversations.add(new Conversation(2, 2, "Cr0s4k", new Date(18,5,19,9,45, 30)));
        conversations.add(new Conversation(3, 3, "alioli", new Date(18,5,18,18,45, 30)));
        conversations.add(new Conversation(4, 4, "eloiiroca", new Date(18,5,18,16,45, 30)));
        conversations.add(new Conversation(5, 5, "nico222", new Date(18,5,18,10,45, 30)));
        conversations.add(new Conversation(6, 6, "bun", new Date(18,5,17,20,45, 30)));


        mAdapter = new ConversationAdapter(getContext(), conversations);
        mRecyclerView.setAdapter(mAdapter);
    }
}
