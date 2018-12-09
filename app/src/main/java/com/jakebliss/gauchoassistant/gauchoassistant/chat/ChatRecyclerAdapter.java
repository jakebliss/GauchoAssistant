package com.jakebliss.gauchoassistant.gauchoassistant.chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import com.jakebliss.gauchoassistant.gauchoassistant.R;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    public static final String TYPE_USER_CHAT = "typeUserChat";
    public static final String TYPE_ASSISTANT_CHAT = "typeAssistantChat";

    private List<ChatMessage> mChats;

    public ChatRecyclerAdapter(List<ChatMessage> chats) {
        mChats = chats;
    }

    public void add(ChatMessage chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    public  ChatMessage getChat(int position){
        return mChats.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(mChats.get(position).getType().equals(TYPE_USER_CHAT))
            return 1;
        else
            return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        Log.d("OnCreateViewHolder", "Inside");
//        View viewChatMine = layoutInflater.inflate(R.layout.item_chat_sender, parent, false);
//        viewHolder = new MyChatViewHolder(viewChatMine);
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_sender, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_receiver, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = mChats.get(position);
        if (message.getType().equals(TYPE_USER_CHAT)) {
            ((MyChatViewHolder) holder).txtChatMessage.setText(message.getText().toString());
            ((MyChatViewHolder) holder).txtUserAlphabet.setText("J");
        } else {
            ((OtherChatViewHolder) holder).txtChatMessage.setText(message.getText().toString());
            ((OtherChatViewHolder) holder).txtUserAlphabet.setText("J");
        }
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        private ImageView photoImageView, profPicView;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        private ImageView photoImageView, profPicView;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
        }
    }
}
