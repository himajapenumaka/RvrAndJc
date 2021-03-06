package com.hashik.rvrandjc.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hashik.rvrandjc.R;
import com.hashik.rvrandjc.models.JSONDataModels.Data;
import com.hashik.rvrandjc.models.JSONDataModels.Semester;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;



public class SemesterGradesAdapter extends RecyclerView.Adapter<SemesterGradesAdapter.ViewHolder> {

    private List<Semester> semesters;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;

    public SemesterGradesAdapter(List<Semester> repos) {
        this.semesters = repos;
        //set initial expanded state to false
        for (int i = 0; i < repos.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public SemesterGradesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SemesterGradesAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tvName.setText(semesters.get(i).getTitle());

        //check if view is expanded
        final boolean isExpanded = expandState.get(i);
        viewHolder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        viewHolder.buttonLayout.setRotation(expandState.get(i) ? 180f : 0f);

        for (Data data : semesters.get(i).getData()) {
            if (data.getTitle() != null && data.getGrade() != null) {
                TextView textView = new TextView(context);

                textView.setText(String.format("%s                                            %s", data.getTitle(), data.getGrade()));
                textView.setTextSize(15);
                textView.setPadding(0, 15, 0, 15);
                if (data.getGrade().equals("F")) {
                    textView.setTextColor(Color.RED);
                }
                if (data.getTitle().equals("CGPA") || data.getTitle().equals("SGPA") || data.getTitle().equals("Rank")) {
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setText(String.format("  %s                                          %s", data.getTitle(), data.getGrade()));
                }
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                viewHolder.expandableLayout.addView(textView);
            }
        }
        viewHolder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(viewHolder.expandableLayout, viewHolder.buttonLayout, viewHolder.getCompleteCard(), i);
            }
        });
        viewHolder.completeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.buttonLayout.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return semesters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvOwnerLogin, tvOwnerUrl;
        private ImageView ivOwner;
        public RelativeLayout buttonLayout;
        public LinearLayout expandableLayout;
        public LinearLayout completeCard;

        public LinearLayout getCompleteCard() {
            return completeCard;
        }

        public ViewHolder(View view) {
            super(view);
            completeCard = (LinearLayout) view.findViewById(R.id.one_card);
            tvName = (TextView) view.findViewById(R.id.title);

            buttonLayout = (RelativeLayout) view.findViewById(R.id.button);
            expandableLayout = (LinearLayout) view.findViewById(R.id.expandableLayout);
        }
    }

    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, LinearLayout completeCard, final int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            //Transition
            final ChangeBounds transition = new ChangeBounds();
            transition.setDuration(300); // Sets a duration of 600 milliseconds
            TransitionManager.beginDelayedTransition(completeCard,transition);

            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            //Transition
            final ChangeBounds transition = new ChangeBounds();
            transition.setDuration(300); // Sets a duration of 600 milliseconds
            TransitionManager.beginDelayedTransition(completeCard,transition);

            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}