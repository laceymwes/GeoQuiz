package edu.cnm.deepdive.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

  private static final String TAG = "QuizActivity";
  private static final String KEY_INDEX = "index";
  private final static int REQUEST_CODE_CHEAT = 0;


  private Button mTrueButton;
  private Button mFalseButton;
  private Button mCheatButton;
  private Button mNextButton;
  private Button mPrevButton;
  private TextView mQuestionTextView;


  private List<Integer> answered = new ArrayList<>();


  private Question[] mQuestionBank = new Question[]{
      new Question(R.string.question_australia, true),
      new Question(R.string.question_oceans, true),
      new Question(R.string.question_mideast, false),
      new Question(R.string.question_africa, false),
      new Question(R.string.question_americas, true),
      new Question(R.string.question_asia, true)
  };
  private int correct = 0;


  private int mCurrentIndex = 0;
  private boolean mIsCheater;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate(Bundle) called");
    setContentView(R.layout.activity_quiz);

    if (savedInstanceState != null) {
      mCurrentIndex = savedInstanceState.getInt("index");
    }

    mQuestionTextView = findViewById(R.id.question_text_view);
    mQuestionTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        incrementIndex(); // increment current index
        updateQuestion(); // change question TextView text to next element
      }
    });

    mTrueButton = findViewById(R.id.true_button);
    mTrueButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (answered.contains(mCurrentIndex)) {
          Toast.makeText(QuizActivity.this, "You've already answered this question.", Toast.LENGTH_LONG).show();
          return;
        }
        checkAnswer(true);
        answered.add(mCurrentIndex);
      }
    });

    mFalseButton = findViewById(R.id.false_button);
    mFalseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (answered.contains(mCurrentIndex)) {
          Toast.makeText(QuizActivity.this, "You've already answered this question.", Toast.LENGTH_LONG).show();
          return;
        }
        checkAnswer(false);
        answered.add(mCurrentIndex);
      }
    });

    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (answered.size() == mQuestionBank.length) {
          double score = (double) correct / mQuestionBank.length;
          String scoreMsg = String.format(getResources().getString(R.string.quiz_score), Double.toString(score));
          Toast.makeText(QuizActivity.this, scoreMsg, Toast.LENGTH_LONG).show();
        }
        incrementIndex();
        updateQuestion();
      }
    });

    mCheatButton = findViewById(R.id.cheat_button);
    mCheatButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        startActivityForResult(CheatActivity.newItent(getApplicationContext(), answerIsTrue), REQUEST_CODE_CHEAT);
      }
      }
    );
    
    mPrevButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        decrementIndex();
        updateQuestion();
      }
    });
    updateQuestion();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == REQUEST_CODE_CHEAT) {
      if (data == null) {
        return;
      }
      mIsCheater = CheatActivity.wasAnswerShown(data);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.d(TAG, "onStart() called");
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume() called");
  }

  @Override
  public void onPause() {
    super.onPause();
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    Log.i(TAG, "onSaveInstanceState()");
    savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
  }

  @Override
  public void onStop() {
    super.onStop();
    Log.d(TAG, "onStop() called");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy() called");
  }

  private void checkAnswer(boolean userPressedTrue) {
    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
    if (answerIsTrue) {
      ++correct;
    }
    int messageResId;
    if (mIsCheater) {
      messageResId = R.string.judgement_toast;
    }
    else {
      messageResId = (userPressedTrue == answerIsTrue) ?
          R.string.correct_toast : R.string.incorrect_toast;
    }
    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
  }

  private void incrementIndex() {
    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
    mIsCheater = false;
  }

  private void decrementIndex() {
    // if index already at first element move to last element.
    mCurrentIndex = (mCurrentIndex < 1) ? mQuestionBank.length - 1 : mCurrentIndex - 1;
  }

  private void updateQuestion() {
    int question = mQuestionBank[mCurrentIndex].getTextResId();
    mQuestionTextView.setText(question);
  }
}
