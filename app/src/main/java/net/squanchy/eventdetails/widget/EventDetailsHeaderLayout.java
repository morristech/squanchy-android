package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.widget.SpeakerView;

public class EventDetailsHeaderLayout extends AppBarLayout {

    private TextView titleView;
    private SpeakerView speakerView;

    public EventDetailsHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleView = (TextView) findViewById(R.id.title);
        speakerView = (SpeakerView) findViewById(R.id.speaker_container);
    }

    void updateWith(Event event, SpeakerView.OnSpeakerClickListener listener) {
        titleView.setText(event.getTitle());
        titleView.setVisibility(VISIBLE);

        speakerView.setVisibility(event.getSpeakers().isEmpty() ? GONE : VISIBLE);
        speakerView.updateWith(event.getSpeakers(), Optional.of(listener));
    }
}
