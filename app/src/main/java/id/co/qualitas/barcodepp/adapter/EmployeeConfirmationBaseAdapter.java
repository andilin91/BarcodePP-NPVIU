package id.co.qualitas.barcodepp.adapter;


import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.activity.ConfirmationActivity;
import id.co.qualitas.barcodepp.activity.ConfirmationOkActivity;
import id.co.qualitas.barcodepp.constants.Constants;
import id.co.qualitas.barcodepp.helper.Helper;
import id.co.qualitas.barcodepp.model.ListEmployee;
import id.co.qualitas.barcodepp.model.PWOResponse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class EmployeeConfirmationBaseAdapter extends BaseAdapter{
	private List<ListEmployee> searchArrayList;
	private LayoutInflater mInflater;
	private ConfirmationActivity mContext;
	private int view = 0;
	protected Context context;
	private boolean[] itemChecked;
	private Address fullObject;
	private Address object;

	public EmployeeConfirmationBaseAdapter(ConfirmationActivity confirmationActivity, int view) {
		searchArrayList = new ArrayList<ListEmployee>();
		mContext = confirmationActivity;
		mInflater = LayoutInflater.from(confirmationActivity);
		this.view = view;

	}

	public void addAllItem(List<ListEmployee> listEmployee) {
		searchArrayList.addAll(listEmployee);
	}

	public void addItem(ListEmployee info) {
		searchArrayList.add(info);
	}

	public int getCount() {
		return searchArrayList.size();
	}

	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			// itemChecked = new boolean[searchArrayList.size()];
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.employee_row_view, null);
			holder.txtEmployeeId = (TextView) convertView
					.findViewById(R.id.txtEmployeeID);
			holder.txtQty = (TextView) convertView
					.findViewById(R.id.txtQty);
			holder.txtUom = (TextView) convertView
					.findViewById(R.id.txtUom);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtEmployeeId.setText(searchArrayList.get(position).getIdEmployee().substring(4));
		
		if(Helper.getItemParam(Constants.PROD_TYPE).toString().equals("FG")){
			holder.txtQty.setText(searchArrayList.get(position).getYield());
			holder.txtUom.setText("(EA)");
		}else{
			holder.txtQty.setText(searchArrayList.get(position).getDuration());
			holder.txtUom.setText("(Minutes)");
		}
		return convertView;

	}

	static class ViewHolder {
		TextView txtEmployeeId;
		TextView txtQty;
		TextView txtUom;

	}

	public void clearAllItem(){
		searchArrayList.clear();
	}
}