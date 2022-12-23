package com.fourshape.numbermazes.page_adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fourshape.numbermazes.fragments.stats.EasyLevelStatsFragment;
import com.fourshape.numbermazes.fragments.stats.HardLevelStatsFragment;
import com.fourshape.numbermazes.fragments.stats.LegendLevelStatsFragment;
import com.fourshape.numbermazes.fragments.stats.MediumLevelStatsFragment;

import org.jetbrains.annotations.NotNull;

public class StatisticsAdapter extends FragmentStateAdapter {

    private int totalTabs;

    public StatisticsAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle, int totalTabs) {
        super(fragmentManager, lifecycle);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new EasyLevelStatsFragment();

            case 1:
                return new MediumLevelStatsFragment();

            case 2:
                return new HardLevelStatsFragment();

            case 3:
                return new LegendLevelStatsFragment();

        }

        return new EasyLevelStatsFragment();

    }



    @Override
    public int getItemCount() {
        return totalTabs;
    }

}
