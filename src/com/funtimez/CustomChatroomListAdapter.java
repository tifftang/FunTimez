package com.funtimez;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomChatroomListAdapter extends ArrayAdapter<String>{

	private Context context;
	private List<String> objects;
	
	public CustomChatroomListAdapter(Context context, List objects) {
		super(context, -1, objects);
		this.context = context;
		this.objects = objects;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.chatroom_list_row, parent, false);
		TextView textView = (TextView) row.findViewById(R.id.chatroomName);
		ImageView imageView = (ImageView) row.findViewById(R.id.usersOfChatroom);
		textView.setText(objects.get(position));

		return row;
	  }
}
