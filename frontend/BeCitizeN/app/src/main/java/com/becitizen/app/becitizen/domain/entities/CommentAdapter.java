package com.becitizen.app.becitizen.domain.entities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.presentation.ControllerThreadPresentation;
import com.becitizen.app.becitizen.presentation.UserProfile;

import java.util.List;

/**
 * Created by ISA on 27/04/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthor, commentTime, commentContent, commentVotes, commentAuthorRank;
        public ImageButton commentVote, commentReport, commentQuote, commentAuthorImage;
        public View commentView;

        public MyViewHolder(View view) {
            super(view);
            commentAuthor = view.findViewById(R.id.commentAuthorText);
            commentTime = view.findViewById(R.id.commentTime);
            commentContent = view.findViewById(R.id.commentContentText);
            commentVotes = view.findViewById(R.id.commentVotesText);
            commentAuthorRank = view.findViewById(R.id.commentAuthorRankText);

            commentVote = view.findViewById(R.id.commentVoteButton);
            commentReport = view.findViewById(R.id.commentReportButton);
            commentQuote = view.findViewById(R.id.commentQuoteButton);
            commentAuthorImage = view.findViewById(R.id.commentAuthorImage);
            commentView = view;
        }
    }

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thread_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.commentAuthor.setText(comment.getAuthor());
        holder.commentTime.setText(comment.getCreatedAt());
        holder.commentContent.setText(comment.getContent());
        holder.commentVotes.setText(comment.getVotes());
        holder.commentAuthorRank.setText(comment.getAuthorRank());

        holder.commentQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Quoting not implemented yet", Toast.LENGTH_LONG).show();
            }
        });

        holder.commentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerThreadPresentation.getUniqueInstance().reportComment(comment.getId());
            }
        });

        holder.commentVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerThreadPresentation.getUniqueInstance().voteComment(comment.getId());
            }
        });

        holder.commentAuthorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("loggeduser", false);
                bundle.putString("username", comment.getAuthor());
                Fragment fragment = new UserProfile();
                fragment.setArguments(bundle);
                fragmentTransaction(fragment, "USER_PROFILE", holder.commentView);


            }
        });

        if (!comment.isVotable()) {
            holder.commentVote.setImageResource(R.drawable.ic_voted_icon);
            holder.commentVote.setEnabled(false);
        }
        if (!comment.isReportable()) {
            holder.commentReport.setImageResource(R.drawable.ic_reported);
            holder.commentReport.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private void fragmentTransaction(Fragment fragment, String tag, View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}

