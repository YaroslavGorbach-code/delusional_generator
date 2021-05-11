package com.YaroslavGorbach.delusionalgenerator.screen.records;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.YaroslavGorbach.delusionalgenerator.R;
import com.YaroslavGorbach.delusionalgenerator.component.recordsList.RecordsListImp;
import com.YaroslavGorbach.delusionalgenerator.data.Record;
import com.YaroslavGorbach.delusionalgenerator.data.Repo;
import com.YaroslavGorbach.delusionalgenerator.databinding.FragmentRecordsBinding;
import com.YaroslavGorbach.delusionalgenerator.feature.ad.AdManagerImp;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RecordsFragment extends Fragment {
    public RecordsFragment(){ super(R.layout.fragment_records); }
    private final CompositeDisposable mBag = new CompositeDisposable();
    private RecordsVm vm;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        vm.recordsList.getRecordsFromFile();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init vm
        Repo repo = new Repo.RepoProvider().provideRepo(requireContext());
        vm = new ViewModelProvider(this,
                new RecordsVm.RecordsVmFactory(new RecordsListImp(repo, requireContext(), mBag), new AdManagerImp(repo))).get(RecordsVm.class);

        // init view
        RecordsView v = new RecordsView(FragmentRecordsBinding.bind(view), new RecordsView.Callback() {
            @Override
            public void onRecord(Record record) { vm.recordsList.onPlay(record); }

            @Override
            public void onSkipNext() { vm.recordsList.onSkipNext(); }

            @Override
            public void onSkipPrevious() { vm.recordsList.onSkipPrevious(); }

            @Override
            public void onPause() { vm.recordsList.onPause(); }

            @Override
            public void onResume() { vm.recordsList.onResume(); }

            @Override
            public void onSeekTo(int progress) { vm.recordsList.onSeekTo(progress); }

            @Override
            public void onRemove(Record record) {
                vm.recordsList.onRemove(record);
            }
        },vm.adManager);
        vm.recordsList.getRecords().observe(getViewLifecycleOwner(), v::setRecords);
        vm.recordsList.getIsPlaying().observe(getViewLifecycleOwner(), v::setIsPlaying);
        vm.recordsList.getDuration().observe(getViewLifecycleOwner(), v::setDuration);
        vm.recordsList.getProgress().observe(getViewLifecycleOwner(), v::setProgress);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBag.dispose();
        vm.recordsList.onStop();
    }
}