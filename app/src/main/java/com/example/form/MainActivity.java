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

import com.example.form.databinding.ActivityMainBinding;
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
    TextInputLayout inputGender;
    AutoCompleteTextView textGender;
    TextInputLayout inputUrl;
    TextInputEditText textUrl;
    MaterialButton buttonSubmit;
    RetrofitInterface retrofitInterface;

    Calendar maxCalendar = Calendar.getInstance();
    Calendar calendarBirth = Calendar.getInstance();

    private void hideKeyboard(View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void updateData(String url, View view) {
        retrofitInterface.getUser(url).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textName.setText(response.body().getName());
                    textEmail.setText(response.body().getEmail());
                    textNumber.setText(response.body().getMobile());
                    String birthdate = response.body().getBirthdate();
                    DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                    try {
                        calendarBirth.setTime(Objects.requireNonNull(format.parse(birthdate)));
                        DateFormat stringFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                        textDateOfBirth.setText(stringFormat.format(calendarBirth.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    textGender.setText(response.body().getGender(), false);
                    buttonSubmit.setClickable(true);
                    buttonSubmit.setIcon(null);
                    Snackbar.make(view, "Data updated", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Snackbar.make(view, Objects.requireNonNull(t.getLocalizedMessage()), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputName = binding.inputName;
        textName = binding.textName;
        inputEmail = binding.inputEmail;
        textEmail = binding.textEmail;
        inputNumber = binding.inputNumber;
        textNumber = binding.textNumber;
        inputDateOfBirth = binding.inputDateOfBirth;
        textDateOfBirth = binding.textDateOfBirth;
        titleAge = binding.titleAge;
        textAge = binding.textAge;
        inputGender = binding.inputGender;
        textGender = binding.textGender;
        inputUrl = binding.inputUrl;
        textUrl = binding.textUrl;
        buttonSubmit = (MaterialButton) binding.buttonSubmit;

        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.progressBarStyleSmall, value, false);
        TypedArray attributes = obtainStyledAttributes(value.data, new int[] { android.R.attr.indeterminateDrawable });
        Animatable progress = (Animatable) attributes.getDrawable(0);

        if (progress != null) {
            progress.start();
        }
        buttonSubmit.setClickable(false);
        buttonSubmit.setIcon((Drawable) progress);

        retrofitInterface = new Retrofit.Builder().baseUrl("https://www.mocky.io/").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitInterface.class);
        updateData("v3/307d7f93-ec7c-4197-9d8f-f4b1c1bbe628", binding.getRoot());

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
        calendarBirth.add(Calendar.YEAR, -18);
        final DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        textDateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputDateOfBirth.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textDateOfBirth.setOnClickListener(view -> {
            hideKeyboard(view);
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
            if (b) {
                hideKeyboard(view);
            }
        });
        textGender.setAdapter(gendersAdapter);
        textGender.setOnItemClickListener((adapterView, view, i, l) -> inputGender.setError(null));


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
            hideKeyboard(view);
            String url = Objects.requireNonNull(textUrl.getText()).toString();
            if (url.isEmpty()) {
                inputUrl.setError("Please enter valid URL");
                return;
            }

            url = url.replace("https://www.mocky.io/", "");
            url = url.replace("https://run.mocky.io/", "");
            url = url.replace("http://www.mocky.io/", "");
            url = url.replace("http://run.mocky.io/", "");

            buttonSubmit.setClickable(false);
            buttonSubmit.setIcon((Drawable) progress);
            updateData(url, binding.getRoot());
        });
    }
}