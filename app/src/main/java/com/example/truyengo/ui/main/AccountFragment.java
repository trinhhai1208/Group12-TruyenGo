package com.example.truyengo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.truyengo.R;
import com.example.truyengo.ui.account.AccountManagementActivity;
import com.example.truyengo.ui.account.ChangePasswordActivity;
import com.example.truyengo.ui.auth.ForgotPasswordActivity;
import com.example.truyengo.ui.auth.LoginActivity;
import com.example.truyengo.ui.account.AppInfoActivity;

public class AccountFragment extends Fragment {

    private TextView btnLogout;

    private LinearLayout itemAccount, itemChangePass, itemForgotPass, itemAboutApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        btnLogout = view.findViewById(R.id.btnLogout);
        itemAccount = view.findViewById(R.id.itemAccountManagement);
        itemChangePass = view.findViewById(R.id.itemChangePassword);
        itemForgotPass = view.findViewById(R.id.itemForgotPassword);
        itemAboutApp = view.findViewById(R.id.itemAboutApp);

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            navigateToActivity(LoginActivity.class, true);
        });

        itemAccount.setOnClickListener(v -> {
            navigateToActivity(AccountManagementActivity.class, false);
        });

        itemChangePass.setOnClickListener(v -> {
            navigateToActivity(ChangePasswordActivity.class, false);
        });

        itemForgotPass.setOnClickListener(v -> {
            navigateToActivity(ForgotPasswordActivity.class, false);
        });

        itemAboutApp.setOnClickListener(v -> {
            navigateToActivity(AppInfoActivity.class, false);
        });

        return view;
    }

    private void navigateToActivity(Class<?> targetClass, boolean isClearHistory) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), targetClass);
            if (isClearHistory) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }

            startActivity(intent);
        }
    }
}
