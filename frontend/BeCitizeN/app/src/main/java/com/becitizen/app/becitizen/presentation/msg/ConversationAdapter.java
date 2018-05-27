package com.becitizen.app.becitizen.presentation.msg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationAdapter  extends RecyclerView.Adapter {

    private Context mContext;
    private List<Conversation> mConversationList;

    public ConversationAdapter(Context context, List<Conversation> ConversationList) {
        mContext = context;
        mConversationList = ConversationList;
    }

    @Override
    public int getItemCount() {
        return mConversationList.size();
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_conversation, parent, false);

        return new ConversationAdapter.ConversationHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Conversation conversation = (Conversation) mConversationList.get(position);
        ((ConversationAdapter.ConversationHolder) holder).bind(conversation);
    }

    private class ConversationHolder extends RecyclerView.ViewHolder {
        RelativeLayout row_conversation;
        CircleImageView profile_photo;
        TextView user_name;
        TextView username;
        TextView last_message;
        TextView last_message_date;
        TextView unread;

        ConversationHolder(View itemView) {
            super(itemView);

            row_conversation = (RelativeLayout) itemView.findViewById(R.id.one_row_conversation);
            profile_photo = (CircleImageView) itemView.findViewById(R.id.conversation_profile_photo);
            user_name = (TextView) itemView.findViewById(R.id.conversation_user_name);
            username = (TextView) itemView.findViewById(R.id.conversation_username);
            last_message = (TextView) itemView.findViewById(R.id.conversation_last_message);
            last_message_date = (TextView) itemView.findViewById(R.id.conversation_last_message_date);
            unread = (TextView) itemView.findViewById(R.id.conversation_unread);
        }

        void bind(final Conversation conversation) {

            row_conversation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OneConversationActivity.class);
                    intent.putExtra("id", conversation.getId());
                    intent.putExtra("name", conversation.getName());
                    intent.putExtra("username", conversation.getUserName());
                    intent.putExtra("profilePicture", conversation.getUserImage());
                    mContext.startActivity(intent);
                }
            });

            Log.d("CONVERSATION", conversation.toString());

            profile_photo.setImageResource(getImageId(conversation.getUserImage()));
            // TODO: set the user name + lastname
            user_name.setText(conversation.getName());
            username.setText("@" + conversation.getUserName());
            if (conversation.getLastMessage().length() < 25) last_message.setText(conversation.getLastMessage());
            else last_message.setText(conversation.getLastMessage().substring(0, 23) + "...");
            // Format the stored timestamp into a readable String using method.
            last_message_date.setText(DateUtils.formatSameDayTime(conversation.getLastMessageDate().getTime(), (new Date()).getTime(), DateFormat.SHORT, DateFormat.SHORT).toString());

            // TODO: See if the conversation is really unread
            if (conversation.isNewMessage()) unread.setText("!");
            else unread.setVisibility(View.INVISIBLE);
        }
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
