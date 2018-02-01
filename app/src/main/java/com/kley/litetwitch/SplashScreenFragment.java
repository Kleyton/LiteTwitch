/**
 * Copyright 2018 Jose Clayton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kley.litetwitch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Screen shown to the user when no previous account has been setup.
 * Created by Kley on 2/1/2018.
 */

public class SplashScreenFragment extends Fragment {
    private static final String TAG = SplashScreenFragment.class.getSimpleName();

    private EditText accountEditText;
    private Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Inflating view");
        return inflater.inflate(R.layout.splash_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountEditText = getView().findViewById(R.id.account_name);
        accountEditText.addTextChangedListener(new AccountEditTextWatcher());
        submitButton = getView().findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new SubmitButtonListener());
    }

    private class SubmitButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "I was pressed!");
            Log.i(TAG, "Current text is " + accountEditText.getText());
            TwitchHandler.getInstance().testAPICall(accountEditText.getText().toString());
        }
    }

    private class AccountEditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            submitButton.setEnabled(count > 0);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
