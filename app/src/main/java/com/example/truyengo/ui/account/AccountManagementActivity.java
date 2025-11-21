package com.example.truyengo.ui.account;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;

public class AccountManagementActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvFullName, tvName, tvUsername, tvEmail, tvPhone, tvStatus, btnChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_account_management_activity);

        btnBack = findViewById(R.id.btnBack);
        tvFullName = findViewById(R.id.tvFullName);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvStatus = findViewById(R.id.tvStatus);
        btnChangeName = findViewById(R.id.btnChangeName);

        tvFullName.setText("Viết Doanh");
        tvName.setText("Viết Doanh");
        tvUsername.setText("Doanh ở Phủ Lý");
        tvEmail.setText("meomeo@gmail.com");
        tvPhone.setText("0991927504");
        tvStatus.setText("Active");

        // Nút quay lại
        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Nút đổi tên
        btnChangeName.setOnClickListener(v -> showChangeNameDialog());
    }

    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi tên hiển thị");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Nhập tên mới");
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            } else {
                tvFullName.setText(newName);
                tvName.setText(newName);
                Toast.makeText(this, "Đổi tên thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
