package com.rohg007.android.instiflo.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rohg007.android.instiflo.MainActivity;
import com.rohg007.android.instiflo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private String username;
    private String password;
    private String confirmPassword;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        MaterialButton backToLoginButton = v.findViewById(R.id.back_to_login_button);
        final TextInputLayout usernameTextInputLayout = v.findViewById(R.id.signup_username_text_layout);
        final TextInputEditText usernameEditText = v.findViewById(R.id.signup_username_edit_text);
        final TextInputLayout passwordTextInputLayout = v.findViewById(R.id.signup_password_text_layout);
        final TextInputEditText passwordEditText = v.findViewById(R.id.signup_password_edit_text);
        final TextInputLayout confirmPasswordLayout = v.findViewById(R.id.signup_confirm_password_text_layout);
        final TextInputEditText confirmPasswordEditText = v.findViewById(R.id.signup_confirm_password_edit_text);
        final MaterialButton cancelButton = v.findViewById(R.id.signup_cancel_button);
        MaterialButton signUpButton = v.findViewById(R.id.signup_button);

        mAuth = FirebaseAuth.getInstance();

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.login_container,loginFragment).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields(usernameEditText,passwordEditText,confirmPasswordEditText);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if(!password.equals(confirmPassword))
                    confirmPasswordLayout.setError("Passwords don't match");

                if(username.isEmpty())
                    usernameTextInputLayout.setError("Username can't be empty");

                if(password.isEmpty())
                    passwordTextInputLayout.setError("Password can't be empty");

                if(password.equals(confirmPassword)&&!password.isEmpty()){
                    mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(),"Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),UserDetailsActivity.class);
                                intent.putExtra("email",username);
                                intent.putExtra("id",mAuth.getCurrentUser().getUid());
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

    private void clearFields(TextInputEditText usernameEditText, TextInputEditText passwordEditText, TextInputEditText confirmPasswordEditText){
        usernameEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
    }

}
