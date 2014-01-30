package com.sunil.upload;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsRowAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public NewsRowAdapter(Activity act, int resource, List<Item> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvName = (TextView) view.findViewById(R.id.tvname);
		holder.tvDescription = (TextView) view.findViewById(R.id.tvdescription);
		holder.tvCreated = (TextView) view.findViewById(R.id.tvcreated);
		holder.tvUpdated = (TextView) view.findViewById(R.id.tvupdated);
		holder.imgView = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

		if (holder.tvName != null && null != objBean.getRegPlate()
				&& objBean.getRegPlate().trim().length() > 0) {
			holder.tvName.setText(Html.fromHtml(objBean.getRegPlate()));
		}
		if (holder.tvDescription != null && null != objBean.getDescription()
				&& objBean.getDescription().trim().length() > 0) {
			holder.tvDescription.setText(Html.fromHtml(objBean.getDescription()));
		}
		if (holder.tvUpdated != null && null != objBean.getUpdated_at()
				&& objBean.getUpdated_at().trim().length() > 0) {
			holder.tvUpdated.setText(Html.fromHtml(objBean.getUpdated_at()));
		}
		if (holder.tvCreated != null && null != objBean.getCreated_at()
				&& objBean.getCreated_at().trim().length() > 0) {
			holder.tvCreated.setText(Html.fromHtml(objBean.getCreated_at()));
		}
		if (holder.imgView != null) {
			if (null != objBean.getLink()
					&& objBean.getLink().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(activity));
				imageLoader.displayImage(objBean.getLink(), holder.imgView,
						options, new ImageLoadingListener() {
							@Override
							public void onLoadingComplete() {
								pbar.setVisibility(View.INVISIBLE);

							}

							@Override
							public void onLoadingFailed() {
								pbar.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onLoadingStarted() {
								pbar.setVisibility(View.VISIBLE);

							}
						});

			} else {
				holder.imgView.setImageResource(R.drawable.ic_launcher);
			}
		}

		return view;
	}

	public class ViewHolder {

		public TextView tvName, tvDescription, tvCreated, tvUpdated;
		private ImageView imgView;
		private ProgressBar pbar;

	}

}