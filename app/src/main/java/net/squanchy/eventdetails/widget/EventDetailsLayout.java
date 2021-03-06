package net.squanchy.eventdetails.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Place;
import net.squanchy.support.lang.Optional;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EventDetailsLayout extends LinearLayout {

    private static final String WHEN_DATE_TIME_FORMAT = "EEEE, d MMMM 'at' HH:mm";

    private View whenContainer;
    private TextView whenTextView;
    private View whereContainer;
    private TextView whereTextView;
    private View levelContainer;
    private TextView levelTextView;
    private View descriptionHeader;
    private TextView descriptionTextView;

    public EventDetailsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventDetailsLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not supported for EventDetailsLayout");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View.inflate(getContext(), R.layout.merge_event_details_layout, this);

        whenTextView = findViewById(R.id.when_text);
        whenContainer = findViewById(R.id.when_container);
        whereTextView = findViewById(R.id.where_text);
        whereContainer = findViewById(R.id.where_container);
        levelTextView = findViewById(R.id.level_text);
        levelContainer = findViewById(R.id.level_container);
        descriptionHeader = findViewById(R.id.description_header);
        descriptionTextView = findViewById(R.id.description_text);
    }

    public void updateWith(Event event) {
        updateWhen(event);
        updateWhere(event);
        updateLevel(event.getExperienceLevel());
        updateDescription(event.getDescription());
    }

    private void updateWhen(Event event) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(WHEN_DATE_TIME_FORMAT)
                .withZone(event.getTimeZone());
        whenTextView.setText(formatter.print(event.getStartTime().toDateTime()));
        whenContainer.setVisibility(VISIBLE);
    }

    private void updateWhere(Event event) {
        if (event.getPlace().isPresent()) {
            whereContainer.setVisibility(VISIBLE);
            whereTextView.setText(placeTextFrom(event.getPlace().get()));
        } else {
            whereContainer.setVisibility(GONE);
        }
    }

    private CharSequence placeTextFrom(Place place) {
        SpannableStringBuilder builder = new SpannableStringBuilder(place.getName());
        if (place.getFloor().isPresent()) {
            String floorLabel = place.getFloor().get();
            builder.append("   ")
                    .append(floorLabel)
                    .setSpan(
                            createColorSpan(whereTextView, android.R.attr.textColorSecondary),
                            builder.length() - floorLabel.length(),
                            builder.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
        }
        return builder;
    }

    private void updateLevel(Optional<ExperienceLevel> level) {
        if (level.isPresent()) {
            levelContainer.setVisibility(VISIBLE);

            ExperienceLevel experienceLevel = level.get();
            levelTextView.setText(experienceLevel.getLabelStringResId());
            tintCompoundDrawableEnd(experienceLevel);
        } else {
            levelContainer.setVisibility(GONE);
        }
    }

    private void tintCompoundDrawableEnd(ExperienceLevel experienceLevel) {
        Drawable[] compoundDrawables = levelTextView.getCompoundDrawablesRelative();
        Drawable endCompoundDrawable = compoundDrawables[2];
        if (endCompoundDrawable != null) {
            endCompoundDrawable.setTint(getResources().getColor(experienceLevel.getColorResId()));
        }
    }

    private ForegroundColorSpan createColorSpan(View targetView, @AttrRes int attributeResId) {
        int color = getColorFromTheme(targetView.getContext().getTheme(), attributeResId);
        return new ForegroundColorSpan(color);
    }

    @ColorInt
    private int getColorFromTheme(Resources.Theme theme, @AttrRes int attributeId) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attributeId, typedValue, true);
        return typedValue.data;
    }

    private void updateDescription(Optional<String> description) {
        if (description.isPresent()) {
            descriptionHeader.setVisibility(VISIBLE);
            descriptionTextView.setVisibility(VISIBLE);
            descriptionTextView.setText(parseHtml(description.get()));
        } else {
            descriptionHeader.setVisibility(GONE);
            descriptionTextView.setVisibility(GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("deprecation")        // The older fromHtml() is only called pre-24
    private Spanned parseHtml(String description) {
        // TODO handle this properly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(description);
        }
    }
}
