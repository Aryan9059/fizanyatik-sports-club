package com.sportsclub.fizanyatik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {

    private TextInputEditText email, password;
    private TextInputLayout email_parent, password_parent;
    private TextView login, forgot;
    private FirebaseAuth mauth;
    CardView welcome;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.emaillog);
        email_parent = root.findViewById(R.id.emaillog_parent);
        password = root.findViewById(R.id.passwordlog);
        password_parent = root.findViewById(R.id.passwordlog_parent);
        login = root.findViewById(R.id.login);
        forgot = root.findViewById(R.id.forgotlog);
        welcome = root.findViewById(R.id.welcome_lottie);

        welcome.setTranslationX(800);
        email_parent.setTranslationX(800);
        password_parent.setTranslationX(800);
        forgot.setTranslationX(800);
        login.setTranslationX(800);

        welcome.setAlpha(v);
        email_parent.setAlpha(v);
        password_parent.setAlpha(v);
        forgot.setAlpha(v);
        login.setAlpha(v);

        welcome.animate().translationX(0).alpha(1).setDuration(900).setStartDelay(200).start();
        email_parent.animate().translationX(0).alpha(1).setDuration(900).setStartDelay(400).start();
        password_parent.animate().translationX(0).alpha(1).setDuration(900).setStartDelay(600).start();
        forgot.animate().translationX(0).alpha(1).setDuration(900).setStartDelay(600).start();
        login.animate().translationX(0).alpha(1).setDuration(900).setStartDelay(800).start();

        mauth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String semail = email.getText().toString();
                if(semail.isEmpty()){
                    Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.sendPasswordResetEmail(semail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
                                alertDialog.setTitle("Verification");
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage("A Reset Password mail has been sent to your Email Address. Please Reset your Password by following the instructions given in the respective mail.");
                                alertDialog.setPositiveButton("OK", null).create().show();
                            } else {
                                Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String semail = email.getText().toString();
                final String spassword = password.getText().toString();

                if(semail.isEmpty()){
                    Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                } else if (spassword.isEmpty()) {
                    Toast.makeText(getContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                } else if (semail.isEmpty() && spassword.isEmpty()) {
                    Toast.makeText(getContext(), "Enter correct credentials", Toast.LENGTH_SHORT).show();
                } else{
                    SignIn();
                }
            }
        });

        return root;
    }

    private void SignIn() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setView(R.layout.progress_material)
                .setCancelable(false).create();

        AlertDialog materialDialogs = dialogBuilder.create();
        materialDialogs.show();
        mauth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        materialDialogs.dismiss();

                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent (getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else{
                            Log.e("ERROR",task.getException().toString());
                            Toast.makeText(getContext(), task.getException().getMessage() + "", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
