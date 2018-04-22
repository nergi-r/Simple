package com.rawr.simple.seach.suggestion;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.rawr.simple.JSONRequestCallback;
import com.rawr.simple.api.Suggestion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class SearchSuggestion {

  private final Handler handler;
  private final ArrayAdapter adapter;
  private final Set<String> suggestions;
  private final Set<String> wordsTyped;
  private final long delay = 1000;
  private long lastSearchTime = 0;
  private String lastInput = "";

  public SearchSuggestion(final Context context, final AutoCompleteTextView searchView) {

    handler = new Handler();
    adapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line);
    suggestions = new HashSet<>();
    wordsTyped = new HashSet<>();
    searchView.setThreshold(1);
    searchView.setAdapter(adapter);

    final Runnable inputFinishChecker = new Runnable() {
      @Override
      public void run() {
        if (System.currentTimeMillis() > lastSearchTime + delay) {
          final String query = searchView.getText().toString();
          lastSearchTime = System.currentTimeMillis();
          new Suggestion(context, query).build().execute(new JSONRequestCallback() {
            @Override
            public void completed(JSONObject jsonObject) {
              try {
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int index = 0; index < jsonArray.length(); index++) {
                  String suggestion = jsonArray.getString(index);
                  if (!suggestions.contains(suggestion)) {
                    suggestions.add(suggestion);
                    adapter.add(suggestion);
                  }
                }
                lastInput = query;
                wordsTyped.add(query);

                // get currentInput again since text might have changed since the last request
                final String currentInput = searchView.getText().toString();
                searchView.showDropDown();
                searchView.setText(currentInput);
                searchView.setSelection(currentInput.length());

              } catch (Exception e) {
                Log.e("Search Suggestion", "Fail to parse JSON");
              }
            }

            @Override
            public void failed(Exception e) {

            }
          });
        }
      }
    };
    searchView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        handler.removeCallbacks(inputFinishChecker);
      }

      @Override
      public void afterTextChanged(Editable editable) {
        final String currentInput = searchView.getText().toString();
        if (wordsTyped.contains(currentInput)) return;

        if (editable.length() > 0) {
          handler.postDelayed(inputFinishChecker, 200);
        }
      }
    });
  }

  public void resetSuggestion() {
    adapter.clear();
    suggestions.clear();
    wordsTyped.clear();
    lastInput = "";
    lastSearchTime = 0;
  }
}
