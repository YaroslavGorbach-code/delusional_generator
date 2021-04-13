package com.YaroslavGorbach.delusionalgenerator.screen.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.YaroslavGorbach.delusionalgenerator.data.oldDataLayer.Repo_SQLite;
import com.YaroslavGorbach.delusionalgenerator.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;


public class StatisticsFragment extends Fragment {

    private BarChart mChartMinutes;
    private BarChart mChartWorldCount;
    private CardView mChartMinutes_cv;
    int mIdEx;
    private MaterialToolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        mChartMinutes = view.findViewById(R.id.chartMinutes);
        mChartWorldCount = view.findViewById(R.id.chartWorlds);
        mToolbar = requireActivity().findViewById(R.id.toolbar_main_a);
        mChartMinutes_cv = view.findViewById(R.id.cardView2);
        mIdEx = StatisticsFragmentArgs.fromBundle(getArguments()).getExId();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Потписка на слушателей*/
        createTimeChart();
        createWorldCounterChart();
        Repo_SQLite.getInstance(getContext()).addListener(this::createTimeChart);
        Repo_SQLite.getInstance(getContext()).addListener(this::createWorldCounterChart);

        /*Показ диалога который для удаления статистики*/
        mToolbar.setOnMenuItemClickListener(v->{
            DialogClearStatistics.crete(mIdEx).show(getChildFragmentManager(),null);
            return true;
        });

        /*Если статисти для упражнений второй категории то убрать чарт с минутами*/
        if (mIdEx == 22 || mIdEx == 20 || mIdEx == 21) {
            mChartMinutes_cv.setVisibility(View.GONE);
        }
    }

    /*Создаем чарт для времени*/
    private  void createTimeChart(){
        BarDataSet bardataset = new BarDataSet(Repo_SQLite.getInstance(getContext()).getEntriesTime(mIdEx), "Минуты");
        BarData data = new BarData(Repo_SQLite.getInstance(getContext()).getTimeLabels(mIdEx), bardataset);
        mChartMinutes.setData(data); // set the data and list of labels into chart
        mChartMinutes.setDescription("Количество минут потраченых на сессию");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        mChartMinutes.animateY(2000);
    }

    /*Создаем чарт для количества слов*/
    private  void createWorldCounterChart(){
        BarDataSet bardataset = new BarDataSet(Repo_SQLite.getInstance(getContext()).getEntriesWorldCount(mIdEx), "Слова");
        BarData data = new BarData(Repo_SQLite.getInstance(getContext()).getWorldCountLabels(mIdEx), bardataset);
        mChartWorldCount.setData(data); // set the data and list of labels into chart
        mChartWorldCount.setDescription("Количество пройденых слов за сессию");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        mChartWorldCount.animateY(2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Repo_SQLite.getInstance(getContext()).removeListener(this::createTimeChart);
        Repo_SQLite.getInstance(getContext()).removeListener(this::createWorldCounterChart);
    }
}