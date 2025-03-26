package com.zombies.cowhealthai;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Message> messageList;
    private int lastBotMessageIndex = -1; // Tracks the last bot message for animation

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) { // User message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_user, parent, false);
        } else { // Bot message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_bot, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int    position) {
        Message message = messageList.get(position);

        if (message.isUser()) {
            holder.messageText.setText(message.getText());
        } else {
            // If this is the newest bot message, animate it
            if (position > lastBotMessageIndex) {
                lastBotMessageIndex = position;
                animateTyping(holder.messageText, message.getText());
            } else {
                holder.messageText.setText(message.getText()); // Instantly show older bot messages
            }
        }
    }

    private void animateTyping(TextView textView, String fullText) {
        textView.setText("");  // Clear text before typing
        Handler handler = new Handler();
        for (int i = 0; i < fullText.length(); i++) {
            int finalI = i;
            handler.postDelayed(() -> {
                textView.append(String.valueOf(fullText.charAt(finalI)));
            }, i * 50); // Adjust typing speed (50ms per character)
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isUser() ? 1 : 2;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}
