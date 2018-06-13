package com.becitizen.app.becitizen.presentation.forum;

import android.accounts.NetworkErrorException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.domain.controllers.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.domain.entities.ForumThread;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.becitizen.app.becitizen.presentation.user.UserProfileActivity;
import com.becitizen.app.becitizen.presentation.controllers.ControllerForumPresentation;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ThreadActivity extends Fragment {
    private View rootView;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private int threadId;
    private ControllerForumPresentation controllerForumPresentation;

    TextView threadAuthor, threadTime, threadContent, threadTitle, threadVotes, threadAuthorRank;
    ImageButton threadVote, threadReport, threadAuthorImage, commentSort;
    EditText newCommentText;
    ImageButton newCommentButton;
    LinearLayout newCommentLayout;
    boolean sortedByVotes;

    private ForumThread thread;

    public ThreadActivity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.thread_content, container, false);

        controllerForumPresentation = ControllerForumPresentation.getUniqueInstance();

        recyclerView = rootView.findViewById(R.id.recyclerView);

        sortedByVotes = false;

        prepareContent();

        setCommentList();

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
            if(ControllerUserDomain.getUniqueInstance().isLoggedAsGuest()) {
                newCommentLayout = (LinearLayout) rootView.findViewById(R.id.newCommentLayout);
                newCommentLayout.setVisibility(View.GONE);
            }
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }

        try {
            thread = controllerForumPresentation.getThreadContent(threadId);

            threadAuthor = rootView.findViewById(R.id.threadAuthorText);
            threadAuthor.setText("@" + thread.getAuthor());

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
            threadAuthorImage = rootView.findViewById(R.id.threadAuthorImage);
            setAuthorImage(thread.getAuthorImage());

            threadVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        controllerForumPresentation.voteThread(threadId);
                        threadVote.setImageResource(R.drawable.ic_voted_icon);
                        threadVote.setEnabled(false);
                        threadVotes.setText(String.valueOf(Integer.valueOf(threadVotes.getText().toString()) + 1));
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), "JSON error", Toast.LENGTH_LONG).show();
                    }
                    catch (ServerException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (NetworkErrorException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            });

            threadReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        controllerForumPresentation.reportThread(threadId);
                        threadReport.setImageResource(R.drawable.ic_reported);
                        threadReport.setEnabled(false);
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), "JSON error", Toast.LENGTH_LONG).show();
                    }
                    catch (ServerException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (NetworkErrorException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

            threadAuthorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("loggeduser", false);
                    bundle.putString("username", thread.getAuthor());
                    Fragment fragment = new UserProfileActivity();
                    fragment.setArguments(bundle);
                    fragmentTransaction(fragment, "USER_PROFILE");
                }
            });

            commentSort = rootView.findViewById(R.id.commentSortButton);
            commentSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sortedByVotes = !sortedByVotes;
                    prepareComments();
                }
            });

            newCommentText = rootView.findViewById(R.id.newCommentInput);
            newCommentButton = rootView.findViewById(R.id.newCommentButton);
            newCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String commentText = newCommentText.getText().toString().trim();
                    if (commentText.isEmpty())
                        Snackbar.make(view, R.string.emptyreply, Snackbar.LENGTH_LONG).show();
                    else {
                        try {
                            controllerForumPresentation.newComment(commentText, threadId);
                            prepareComments();
                            Toast.makeText(getContext(), R.string.commentCreated, Toast.LENGTH_LONG).show();
                            newCommentText.clearFocus();
                            newCommentText.setText(null);
                        }
                        catch (JSONException e) {
                            Toast.makeText(getContext(), "JSON error", Toast.LENGTH_LONG).show();
                        }
                        catch (ServerException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (NetworkErrorException e) {
                            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            });

        }
        catch (JSONException e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack();
            transaction.remove(this);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.commit();
        }
        catch (ServerException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void prepareComments () {
        try {
            List<Comment> newCommentList = controllerForumPresentation.getThreadComments(threadId, sortedByVotes);
            commentList.clear();
            commentList.addAll(newCommentList);
            adapter.notifyDataSetChanged();
        }
        catch (JSONException e) {
            Toast.makeText(getContext(), "Comments couldn't be loaded", Toast.LENGTH_LONG).show();
            Log.e("JSONe", e.getMessage());
        }
        catch (ServerException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
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
