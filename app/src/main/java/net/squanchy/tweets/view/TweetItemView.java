package net.squanchy.tweets.view;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.support.widget.CardLayout;
import net.squanchy.tweets.domain.view.TweetViewModel;
import net.squanchy.tweets.util.TwitterFooterFormatter;

public class TweetItemView extends CardLayout {

    private TextView tweetTextView;
    private TweetFooterView tweetFooterView;

    public TweetItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public TweetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tweetTextView = (TextView) findViewById(R.id.tweet_text);
        tweetFooterView = (TweetFooterView) findViewById(R.id.tweet_footer);

        tweetTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void updateWith(TweetViewModel tweet) {
        tweetTextView.setText(tweet.spannedText());
        tweetFooterView.updateWith(tweet.user().photoUrl(), TwitterFooterFormatter.recapFrom(tweet, getContext()));
    }
}
