package com.example.assignment_02;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Patterns;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Button pickImageButton;
    private EditText nameInput, emailInput, passwordInput, reenterPasswordInput;
    private RadioGroup genderRadioGroup;
    private Spinner countrySpinner;
    private CheckBox agreeCheckbox;
    private TextView nameError, emailError, passwordError, reenterPasswordError;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        pickImageButton = findViewById(R.id.pickImageBtn);
        pickImageButton.setOnClickListener(v -> openImagePicker());

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        reenterPasswordInput = findViewById(R.id.reenterPasswordInput);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        countrySpinner = findViewById(R.id.countrySpinner);
        agreeCheckbox = findViewById(R.id.agreeCheckBox);

        nameError = findViewById(R.id.nameError);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        reenterPasswordError = findViewById(R.id.reenterPasswordError);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> validateFields());

        List<String> countries = new ArrayList<>();
        countries.add("Select Country");
        countries.add("Sri Lanka");
        countries.add("India");
        countries.add("Australia");
        countries.add("USA");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateFields() {
        boolean isValid = true;

        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            nameError.setText("Name is required");
            nameError.setVisibility(TextView.VISIBLE);
            isValid = false;
        } else if (!name.matches("[a-zA-Z ]{3,}")) {
            Toast.makeText(this, "Name must be at least 3 letters", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            nameError.setVisibility(TextView.GONE);
        }

        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            emailError.setText("Email is required");
            emailError.setVisibility(TextView.VISIBLE);
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            emailError.setVisibility(TextView.GONE);
        }

        String password = passwordInput.getText().toString().trim();
        if (password.isEmpty()) {
            passwordError.setText("Password is required");
            passwordError.setVisibility(TextView.VISIBLE);
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(this, "Password must contain at least 1 uppercase letter", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!password.matches(".*[0-9].*")) {
            Toast.makeText(this, "Password must contain at least 1 number", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!password.matches(".*[!@#\\$%^&*].*")) {
            Toast.makeText(this, "Password must contain at least 1 special character", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            passwordError.setVisibility(TextView.GONE);
        }

        String reenterPassword = reenterPasswordInput.getText().toString().trim();
        if (reenterPassword.isEmpty()) {
            reenterPasswordError.setText("Please re-enter password");
            reenterPasswordError.setVisibility(TextView.VISIBLE);
            isValid = false;
        } else if (!reenterPassword.equals(password)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            reenterPasswordError.setVisibility(TextView.GONE);
        }

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (countrySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a country", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!agreeCheckbox.isChecked()) {
            Toast.makeText(this, "You must agree to the terms", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Registration Success!!")
                    .setMessage("Thank you for Register the system")
                    .setPositiveButton("OK", null)
                    .show();
        }

    }
}
