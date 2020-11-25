package com.YaroslavGorbach.delusionalgenerator.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.YaroslavGorbach.delusionalgenerator.Adapters.AudioListAdapter;
import com.YaroslavGorbach.delusionalgenerator.Database.Repo;
import com.YaroslavGorbach.delusionalgenerator.Fragments.Dialogs.DialogDeleteRecords;
import com.YaroslavGorbach.delusionalgenerator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class AudioListFragment extends Fragment  {

    private BottomSheetBehavior mBottomSheetBehavior;
    private RecyclerView mAudioList;
    private File[] mAllFiles;
    private AudioListAdapter mAudioListAdapter;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private boolean isPause = false;

    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFilename;

    private MaterialToolbar mToolbar;

    private ImageButton buttonAgo;
    private ImageButton buttonForward;

    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;
    private Repo mRepo;


    private File fileToPlay = null;

    private CoordinatorLayout mCoordinatorLayout;
    private AppCompatImageView mImageNoData;
    private TextView mTextViewNoData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.activity_audio_list, container, false);
        ConstraintLayout mPlayerSheet = view.findViewById(R.id.player_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mPlayerSheet);
        mAudioList = view.findViewById(R.id.audio_list_view);
        playBtn = view.findViewById(R.id.player_play_btn);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFilename = view.findViewById(R.id.player_filename);
        playerSeekbar = view.findViewById(R.id.player_seekbar);
        buttonAgo = view.findViewById(R.id.buttonAgo);
        buttonForward = view.findViewById(R.id.buttonForward);
        mCoordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        mImageNoData = view.findViewById(R.id.audio_fragment_image_nothing);
        mTextViewNoData = view.findViewById(R.id.audio_fragment_text_nothing);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mRepo = Repo.getInstance(getContext());
        mToolbar = getActivity().findViewById(R.id.toolbar_main_a);
        mToolbar.inflateMenu(R.menu.menu_recordings_del);
        mToolbar.getMenu().getItem(0).setVisible(true);

        new Thread(() -> {
            /*Получаем файлы из деректории*/
            String mPath = getContext().getExternalFilesDir("/").getAbsolutePath();
            File directory = new File(mPath);
            mAllFiles = directory.listFiles();

            /*Сортировка файлов по дате измененя*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (mAllFiles != null) {
                    Arrays.sort(mAllFiles, Comparator.comparingLong(File::lastModified).reversed());
                }
            }

            if (mAllFiles != null && mAllFiles.length > 0){
                mImageNoData.setVisibility(View.GONE);
                mTextViewNoData.setVisibility(View.GONE);
                mCoordinatorLayout.setVisibility(View.VISIBLE);
            }else {
                mImageNoData.setVisibility(View.VISIBLE);
                mTextViewNoData.setVisibility(View.VISIBLE);
                mCoordinatorLayout.setVisibility(View.GONE);
            }

        }).start();

        /*Показ банера*/
        AdView mAdView;
        mAdView = view.findViewById(R.id.adViewTab3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*Инициализация адаптера и лисенера который отвечает за нажатие на елемент списка*/
        mAudioListAdapter = new AudioListAdapter(mAllFiles, (file, position) -> {
            fileToPlay = file;
            if (isPlaying) {
             pauseAudio();
            } else {
                playAudio(fileToPlay);
            }
        });

        /*Настройка списка*/
        mAudioList.setHasFixedSize(true);
        mAudioList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAudioList.setAdapter(mAudioListAdapter);
        mAudioList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        // фикс бага который скрывает плеер если потянуть вниз
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (!isPlaying && !isPause){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //We cant do anything here for this app
            }
        });

        /*Настройка перемотки записи з помощу сик бара*/
        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
            }
        });

        /*Оброботка нажатия на кнопку пауза/плей в плеере*/
        playBtn.setOnClickListener(v -> {
            if (isPlaying) {
                pauseAudio();
            } else {
                if (fileToPlay != null) {
                    resumeAudio();
                }
            }
        });

        /*Перемотка назад*/
        buttonAgo.setOnClickListener(v -> {
            int progress = playerSeekbar.getProgress();
            progress -= 1000;
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(progress);
            }
        });

        /*Перемотка вперед*/
        buttonForward.setOnClickListener(v -> {
            int progress = playerSeekbar.getProgress();
            progress += 1000;
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(progress);
            }
        });

        return view;
    }

    private void resumeAudio() {
        if (mediaPlayer!=null){
            mediaPlayer.start();
            playBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_player_pause_btn, null));
            isPlaying = true;
            isPause = false;
            updateRunnable();
            seekbarHandler.postDelayed(updateSeekbar, 0);
        }
    }

    private void pauseAudio() {
        if (mediaPlayer!=null){
            mediaPlayer.pause();
            playBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_player_play_btn, null));
            isPlaying = false;
            isPause = true;
            seekbarHandler.removeCallbacks(updateSeekbar);
        }

    }


    private void playAudio(File fileToPlay) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_player_pause_btn, null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText("Играет...");
        isPlaying = true;
        isPause = false;
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        mediaPlayer.setOnCompletionListener(mp -> {
            stopAudio();
            playerHeader.setText("Закончено");
            isPlaying = false;
            isPause = false;
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    /*Метот для отображения прогреса в плеере */
    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    private void stopAudio() {
        playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_player_play_btn, null));
        playerHeader.setText("Остановлено");
        isPlaying = false;
        isPause = true;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.getMenu().getItem(0).setVisible(true);
        Log.println(Log.VERBOSE, "test", "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isPlaying) {
            stopAudio();
        }
    }

}