package ru.alexey.weather.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.alexey.weather.R;


public class FragmentFeedback extends Fragment {

    private Button button;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        button = view.findViewById(R.id.button_send_feedback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonSendFeedback();
            }
        });
        editText = view.findViewById(R.id.edit_text_feedback);
        return view;
    }

    private void actionButtonSendFeedback(){
        String editTextString = editText.getText().toString();
        Toast.makeText(getActivity(), editTextString, Toast.LENGTH_LONG).show();
    }
}
