package com.jakebliss.gauchoassistant.gauchoassistant.chat;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakebliss.gauchoassistant.gauchoassistant.R;

import java.util.ArrayList;


public class ChatFragment extends Fragment {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private static final int RC_PHOTO_PICKER = 2;

    private Button mSendButton;
    private Button mSendButtonCancel;


    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButtonCancel = (Button) view.findViewById(R.id.sendButtonCancel);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        // Enable Send button when there's text to send
        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setVisibility(View.VISIBLE);
                    mSendButton.setEnabled(true);
                    mSendButtonCancel.setVisibility(View.GONE);
                } else {
                    mSendButton.setVisibility(View.GONE);
                    mSendButton.setEnabled(false);
                    mSendButtonCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mETxtMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatRecyclerAdapter == null) {
                    mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<ChatMessage>());
                    mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
                }
                mChatRecyclerAdapter.add(makeNewChatMessage(mETxtMessage.getText().toString()));
                mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
                mETxtMessage.setText("");
            }
        });

    }

    private ChatMessage makeNewChatMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, "typeUserChat");
        return chatMessage;
    }

}
