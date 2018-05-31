package edu.cnm.deepdive.geoquiz;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

  private Button mTrueButton;
  private Button mFalseButton;
  private ImageButton mNextButton;
  private ImageButton mPrevButton;
  private TextView mQuestionTextView;

  private Question[] mQuestionBank = new Question[]{
      new Question(R.string.question_australia, true),
      new Question(R.string.question_oceans, true),
      new Question(R.string.question_mideast, false),
      new Question(R.string.question_africa, false),
      new Question(R.string.question_americas, true),
      new Question(R.string.question_asia, true)
  };

  private int mCurrentIndex = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);

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
        checkAnswer(true);
      }
    });

    mFalseButton = findViewById(R.id.false_button);
    mFalseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(false);
      }
    });

    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        incrementIndex();
        updateQuestion();
      }
    });

    mPrevButton = findViewById(R.id.prev_button);
    mPrevButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        decrementIndex();
        updateQuestion();
      }
    });
    updateQuestion();
  }

  private void checkAnswer(boolean userPressedTrue) {
    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
    int messageResId = 0;

    if (userPressedTrue == answerIsTrue) {
      messageResId = R.string.correct_toast;
    } else {
      messageResId = R.string.incorrect_toast;
    }
    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
  }

  private void incrementIndex() {
    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
  }

  private void decrementIndex() {
    // if index already at first element make no changes
    mCurrentIndex = (mCurrentIndex < 1) ? 0 : (mCurrentIndex - 1) % mQuestionBank.length;
  }

  private void updateQuestion() {
    int question = mQuestionBank[mCurrentIndex].getTextResId();
    mQuestionTextView.setText(question);
  }
}
