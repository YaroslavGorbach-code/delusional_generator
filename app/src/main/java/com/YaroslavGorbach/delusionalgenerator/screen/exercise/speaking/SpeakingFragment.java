package com.YaroslavGorbach.delusionalgenerator.screen.exercise.speaking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.YaroslavGorbach.delusionalgenerator.data.Repo;
import com.YaroslavGorbach.delusionalgenerator.util.AdMob;
import com.YaroslavGorbach.delusionalgenerator.R;

public class SpeakingFragment extends Fragment {
    private Button mButtonFinish;
    private TextView mWorldCounter;
    private int mWorldCount;
    private Button mStartRecordingButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises_category_1, container, false);
        mButtonFinish = view.findViewById(R.id.buttonFinishEx1);
        mWorldCounter = view.findViewById(R.id.world_counter);
        mStartRecordingButton = view.findViewById(R.id.buttonStartRecording);


        // init speaking exercise
        Repo repo = new Repo.RepoProvider().provideRepo();
        int exId = SpeakingFragmentArgs.fromBundle(requireArguments()).getIdEx();
        Chronometer chronometer = view.findViewById(R.id.chronometer);
        Chronometer chronometerOneWord = view.findViewById(R.id.chronometer_one_word);
        SpeakingVm vm = new ViewModelProvider(this, new SpeakingVm.SpeakingVmFactory(
                exId, repo, getResources(), chronometer, chronometerOneWord)).get(SpeakingVm.class);

        TextView shortDesc = view.findViewById(R.id.description_short);
        shortDesc.setText(vm.speakingEx.getShortDesc());

        ImageButton startPause = view.findViewById(R.id.button);
        startPause.setOnClickListener(v -> vm.speakingEx.startPauseChronometer());

        TextView  mWorld = view.findViewById(R.id.world_tv);
        vm.speakingEx.getWord().observe(getViewLifecycleOwner(), mWorld::setText);

        Button nextWord = view.findViewById(R.id.buttonNextWorld);
        nextWord.setOnClickListener(v ->  vm.speakingEx.nextWord());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*показ банера*/
        AdMob.showBanner(view.findViewById(R.id.banner));



//        /*Оброботка нажатия на кнопку начать и остановить запись голоса*/
//        mStartRecordingButton.setOnClickListener(v -> {
//                if (mViewModel.isRecording.getValue()){
//                    stopRecording();
//                }else {
//                    if(new Permissions().checkRecordPermission(getActivity())){
//                        startRecording();
//                        mStartRecordingButton.setClickable(false);
//                        new Handler().postDelayed(() -> mStartRecordingButton.setClickable(true), 1000);
//                    }
//                }
//
//        });


        /*Установка нового слова и обнуление секундомера
        который показывает время потраченое на одно слово */
//        mButtonNextWorld.setOnClickListener(v->{
//            mChronometer_1worldTime.setBase(SystemClock.elapsedRealtime());
//            mWorldCount++;
//            mWorldCounter.setText(String.format("%s/%s", mWorldCount, mViewModel.getMaxWorldCount(mIdEx)));
//        });

    }




//    /*В зависимости от айди упражнения устанавливаем в textView правельное слово или пару слов*/
//    @SuppressLint("WrongConstant")
//    private void changeWord(){
//        YoYo.with(Techniques.FadeIn)
//                .duration(400)
//                .playOn(mWorld);
//        mWorld.setText(mViewModel.getWord());
//        switch (mIdEx) {
//            case 1:
//                setThumbAndText();
//                animateThumb();
//                break;
//            case 6:
//                mButtonNextWorld.setTextSize(17);
//                break;
//            case 13:
//                mWorld.setTextSize(25);
//                break;
//            case 14:
//                mWorld.setTextSize(30);
//                break;
//        }
//    }

//    /*Уставнока значения борльшого пальца*/
//    private void setThumbAndText(){
//        mThumbAndText.setVisibility(View.VISIBLE);
//        mTextUnderThumb.setText(mViewModel.getThumbWord());
//        if (mTextUnderThumb.getText().equals("Аналогия")){
//            mThumb.setImageResource(R.drawable.ic_right);
//        }else if(mTextUnderThumb.getText().equals("Разобобщения")){
//            mThumb.setImageResource(R.drawable.ic_down);
//        }else if(mTextUnderThumb.getText().equals("Обобщение")){
//            mThumb.setImageResource(R.drawable.ic_up);
//        }
//
//    }


    @Override
    public void onStop() {
            super.onStop();
//                if (mViewModel.isRecording.getValue()){
//                    stopRecording();
//                }
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mChronometer_allTime.stop();
//        mChronometer_1worldTime.stop();
    }
}

