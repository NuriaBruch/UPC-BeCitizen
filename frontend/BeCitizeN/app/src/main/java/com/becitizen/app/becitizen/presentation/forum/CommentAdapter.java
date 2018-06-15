package com.becitizen.app.becitizen.presentation.forum;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.controllers.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerForumPresentation;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;
import com.becitizen.app.becitizen.presentation.user.UserProfileActivity;

import org.json.JSONException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ISA on 27/04/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private ControllerForumPresentation controllerForumPresentation;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthor, commentTime, commentContent, commentVotes, commentAuthorRank, commentId;
        public ImageButton commentVote, commentReport, commentQuote;
        public CircleImageView commentAuthorImage;
        public View commentView;

        public MyViewHolder(View view) {
            super(view);
            commentAuthor = view.findViewById(R.id.commentAuthorText);
            commentTime = view.findViewById(R.id.commentTime);
            commentContent = view.findViewById(R.id.commentContentText);
            commentVotes = view.findViewById(R.id.commentVotesText);
            commentAuthorRank = view.findViewById(R.id.commentAuthorRankText);
            commentId = view.findViewById(R.id.commentId);

            commentVote = view.findViewById(R.id.commentVoteButton);
            commentReport = view.findViewById(R.id.commentReportButton);
            commentQuote = view.findViewById(R.id.commentQuoteButton);
            commentAuthorImage = view.findViewById(R.id.commentAuthorImage);
            commentView = view;

            controllerForumPresentation = ControllerForumPresentation.getUniqueInstance();
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
        holder.commentAuthor.setText("@" + comment.getAuthor());
        holder.commentTime.setText(comment.getCreatedAt());
        holder.commentContent.setText(comment.getContent());
        holder.commentVotes.setText(String.valueOf(comment.getVotes()));
        holder.commentAuthorRank.setText(comment.getAuthorRank());
        holder.commentId.setText('#'+String.valueOf(comment.getId()));

        try {
            if(!ControllerUserPresentation.getUniqueInstance().isLoggedAsGuest()) {
                holder.commentQuote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.thread_comment_quote);
                        //LayoutInflater inflater = LayoutInflater.from(context);
                        //View quoteView = inflater.inflate(R.layout.thread_comment_quote, null);

                        TextView quoteId = dialog.findViewById(R.id.quote_id_text);
                        quoteId.setText('#'+String.valueOf(comment.getId()));

                        TextView quoteContent = dialog.findViewById(R.id.quote_content);
                        quoteContent.setText(comment.getContent());

                        Button sendButton = dialog.findViewById(R.id.send_button);

                        final EditText commentContent = dialog.findViewById(R.id.comment_text);

                        dialog.show();


                        sendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String commentText = commentContent.getText().toString().trim();
                                if (commentText.isEmpty())
                                    Toast.makeText(context, R.string.emptyreply, Toast.LENGTH_LONG).show();
                                else {
                                    try {
                                        controllerForumPresentation.newComment("#"+String.valueOf(comment.getId())+' '+commentText, comment.getThreadId());
                                        Toast.makeText(context, R.string.commentCreated, Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        List<Comment> newCommentList = controllerForumPresentation.getThreadComments(comment.getThreadId(), false);
                                        commentList.clear();
                                        commentList.addAll(newCommentList);
                                        notifyDataSetChanged();
                                    }
                                    catch (JSONException e) {
                                        Toast.makeText(context, "JSON error", Toast.LENGTH_LONG).show();
                                    }
                                    catch (ServerException e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (NetworkErrorException e) {
                                        Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            }
                        });
                    }
                });

                holder.commentReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ControllerForumPresentation.getUniqueInstance().reportComment(comment.getId());
                            holder.commentReport.setImageResource(R.drawable.ic_reported);
                            holder.commentReport.setEnabled(false);
                        }
                        catch (JSONException e) {
                            Toast.makeText(context, "JSON error", Toast.LENGTH_LONG).show();
                        }
                        catch (ServerException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (NetworkErrorException e) {
                            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

                holder.commentVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ControllerForumPresentation.getUniqueInstance().voteComment(comment.getId());
                            holder.commentVote.setImageResource(R.drawable.ic_voted_icon);
                            holder.commentVote.setEnabled(false);
                            holder.commentVotes.setText(String.valueOf(Integer.valueOf(holder.commentVotes.getText().toString()) + 1));
                        }
                        catch (JSONException e) {
                            Toast.makeText(context, "JSON error", Toast.LENGTH_LONG).show();
                        }
                        catch (ServerException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (NetworkErrorException e) {
                            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }


        holder.commentAuthorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("loggeduser", false);
                bundle.putString("username", comment.getAuthor());
                Fragment fragment = new UserProfileActivity();
                fragment.setArguments(bundle);
                fragmentTransaction(fragment, "USER_PROFILE", holder.commentView);


            }
        });

        try {
            if(ControllerUserDomain.getUniqueInstance().isLoggedAsGuest()) {
                holder.commentVote.setClickable(false);
                holder.commentVote.setColorFilter(0xcacaba);
                holder.commentVote.setAlpha(.5f);

                holder.commentReport.setClickable(false);
                holder.commentReport.setColorFilter(0xcacaba);
                holder.commentReport.setAlpha(.5f);

                holder.commentQuote.setClickable(false);
                holder.commentQuote.setColorFilter(0xcacaba);
                holder.commentQuote.setAlpha(.5f);
            } else {
                if (!comment.isVotable()) {
                    holder.commentVote.setImageResource(R.drawable.ic_voted_icon);
                    holder.commentVote.setEnabled(false);
                }
                else {
                    holder.commentVote.setImageResource(R.drawable.vote_icon);
                    holder.commentVote.setEnabled(true);
                }
                if (!comment.isReportable()) {
                    holder.commentReport.setImageResource(R.drawable.ic_reported);
                    holder.commentReport.setEnabled(false);
                }
                else {
                    holder.commentReport.setImageResource(R.drawable.report_icon);
                    holder.commentReport.setEnabled(true);
                }
            }
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }

        setAuthorImage(holder, comment.getAuthorImage());
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

    private void setAuthorImage(MyViewHolder holder, int number) {
        switch (number) {
            case 1:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile1);
                break;

            case 2:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile2);
                break;

            case 3:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile3);
                break;

            case 4:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile4);
                break;

            case 5:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile5);
                break;

            case 6:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile6);
                break;

            case 7:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile7);
                break;

            case 8:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile8);
                break;

            default:
                holder.commentAuthorImage.setImageResource(R.drawable.userprofile1);
                break;
        }
    }
}

