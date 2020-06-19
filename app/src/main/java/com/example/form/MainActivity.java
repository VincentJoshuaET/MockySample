package com.example.form;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    InputMethodManager inputMethodManager;
    TextInputLayout inputName;
    TextInputEditText textName;
    TextInputLayout inputEmail;
    TextInputEditText textEmail;
    TextInputLayout inputNumber;
    TextInputEditText textNumber;
    TextInputLayout inputDateOfBirth;
    TextInputEditText textDateOfBirth;
    TextView titleAge;
    TextView textAge;
    AutoCompleteTextView textGender;
    TextInputLayout inputUrl;
    TextInputEditText textUrl;
    MaterialButton buttonSubmit;
    RetrofitInterface retrofitInterface;

    Calendar maxCalendar = Calendar.getInstance();
    Calendar calendarBirth = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    private void hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private void updateData(String url) {
        retrofitInterface.getUser(url).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textName.setText(response.body().getName());
                    textEmail.setText(response.body().getEmail());
                    textNumber.setText(response.body().getMobile());
                    try {
                        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                        calendarBirth.setTime(Objects.requireNonNull(format.parse(response.body().getBirthdate())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    textDateOfBirth.setText(dateFormat.format(calendarBirth.getTime()));
                    titleAge.setVisibility(View.VISIBLE);
                    textAge.setVisibility(View.VISIBLE);
                    Calendar current = Calendar.getInstance();
                    int years = current.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR);
                    if (calendarBirth.get(Calendar.MONTH) > current.get(Calendar.MONTH) || (calendarBirth.get(Calendar.MONTH) == current.get(Calendar.MONTH) && calendarBirth.get(Calendar.DATE) > current.get(Calendar.DATE))) {
                        years--;
                    }
                    String age = years + " years old";
                    textAge.setText(age);
                    textGender.setText(response.body().getGender(), false);
                    buttonSubmit.setClickable(true);
                    buttonSubmit.setIcon(null);
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Data updated", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(), Objects.requireNonNull(t.getLocalizedMessage()), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputName = findViewById(R.id.inputName);
        textName = findViewById(R.id.textName);
        inputEmail = findViewById(R.id.inputEmail);
        textEmail = findViewById(R.id.textEmail);
        inputNumber = findViewById(R.id.inputNumber);
        textNumber = findViewById(R.id.textNumber);
        inputDateOfBirth = findViewById(R.id.inputDateOfBirth);
        textDateOfBirth = findViewById(R.id.textDateOfBirth);
        titleAge = findViewById(R.id.titleAge);
        textAge = findViewById(R.id.textAge);
        textGender = findViewById(R.id.textGender);
        inputUrl = findViewById(R.id.inputUrl);
        textUrl = findViewById(R.id.textUrl);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.progressBarStyleSmall, value, false);
        int[] drawable = new int[] { android.R.attr.indeterminateDrawable };
        TypedArray attributes = obtainStyledAttributes(value.data, drawable);
        Animatable progress = (Animatable) attributes.getDrawable(0);

        if (progress != null) {
            progress.start();
        }
        buttonSubmit.setClickable(false);
        buttonSubmit.setIcon((Drawable) progress);

        retrofitInterface = new Retrofit.Builder().baseUrl("https://www.mocky.io/").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitInterface.class);
        updateData("v3/307d7f93-ec7c-4197-9d8f-f4b1c1bbe628");

        textName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && !Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
                    inputEmail.setError("Please enter valid email");
                } else {
                    inputEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() != 11) {
                    inputNumber.setError("Please enter valid mobile number");
                } else {
                    inputNumber.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        maxCalendar.add(Calendar.YEAR, -18);
        textDateOfBirth.setOnClickListener(view -> {
            hideKeyboard();
            DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, (datePicker, i, i1, i2) -> {
                calendarBirth.set(i, i1, i2);
                textDateOfBirth.setText(dateFormat.format(calendarBirth.getTime()));
                titleAge.setVisibility(View.VISIBLE);
                textAge.setVisibility(View.VISIBLE);
                Calendar current = Calendar.getInstance();
                int years = current.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR);
                if (calendarBirth.get(Calendar.MONTH) > current.get(Calendar.MONTH) || (calendarBirth.get(Calendar.MONTH) == current.get(Calendar.MONTH) && calendarBirth.get(Calendar.DATE) > current.get(Calendar.DATE))) {
                    years--;
                }
                String age = years + " years old";
                textAge.setText(age);
            }, calendarBirth.get(Calendar.YEAR), calendarBirth.get(Calendar.MONTH), calendarBirth.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());
            dialog.show();
        });


        String[] genders = {"Male", "Female"};
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, R.layout.item_dropdown, genders);
        textGender.setInputType(EditorInfo.TYPE_NULL);
        textGender.setOnFocusChangeListener((view, b) -> {
            if (b) hideKeyboard();
        });
        textGender.setAdapter(gendersAdapter);

        textUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && !Patterns.WEB_URL.matcher(charSequence).matches()) {
                    inputUrl.setError("Please enter valid URL");
                } else {
                    inputUrl.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonSubmit.setOnClickListener(view -> {
            hideKeyboard();
            String url = Objects.requireNonNull(textUrl.getText()).toString();
            if (url.isEmpty()) {
                inputUrl.setError("Please enter valid URL");
                return;
            }

            url = url.replace("https://www.mocky.io/", "");
            url = url.replace("https://run.mocky.io/", "");
            url = url.replace("http://www.mocky.io/", "");
            url = url.replace("http://run.mocky.io/", "");
            url = url.replace("www.mocky.io/", "");
            url = url.replace("run.mocky.io/", "");
            url = url.replace("mocky.io/", "");

            buttonSubmit.setClickable(false);
            buttonSubmit.setIcon((Drawable) progress);
            updateData(url);
        });
    }
}