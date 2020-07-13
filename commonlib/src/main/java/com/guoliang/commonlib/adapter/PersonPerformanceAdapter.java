package com.guoliang.commonlib.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.guoliang.commonlib.R;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.entity.PersonPerformance;
import com.guoliang.commonlib.view.NiceImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PersonPerformanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "PersonPerformanceAdapter";

    private List<PersonPerformance> list;
    private String  mType;

    public PersonPerformanceAdapter(List<PersonPerformance> list) {
        this.list = list;
    }

    public void setList(List<PersonPerformance> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_performance_one_item, parent, false);
        PersonPerformaceViewHolder holder = new PersonPerformaceViewHolder(view);

        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        position = position * 5;
        if (position >= list.size()) {
            ((PersonPerformaceViewHolder)holder).rl1.setVisibility(View.GONE);
        }
        else {
            PersonPerformance personPerformance = list.get(position);
//        ((PersonPerformaceViewHolder)holder).head1.set;
            ((PersonPerformaceViewHolder)holder).nickName1.setText(personPerformance.getNickName());
            if (!TextUtils.isEmpty(personPerformance.getHeadUrl())) {
//                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).into(((PersonPerformaceViewHolder)holder).head1);
                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(((PersonPerformaceViewHolder)holder).head1);
            }
            ((PersonPerformaceViewHolder)holder).score1.setText("" + personPerformance.getScore());
        }

        if (position == mSelectPosition) {
            ((PersonPerformaceViewHolder)holder).nickName1.setBackgroundColor(0xFFF8D91C);
        }
        else {
            ((PersonPerformaceViewHolder)holder).nickName1.setBackgroundColor(0xFFFFFFFF);
        }

        ++position;

        if (position >= list.size()) {
            ((PersonPerformaceViewHolder)holder).rl2.setVisibility(View.GONE);
        }
        else {
            PersonPerformance personPerformance = list.get(position);
//        ((PersonPerformaceViewHolder)holder).head1.set;
            ((PersonPerformaceViewHolder)holder).nickName2.setText(personPerformance.getNickName());
            if (!TextUtils.isEmpty(personPerformance.getHeadUrl())) {
//                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).into(((PersonPerformaceViewHolder)holder).head2);
                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(((PersonPerformaceViewHolder)holder).head2);
            }
            ((PersonPerformaceViewHolder)holder).score2.setText("" + personPerformance.getScore());
        }

        if (position == mSelectPosition) {
            ((PersonPerformaceViewHolder)holder).nickName2.setBackgroundColor(0xFFF8D91C);
        }
        else {
            ((PersonPerformaceViewHolder)holder).nickName2.setBackgroundColor(0xFFFFFFFF);
        }

        ++position;

        if (position >= list.size()) {
            ((PersonPerformaceViewHolder)holder).rl3.setVisibility(View.GONE);
        }
        else {
            PersonPerformance personPerformance = list.get(position);
//        ((PersonPerformaceViewHolder)holder).head1.set;
            ((PersonPerformaceViewHolder)holder).nickName3.setText(personPerformance.getNickName());
            if (!TextUtils.isEmpty(personPerformance.getHeadUrl())) {
//                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).into(((PersonPerformaceViewHolder)holder).head3);
                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(((PersonPerformaceViewHolder)holder).head3);
            }
            ((PersonPerformaceViewHolder)holder).score3.setText("" + personPerformance.getScore());
        }

        if (position == mSelectPosition) {
            ((PersonPerformaceViewHolder)holder).nickName3.setBackgroundColor(0xFFF8D91C);
        }
        else {
            ((PersonPerformaceViewHolder)holder).nickName3.setBackgroundColor(0xFFFFFFFF);
        }

        ++position;

        if (position >= list.size()) {
            ((PersonPerformaceViewHolder)holder).rl4.setVisibility(View.GONE);
        }
        else {
            PersonPerformance personPerformance = list.get(position);
//        ((PersonPerformaceViewHolder)holder).head1.set;
            ((PersonPerformaceViewHolder)holder).nickName4.setText(personPerformance.getNickName());
            if (!TextUtils.isEmpty(personPerformance.getHeadUrl())) {
//                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).into(((PersonPerformaceViewHolder)holder).head4);
                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(((PersonPerformaceViewHolder)holder).head4);
            }
            ((PersonPerformaceViewHolder)holder).score4.setText("" + personPerformance.getScore());
        }

        if (position == mSelectPosition) {
            ((PersonPerformaceViewHolder)holder).nickName4.setBackgroundColor(0xFFF8D91C);
        }
        else {
            ((PersonPerformaceViewHolder)holder).nickName4.setBackgroundColor(0xFFFFFFFF);
        }

        ++position;

        if (position >= list.size()) {
            ((PersonPerformaceViewHolder)holder).rl5.setVisibility(View.GONE);
        }
        else {
            PersonPerformance personPerformance = list.get(position);
//        ((PersonPerformaceViewHolder)holder).head1.set;
            ((PersonPerformaceViewHolder)holder).nickName5.setText(personPerformance.getNickName());
            if (!TextUtils.isEmpty(personPerformance.getHeadUrl())) {
//                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).into(((PersonPerformaceViewHolder)holder).head5);
                Glide.with(((PersonPerformaceViewHolder)holder).view).load(personPerformance.getHeadUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(((PersonPerformaceViewHolder)holder).head5);
            }
            ((PersonPerformaceViewHolder)holder).score5.setText("" + personPerformance.getScore());
        }

        if (position == mSelectPosition) {
            ((PersonPerformaceViewHolder)holder).nickName5.setBackgroundColor(0xFFF8D91C);
        }
        else {
            ((PersonPerformaceViewHolder)holder).nickName5.setBackgroundColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() {
        int ret = list.size() / 5;
         return list.size() % 5 == 0 ? ret : ret + 1;
    }

    private int mSelectPosition = 0;
    private void setOnClickListener(final PersonPerformaceViewHolder holder) {
        if (!list.isEmpty()) {
            EventBus.getDefault().postSticky(new MessageEvent("select_person_formance", list.get(0).getUserId()));
        }

        holder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mSelectPosition = position * 5;
                notifyDataSetChanged();
                EventBus.getDefault().postSticky(new MessageEvent("select_person_formance", list.get(mSelectPosition).getUserId()));
            }
        });

        holder.rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mSelectPosition = position * 5 + 1;
                notifyDataSetChanged();
                EventBus.getDefault().postSticky(new MessageEvent("select_person_formance", list.get(mSelectPosition).getUserId()));
            }
        });

        holder.rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mSelectPosition = position * 5 + 2;
                notifyDataSetChanged();
                EventBus.getDefault().postSticky(new MessageEvent("select_person_formance", list.get(mSelectPosition).getUserId()));
            }
        });

        holder.rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mSelectPosition = position * 5 + 3;
                notifyDataSetChanged();
                EventBus.getDefault().postSticky(new MessageEvent("select_person_formance", list.get(mSelectPosition).getUserId()));
            }
        });

        holder.rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mSelectPosition = position * 5 + 4;
                notifyDataSetChanged();
                EventBus.getDefault().postSticky(new MessageEvent("select_person_formance", list.get(mSelectPosition).getUserId()));
            }
        });
    }
}

class PersonPerformaceViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout rl1;
    public ImageView head1;
    public TextView nickName1, score1;

    public RelativeLayout rl2;
    public ImageView head2;
    public TextView nickName2, score2;

    public RelativeLayout rl3;
    public ImageView head3;
    public TextView nickName3, score3;

    public RelativeLayout rl4;
    public ImageView head4;
    public TextView nickName4, score4;

    public RelativeLayout rl5;
    public ImageView head5;
    public TextView nickName5, score5;

    public View view;

    public PersonPerformaceViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        rl1 = itemView.findViewById(R.id.rl_1);
        head1 = itemView.findViewById(R.id.head_1);
        nickName1 = itemView.findViewById(R.id.nick_name_1);
        score1 = itemView.findViewById(R.id.score_1);

        rl2 = itemView.findViewById(R.id.rl_2);
        head2 = itemView.findViewById(R.id.head_2);
        nickName2 = itemView.findViewById(R.id.nick_name_2);
        score2 = itemView.findViewById(R.id.score_2);

        rl3 = itemView.findViewById(R.id.rl_3);
        head3 = itemView.findViewById(R.id.head_3);
        nickName3 = itemView.findViewById(R.id.nick_name_3);
        score3 = itemView.findViewById(R.id.score_3);

        rl4 = itemView.findViewById(R.id.rl_4);
        head4 = itemView.findViewById(R.id.head_4);
        nickName4 = itemView.findViewById(R.id.nick_name_4);
        score4 = itemView.findViewById(R.id.score_4);

        rl5 = itemView.findViewById(R.id.rl_5);
        head5 = itemView.findViewById(R.id.head_5);
        nickName5 = itemView.findViewById(R.id.nick_name_5);
        score5 = itemView.findViewById(R.id.score_5);
    }
}

