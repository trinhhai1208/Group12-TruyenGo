package com.example.truyengo.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Dùng Log thay cho System.out
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyengo.R;
import com.example.truyengo.commons.ChatAdapter;
import com.example.truyengo.models.user.ChatMessage;
import com.example.truyengo.services.impl.IChatBotServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatBotFragment extends Fragment {

    private RecyclerView rvChat;
    private EditText edtMessage;
    private ImageButton btnSend;
    private ChatAdapter chatAdapter;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Gán layout
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        // 2. Ánh xạ (Phải dùng view.findViewById)
        initViews(view);

        // 3. Setup RecyclerView
        setupRecyclerView();

        // 4. Gửi tin nhắn chào mừng
        addMessageToChat("Xin chào! Tôi có thể giúp gì cho bạn?", false);

        // 5. Sự kiện Click
        btnSend.setOnClickListener(v -> {
            String question = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(question)) {
                sendMessage(question);
            }
        });

        return view;
    }

    private void initViews(View view) {
        rvChat = view.findViewById(R.id.rvChat);
        edtMessage = view.findViewById(R.id.edtMessage);
        btnSend = view.findViewById(R.id.btnSend);
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter();
        // Fragment dùng getContext() thay vì this
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        rvChat.setLayoutManager(manager);
        rvChat.setAdapter(chatAdapter);
    }

    private void sendMessage(String question) {
        addMessageToChat(question, true);
        edtMessage.setText("");

        executorService.execute(() -> {
            try {
                String response = IChatBotServiceImpl.askGemini(question);

                // Fragment dùng getActivity().runOnUiThread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> addMessageToChat(response, false));
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void addMessageToChat(String message, boolean isUser) {
        if (chatAdapter != null) {
            chatAdapter.addMessage(new ChatMessage(message, isUser));
            if (chatAdapter.getItemCount() > 0) {
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}