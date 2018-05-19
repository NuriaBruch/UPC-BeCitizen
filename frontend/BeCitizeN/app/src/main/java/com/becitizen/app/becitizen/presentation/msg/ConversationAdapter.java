package com.becitizen.app.becitizen.presentation.msg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

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
        ImageView profile_photo;
        TextView user_name;
        TextView username;
        TextView last_message_date;
        TextView unread;

        ConversationHolder(View itemView) {
            super(itemView);

            row_conversation = (RelativeLayout) itemView.findViewById(R.id.one_row_conversation);
            profile_photo = (ImageView) itemView.findViewById(R.id.conversation_profile_photo);
            user_name = (TextView) itemView.findViewById(R.id.conversation_user_name);
            username = (TextView) itemView.findViewById(R.id.conversation_username);
            last_message_date = (TextView) itemView.findViewById(R.id.conversation_last_message_date);
            unread = (TextView) itemView.findViewById(R.id.conversation_unread);
            //row_conversation.setOnClickListener(this);
        }

        void bind(Conversation conversation) {

            profile_photo.setImageResource(mContext.getResources().getIdentifier("userprofile" + conversation.getUserImage(), "drawable", null));
            // TODO: set the user name + lastname
            username.setText(conversation.getUserName());

            // Format the stored timestamp into a readable String using method.
            last_message_date.setText(DateUtils.formatSameDayTime(conversation.getLastMessage().getTime(), (new Date()).getTime(), DateFormat.SHORT, DateFormat.SHORT).toString());

            // TODO: See if the conversation is really unread
            unread.setText("!");
        }
    }
}
