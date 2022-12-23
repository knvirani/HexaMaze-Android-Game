package com.fourshape.numbermazes.rv_adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.CheckPackage;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.OpenExternalUrl;
import com.fourshape.numbermazes.utils.ScreenParams;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter {

    private AppColor appColor;

    private final ArrayList<ContentModal> contentModalArrayList;

    public ContentAdapter (ArrayList<ContentModal> contentModalArrayList) {
        this.contentModalArrayList = contentModalArrayList;
        appColor = new AppColor();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == ContentType.APPLICATION) {

            view = layoutInflater.inflate(R.layout.dynamic_apps_info, null);
            return new AppInfo(view);

        } else if (viewType == ContentType.DIVIDER) {

            view = layoutInflater.inflate(R.layout.dynamic_divider, null);
            return new Divider(view);

        } else if (viewType == ContentType.DEVELOPER_INFO) {

            view = layoutInflater.inflate(R.layout.dynamic_developer_info, null);
            return new DeveloperInfo(view);

        } else {

            view = layoutInflater.inflate(R.layout.dynamic_default, null);
            return new Default(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        int contentType = holder.getItemViewType();

        if (contentType == ContentType.APPLICATION) {

            ((AppInfo)holder).setViews(position);

        } else if (contentType == ContentType.DIVIDER) {

            ((Divider)holder).setViews(position);

        } else if (contentType == ContentType.DEVELOPER_INFO) {

            ((DeveloperInfo)holder).setViews(position);

        } else {

            ((Default)holder).setViews(position);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return contentModalArrayList.get(position).getContentType();
    }

    @Override
    public int getItemCount() {
        return contentModalArrayList.size();
    }

    private static void setLinearLayoutCompactParams (View itemView) {

        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ScreenParams.getDisplayWidthPixels(itemView.getContext()), LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(layoutParams);

    }

    class DeveloperInfo extends RecyclerView.ViewHolder {

        public DeveloperInfo(@NonNull @NotNull View itemView) {
            super(itemView);
            setLinearLayoutCompactParams(itemView);
        }

        private void setViews (int position) {

            appColor.setThemeId(new SharedData(itemView.getContext()).getAppCurrentTheme());
            itemView.setBackgroundColor(this.itemView.getContext().getColor(appColor.getAppBackgroundColor()));

            ((TextView)itemView.findViewById(R.id.header_title)).setTextColor(itemView.getContext().getColor(appColor.getDynamicHeaderTitleTextColor()));
            ((TextView)itemView.findViewById(R.id.header_desc)).setTextColor(itemView.getContext().getColor(appColor.getDynamicHeaderDescTextColor()));

        }

    }

    class AppInfo extends RecyclerView.ViewHolder {

        public AppInfo(@NonNull @NotNull View itemView) {
            super(itemView);
            setLinearLayoutCompactParams(itemView);
        }

        private void setViews (int position) {

            appColor.setThemeId(new SharedData(itemView.getContext()).getAppCurrentTheme());

            itemView.setBackgroundColor(this.itemView.getContext().getColor(appColor.getAppBackgroundColor()));

            ImageView appLogoIV = itemView.findViewById(R.id.app_logo);
            TextView appTitleTV = itemView.findViewById(R.id.app_title);
            MaterialButton actionMB = itemView.findViewById(R.id.open_app_mb);

            appTitleTV.setTextColor(itemView.getContext().getColor(appColor.getAppListAppTitleTextColor()));
            actionMB.setBackgroundTintList(ColorStateList.valueOf(this.itemView.getContext().getColor(appColor.getPrimaryBtnBackgroundColor())));
            actionMB.setTextColor(this.itemView.getContext().getColor(appColor.getPrimaryBtnTextColor()));

            int appLogoDrawableId = contentModalArrayList.get(position).getAppLogoId();
            String appTitle = contentModalArrayList.get(position).getAppTitle();
            String appPkg = contentModalArrayList.get(position).getAppPackage();

            appLogoIV.setImageDrawable(itemView.getContext().getDrawable(appLogoDrawableId));
            appTitleTV.setText(appTitle);

            if (CheckPackage.isFound(itemView.getContext(), appPkg)) {

                actionMB.setText(R.string.open_app);
                actionMB.setIcon(itemView.getContext().getDrawable(R.drawable.ic_right_arrow));

                actionMB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openApp(appPkg);
                    }
                });

            } else {

                actionMB.setText(R.string.get_app);
                actionMB.setIcon(itemView.getContext().getDrawable(R.drawable.ic_download));

                actionMB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String appUrl = "https://play.google.com/store/apps/details?id=" + appPkg;
                        openPlayStore(appUrl);

                    }
                });

            }

        }

        private void openPlayStore (String appLink) {

            try {
                if (itemView.getContext() != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
                    browserIntent.setPackage("com.android.vending");
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startActivity(browserIntent);
                }
            } catch (Exception e){

                MakeLog.exception(e);

                if (itemView.getContext() != null) {
                    Toast.makeText(itemView.getContext(), "Can't open.", Toast.LENGTH_SHORT).show();
                }

            }

        }

        private void openApp (String appPkg) {

            try {
                if (itemView.getContext() != null) {
                    Intent browserIntent = itemView.getContext().getPackageManager().getLaunchIntentForPackage(appPkg);
                    itemView.getContext().startActivity(browserIntent);
                }
            } catch (Exception e){

                MakeLog.exception(e);

                if (itemView.getContext() != null) {
                    Toast.makeText(itemView.getContext(), "Can't open.", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    class Default extends RecyclerView.ViewHolder {

        public Default(@NonNull @NotNull View itemView) {
            super(itemView);
            setLinearLayoutCompactParams(itemView);
        }

        private void setViews (int position) {}

    }

    class Divider extends RecyclerView.ViewHolder {

        public Divider(@NonNull @NotNull View itemView) {
            super(itemView);
            setLinearLayoutCompactParams(itemView);
        }

        private void setViews (int position) {

            appColor.setThemeId( new SharedData(itemView.getContext()).getAppCurrentTheme());

            itemView.findViewById(R.id.divider_view).setBackgroundColor(this.itemView.getContext().getColor(appColor.getContentDividerColor()));

        }

    }

}
