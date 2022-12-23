package com.fourshape.numbermazes.utils;

import com.fourshape.numbermazes.R;

public class AppColor {

    private int themeId;

    public AppColor () {
        themeId = -1;
    }

    public void setThemeId (int themeId) {
        this.themeId = themeId;
    }

    public int getPrefilledCellBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_prefilled_cell_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_prefilled_cell_bg;

        return R.color.default_prefilled_cell_bg;
    }

    public int getPrefilledCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_prefilled_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_prefilled_cell_text;

        return R.color.default_prefilled_cell_text;

    }

    public int getUserFilledCellBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_userfilled_cell_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_userfilled_cell_bg;

        return R.color.default_userfilled_cell_bg;
    }

    public int getUserFilledCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_userfilled_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_userfilled_cell_text;

        return R.color.default_userfilled_cell_text;

    }

    public int getCellBorderColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_border;

        return R.color.default_cell_border;

    }

    public int getHintCellBorderColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_hint_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_hint_border;

        return R.color.default_cell_hint_border;
    }

    public int getCellTapBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_tap_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_tap_bg;


        return R.color.default_cell_tap_bg;
    }

    public int getCellTapTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_tap_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_tap_text;

        return R.color.default_cell_tap_text;
    }

    public int getWrongCellBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_wrong_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_wrong_bg;

        return R.color.default_cell_wrong_bg;
    }

    public int getWrongCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_wrong_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_wrong_text;

        return R.color.default_cell_wrong_text;
    }

    public int getGameControlIconTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_control_icon_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_control_icon_tint;

        return R.color.default_control_icon_tint;
    }

    public int getGameTimeTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_time_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_time_text;

        return R.color.default_game_time_text;
    }

    public int getTargetCellBorderColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_target_cell_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_target_cell_border;

        return R.color.default_target_cell_border;
    }

    public int getTargetCellBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_target_cell_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_target_cell_bg;

        return R.color.default_target_cell_bg;
    }

    public int getTargetCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_target_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_target_cell_text;

        return R.color.default_target_cell_text;
    }

    public int getUndoControlTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_undo_control_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_undo_control_tint;


        return R.color.default_undo_control_tint;
    }

    public int getUndoControlLimitTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_undo_control_limit_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_undo_control_limit_text;

        return R.color.default_undo_control_limit_text;
    }

    public int getAvailableLifeIconTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_available_life;
        else if (themeId ==AppThemes.DARK)
            return R.color.theme_1_available_life;

        return R.color.default_available_life;
    }

    public int getLostLifeIconTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_lost_life;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_lost_life;

        return R.color.default_lost_life;
    }

    public int getGameStatusBarBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_status_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_status_bg;

        return R.color.default_game_status_bg;
    }

    public int getGameBottomBarBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_bottom_bar_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_bottom_bar_bg;

        return R.color.default_game_bottom_bar_bg;
    }

    public int getContentDividerColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_content_divider;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_bottom_bar_bg;

        return R.color.default_game_bottom_bar_bg;
    }

    public int getNormalDividerColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_normal_divider;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_normal_divider;

        return R.color.default_normal_divider;
    }

    public int getPopupCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_card_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_card_bg;

        return R.color.default_popup_card_bg;
    }

    public int getSelectedItemBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_selected_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_selected_bg;


        return R.color.default_selected_bg;
    }

    public int getSelectedItemTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_selected_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_selected_text;


        return R.color.default_selected_text;
    }

    public int getUnselectedItemBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_unselected_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_unselected_bg;

        return R.color.default_unselected_bg;
    }

    public int getUnselectedItemTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_unselected_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_unselected_text;

        return R.color.default_unselected_text;
    }

    public int getConnectorLineColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_connector_line;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_connector_line;


        return R.color.default_connector_line;
    }

    public int getWinAnimationConnectorLine () {

        if (themeId == AppThemes.DAY)
            return R.color.default_win_animation_connector_line;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_win_animation_connector_line;

        return R.color.default_win_animation_connector_line;
    }

    public int getEasyLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_easy_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_easy_level_bg;

        return R.color.default_easy_level_bg;
    }

    public int getMediumLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_medium_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_medium_level_bg;

        return R.color.default_medium_level_bg;
    }

    public int getHardLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_hard_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_hard_level_bg;

        return R.color.default_hard_level_bg;

    }

    public int getLegendLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_legend_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_legend_level_bg;

        return R.color.default_legend_level_bg;
    }

    public int getDifficultyLevelTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_difficulty_level_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_difficulty_level_title;


        return R.color.default_difficulty_level_title;
    }

    public int getProgressCircularIndicatorColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_circular_progress_indicator;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_circular_progress_indicator;

        return R.color.default_circular_progress_indicator;
    }

    public int getPopupTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_title;


        return R.color.default_popup_title;
    }

    public int getPopupBodyTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_body_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_body_text;


        return R.color.default_popup_body_text;
    }

    public int getPopupLinkColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_policy_link;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_policy_link;


        return R.color.default_policy_link;
    }

    public int getPopupRatingsBarColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_ratings_bar;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_ratings_bar;

        return R.color.default_popup_ratings_bar;
    }

    public int getPopupPrimaryButtonBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_primary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_primary_btn_bg;

        return R.color.default_popup_primary_btn_bg;
    }

    public int getPopupPrimaryButtonTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_primary_btn_text;


        return R.color.default_popup_primary_btn_text;
    }

    public int getPopupSecondaryButtonBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_secondary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_secondary_btn_bg;

        return R.color.default_popup_secondary_btn_bg;
    }

    public int getPopupSecondaryButtonTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_secondary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_secondary_btn_text;


        return R.color.default_popup_secondary_btn_text;
    }

    public int getAppBarBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_bg;

        return R.color.default_app_bar_bg;
    }

    public int getAppBarTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_text;

        return R.color.default_app_bar_text;
    }

    public int getAppBarIconTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_icon_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_icon_tint;


        return R.color.default_app_bar_icon_tint;
    }

    public int getBottomNavigationBarDividerBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_bottom_navigation_bar_divider;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_bottom_navigation_bar_divider;


        return R.color.default_bottom_navigation_bar_divider;
    }

    public int getAppBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_background;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_background;


        return R.color.default_app_background;
    }

    public int getAppListAppTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_apps_list_app_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_apps_list_app_title;


        return R.color.default_apps_list_app_title;
    }

    public int getMoreAppsBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_about_more_apps_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_about_more_apps_btn_bg;


        return R.color.default_about_more_apps_btn_bg;
    }

    public int getBottomNavigationItemActiveTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_bottom_nav_item_active_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_bottom_nav_item_active_tint;

        return R.color.default_bottom_nav_item_active_tint;
    }

    public int getHomeAppTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_home_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_home_title;


        return R.color.default_app_home_title;
    }

    public int getHomeDailyPlayCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_play_cv_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_play_cv_bg;


        return R.color.default_daily_play_cv_bg;
    }

    public int getHomeDailyDayTextBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_day_ll_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_day_ll_bg;


        return R.color.default_daily_day_ll_bg;
    }

    public int getHomeDailyDayTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_day_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_day_text;

        return R.color.default_daily_day_text;
    }

    public int getHomeDailyMonthTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_month_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_month_text;

        return R.color.default_daily_month_text;
    }

    public int getHomeDailyPlayRightArrowTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_right_arrow_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_right_arrow_tint;

        return R.color.default_home_page_right_arrow_tint;
    }

    public int getHomePrimaryBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_primary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_primary_btn_bg;

        return R.color.default_home_page_primary_btn_bg;
    }

    public int getHomePrimaryBtnTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_primary_btn_text;


        return R.color.default_home_page_primary_btn_text;
    }

    public int getHomeSecondaryBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_secondary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_secondary_btn_bg;


        return R.color.default_home_page_secondary_btn_bg;
    }

    public int getHomeSecondaryBtnTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_secondary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_secondary_btn_text;

        return R.color.default_home_page_secondary_btn_text;
    }

    public int getHomeStatsCardTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_text;

        return R.color.default_stats_text;
    }

    public int getHomeStatsCardIconTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_text;

        return R.color.default_stats_text;
    }

    public int getHomeStatsCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_cv_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_cv_bg;

        return R.color.default_stats_cv_bg;
    }

    public int getStatsTabIndicatorColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_tab_indicator;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_tab_indicator;


        return R.color.default_stats_tab_indicator;
    }

    public int getStatsTabTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_tab_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_tab_text;


        return R.color.default_stats_tab_text;
    }

    public int getDynamicHeaderTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_dynamic_view_header_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_dynamic_view_header_title;

        return R.color.default_dynamic_view_header_title;
    }

    public int getDynamicHeaderDescTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_dynamic_view_header_desc;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_dynamic_view_header_desc;


        return R.color.default_dynamic_view_header_desc;
    }

    public int getPrimaryBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_primary_btn_bg;

        return R.color.default_primary_btn_bg;
    }

    public int getPrimaryBtnTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_primary_btn_text;


        return R.color.default_primary_btn_text;
    }

    public int getCalendarWeekDayRowBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_calendar_week_day_row_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_calendar_week_day_row_bg;


        return R.color.default_calendar_week_day_row_bg;
    }

    public int getCalendarWeekDayTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_calendar_week_day_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_calendar_week_day_title;

        return R.color.default_calendar_week_day_title;
    }

    public int getCalendarDayColorTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_calendar_day;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_calendar_day;

        return R.color.default_calendar_day;
    }

    public int getInvalidCalendarDateTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_invalid_calendar_date;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_invalid_calendar_date;


        return R.color.default_invalid_calendar_date;
    }

    public int getOptionMenuTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_options_menu_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_options_menu_text;

        return R.color.default_options_menu_text;
    }

    public int getDailyPageMonthTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_month_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_month_text;


        return R.color.default_daily_month_text;
    }

    public int getDailyPageYearTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_year_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_year_text;


        return R.color.default_daily_year_text;
    }

    public int getDynamicStatsCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_card_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_card_bg;


        return R.color.default_stats_card_bg;
    }

    public int getDynamicStatsTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_title_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_title_text;


        return R.color.default_stats_title_text;
    }

    public int getDynamicStatsTitleBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_title_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_title_bg;


        return R.color.default_stats_title_bg;
    }

    public int getDynamicStatsValueTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_value_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_value_text;

        return R.color.default_stats_value_text;
    }

    public int getSwitchTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_text;


        return R.color.default_switch_text;
    }

    public int getSwitchOnTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_tint;


        return R.color.default_switch_tint;
    }

    public int getSwitchTrackUncheckedColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_track_normal;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_track_normal;


        return R.color.default_switch_track_normal;
    }

    public int getSwitchTrackCheckedColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_track_checked;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_track_checked;


        return R.color.default_switch_track_checked;
    }

    public int getSwitchSuggestionTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_suggestion_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_suggestion_text;


        return R.color.default_switch_suggestion_text;
    }

    public int getSuccessTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_success_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_success_tint;

        return R.color.default_success_tint;
    }

    public int getErrorTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_error_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_error_tint;

        return R.color.default_error_tint;
    }

    public int getShareViewBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_share_view_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_share_view_bg;

        return R.color.default_share_view_bg;
    }

    public int getShareViewAppTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_share_view_app_title_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_share_view_app_title_text;


        return R.color.default_share_view_app_title_text;
    }

    public int getGameSessionLinearProgressIndicatorColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_session_linear_progress_indicator;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_session_linear_progress_indicator;

        return R.color.default_game_session_linear_progress_indicator;
    }

    public int getGameSessionLinearProgressTrackColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_session_linear_progress_track;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_session_linear_progress_track;

        return R.color.default_game_session_linear_progress_track;
    }

    public int getNextStepCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_next_step_cv_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_next_step_cv_bg;

        return R.color.default_next_step_cv_bg;

    }

    public int getNextStepTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_next_step_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_next_step_text;

        return R.color.default_next_step_text;

    }

    public int getHowtoplayCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_howtoplay_card_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_howtoplay_card_bg;

        return R.color.default_howtoplay_card_bg;

    }

    public int getHowtoplayTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_howtoplay_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_howtoplay_title;

        return R.color.default_howtoplay_title;

    }

}
