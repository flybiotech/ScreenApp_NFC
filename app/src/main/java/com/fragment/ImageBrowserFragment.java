package com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;


/**
 * Created by gyl1 on 3/10/16.
 */
public class ImageBrowserFragment extends Fragment implements View.OnClickListener{

    private String mUrl;
    public static final String BUNDLE_TITLE = "title";
    public static final String BUNDLE_URL = "url";
    private PhotoView imageView=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(BUNDLE_URL);
        }

        RelativeLayout layout = new RelativeLayout(getActivity());

        imageView = new PhotoView(getActivity());
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(imageView, lp1 );
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        Glide.with(getActivity())
//                .load(mUrl)
//                .into(imageView);
//        Picasso.with(getActivity())
//                .load(mUrl)
//                .into(imageView);

        Glide.with(getActivity())//图片加载框架
                .load(mUrl)//图片的路径
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .override(200,200)
//                .crossFade()
                .into(imageView);
//        ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
//        imageView.setImageUrl(mUrl, imageLoader);
        // imageView.setId(1);
        imageView.setOnClickListener(this);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.BELOW, imageView.getId());
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);

        return layout;

    }

    public static ImageBrowserFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, url);
        ImageBrowserFragment fragment = new ImageBrowserFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
        this.getActivity().overridePendingTransition( R.anim.alpha_in, R.anim.alpha_out );
    }


    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Glide.get(getActivity()).clearMemory();

    }
}
