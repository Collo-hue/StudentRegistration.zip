package com.example.studentregistration;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etAge;
    private RadioGroup rgGender;
    private AutoCompleteTextView actvCourse;
    private Button btnSubmit;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Link the views
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        rgGender = findViewById(R.id.rgGender);
        actvCourse = findViewById(R.id.actvCourse);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvResult = findViewById(R.id.tvResult);

        // 2. Feed the AutoCompleteTextView with course suggestions
        String[] courses = getResources().getStringArray(R.array.courses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, courses);
        actvCourse.setAdapter(adapter);
        actvCourse.setThreshold(1);               // suggest after 1 character
        actvCourse.setOnClickListener(v -> actvCourse.showDropDown());

        // 3. Handle the Submit button
        btnSubmit.setOnClickListener(v -> submitForm());
    }

    private void submitForm() {
        String name = etName.getText().toString().trim();
        String ageText = etAge.getText().toString().trim();
        String course = actvCourse.getText().toString().trim();
        int checkedId = rgGender.getCheckedRadioButtonId();

        // ---------- Validation ----------
        if (name.isEmpty()) {
            etName.setError(getString(R.string.err_name));
            etName.requestFocus();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            age = -1;
        }
        if (age < 1 || age > 120) {
            etAge.setError(getString(R.string.err_age));
            etAge.requestFocus();
            return;
        }

        if (checkedId == -1) {
            Toast.makeText(this, R.string.err_gender, Toast.LENGTH_SHORT).show();
            return;
        }

        if (course.isEmpty()) {
            actvCourse.setError(getString(R.string.err_course));
            actvCourse.requestFocus();
            return;
        }

        // ---------- Read gender ----------
        RadioButton selected = findViewById(checkedId);
        String gender = selected.getText().toString();

        // ---------- Display the entered information ----------
        String summary = "Name: " + name
                + "\nAge: " + age
                + "\nGender: " + gender
                + "\nCourse: " + course;

        tvResult.setText(summary);                 // show in a TextView
        tvResult.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.toast_success, Toast.LENGTH_SHORT).show();  // and a Toast

        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
