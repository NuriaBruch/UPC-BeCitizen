package com.becitizen.app.becitizen.presentation;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.becitizen.app.becitizen.domain.entities.Thread;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Comment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ThreadActivity extends Fragment {
    private View rootView;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private int threadId;
    ControllerThreadPresentation controllerThreadPresentation;

    TextView threadAuthor, threadTime, threadContent, threadTitle, threadVotes, threadAuthorRank;
    ImageButton threadVote, threadReport, threadAuthorImage;
    EditText newCommentText;
    ImageButton newCommentButton;

    public ThreadActivity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.thread_content, container, false);

        controllerThreadPresentation = ControllerThreadPresentation.getUniqueInstance();

        recyclerView = rootView.findViewById(R.id.recyclerView);

        prepareContent();

        setCommentList();


        threadVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerThreadPresentation.voteThread(threadId);
            }
        });

        threadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerThreadPresentation.reportThread(threadId);
            }
        });

        threadAuthorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("loggeduser", false);
                bundle.putString("username", threadAuthor.getText().toString());
                Fragment fragment = new UserProfile();
                fragment.setArguments(bundle);
                fragmentTransaction(fragment, "USER_PROFILE");
            }
        });

        newCommentText = rootView.findViewById(R.id.newCommentInput);
        newCommentButton = rootView.findViewById(R.id.newCommentButton);
        newCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = newCommentText.getText().toString().trim();
                if (commentText.isEmpty())
                    Snackbar.make(view, "Your reply is empty", Snackbar.LENGTH_LONG).show();
                else {
                    controllerThreadPresentation.newComment(commentText, threadId);
                    prepareComments();
                }
            }
        });

        return rootView;
    }

    private void setCommentList() {
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(rootView.getContext(), commentList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareComments();
    }

    private void prepareContent() {
        try {
            Thread thread = controllerThreadPresentation.getThreadContent(threadId);

            threadAuthor = rootView.findViewById(R.id.threadAuthorText);
            threadAuthor.setText(thread.getAuthor());

            threadTime = rootView.findViewById(R.id.threadTimeText);
            threadTime.setText(thread.getCreatedAt());

            threadContent = rootView.findViewById(R.id.threadContentText);
            threadContent.setText(thread.getContent());

            threadTitle = rootView.findViewById(R.id.threadTitleText);
            threadTitle.setText(thread.getTitle());

            threadVotes = rootView.findViewById(R.id.threadVotesText);
            threadVotes.setText(String.valueOf(thread.getVotes()));

            threadAuthorRank = rootView.findViewById(R.id.threadAuthorRankText);
            threadAuthorRank.setText(thread.getAuthorRank());

            threadVote = rootView.findViewById(R.id.threadVoteButton);
            if (!thread.isCanVote()) {
                threadVote.setImageResource(R.drawable.ic_voted_icon);
                threadVote.setEnabled(false);
            }
            threadReport = rootView.findViewById(R.id.threadReportButton);
            if (!thread.isCanReport()) {
                threadReport.setImageResource(R.drawable.ic_reported);
                threadReport.setEnabled(false);
            }

            setAuthorImage(thread.getAuthorImage());

        }
        catch (JSONException e) {
            //TODO TOAST OR LOG THAT JSON BROKE MY PROGRAM, TY.
            //TODO RETURN TO THE LIST OF THREADS PLS
        }

    }

    private void prepareComments () {
        try {
            List<Comment> newCommentList = controllerThreadPresentation.getThreadComments(threadId);
            commentList.addAll(newCommentList);
            adapter.notifyDataSetChanged();
        }
        catch (JSONException e) {
            // TODO show toast
        }
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void setAuthorImage(int number) {
        switch (number) {
            case 1:
                threadAuthorImage.setImageResource(R.drawable.userprofile1);
                break;

            case 2:
                threadAuthorImage.setImageResource(R.drawable.userprofile2);
                break;

            case 3:
                threadAuthorImage.setImageResource(R.drawable.userprofile3);
                break;

            case 4:
                threadAuthorImage.setImageResource(R.drawable.userprofile4);
                break;

            case 5:
                threadAuthorImage.setImageResource(R.drawable.userprofile5);
                break;

            case 6:
                threadAuthorImage.setImageResource(R.drawable.userprofile6);
                break;

            case 7:
                threadAuthorImage.setImageResource(R.drawable.userprofile7);
                break;

            case 8:
                threadAuthorImage.setImageResource(R.drawable.userprofile8);
                break;

            default:
                threadAuthorImage.setImageResource(R.drawable.userprofile1);
                break;
        }
    }

}
