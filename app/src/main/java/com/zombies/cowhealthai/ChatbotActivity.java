package com.zombies.cowhealthai;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {
    private EditText userInput;
    private Button sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();
    private TextView generatingText;
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";  // Replace with actual API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        userInput = findViewById(R.id.user_input);
        sendButton = findViewById(R.id.send_button);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        generatingText = findViewById(R.id.generating_text);

        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Add initial bot message
        messageList.add(new Message("How can I help you?", false)); // Bot message
        chatAdapter.notifyDataSetChanged();

        sendButton.setOnClickListener(v -> {
            String message = userInput.getText().toString();
            if (!message.isEmpty()) {
                messageList.add(new Message(message, true)); // User message
                chatAdapter.notifyDataSetChanged();
                userInput.setText("");

                // Show "Generating..." animation
                generatingText.setVisibility(View.VISIBLE);
                generatingText.setText("Generating...");
                new SendMessageTask().execute(message);
            }
        });
    }

    private class SendMessageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("model", "mistralai/mistral-7b-instruct");
                jsonInput.put("temperature", 0.7);
                jsonInput.put("max_tokens", 1024);

                JSONArray messages = new JSONArray();
                for (Message msg : messageList) {
                    JSONObject obj = new JSONObject();
                    obj.put("role", msg.isUser() ? "user" : "assistant");
                    obj.put("content", msg.getText());
                    messages.put(obj);
                }
                jsonInput.put("messages", messages);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonInput.toString());
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                // Debugging
                Log.d("ChatbotActivity", "API Response: " + responseBody);

                JSONObject jsonResponse = new JSONObject(responseBody);

                if (jsonResponse.has("choices")) {
                    return jsonResponse.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                } else if (jsonResponse.has("error")) {
                    return "API Error: " + jsonResponse.getJSONObject("error").getString("message");
                } else {
                    return "Unexpected API response format!";
                }

            } catch (Exception e) {
                Log.e("ChatbotActivity", "Error: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hide "Generating..." animation
            generatingText.setVisibility(View.GONE);

            messageList.add(new Message(result, false));
            chatAdapter.notifyDataSetChanged();
        }
    }
}
