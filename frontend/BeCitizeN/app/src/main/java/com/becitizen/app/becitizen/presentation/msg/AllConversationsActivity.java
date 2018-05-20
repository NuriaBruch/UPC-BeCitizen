package com.becitizen.app.becitizen.presentation.msg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.exceptions.ServerException;

import java.util.ArrayList;
import java.util.List;

public class AllConversationsActivity extends Fragment {

    public static final String EXTRA_TAB_NAME = "tab_name";
    private String mTabName;

    View rootView;

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
        rootView = inflater.inflate(R.layout.activity_all_conversations, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        List<Conversation> conversations = null;
        try {
            conversations = ControllerMsgPresentation.getInstance().getConversations();
        } catch (ServerException e) {
            e.printStackTrace();
            conversations = new ArrayList<>();
            Toast.makeText(rootView.getContext(), getContext().getResources().getString(R.string.serverError), Toast.LENGTH_LONG).show();
        }
        mAdapter = new ConversationAdapter(getContext(), conversations);
        mRecyclerView.setAdapter(mAdapter);
    }

    private RecyclerView mRecyclerView;
    private ConversationAdapter mAdapter;

    private void initRecyclerView()
    {
        mRecyclerView= (RecyclerView) getView().findViewById(R.id.chatsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
