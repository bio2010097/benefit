package com.android.benefit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.benefit.nfcread.NFCTagReadActivity;
import com.android.benefit.nfcwrite.NFCTagWriteActivity;

public class MainActivity extends ActionBarActivity {

	ActionBarDrawerToggle actionBarDrawerToggle;
	DrawerLayout drawerLayout;
	ListView left_drawer;

	private Map<String, String[]> left_nav = new HashMap<String, String[]>();
	private ArrayList<Class> left_nav_link = new ArrayList<Class>();

	SectionedAdapter adapter;

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
		left_drawer = (ListView) findViewById(R.id.left_drawer);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				toolbar, R.string.app_name, R.string.app_name);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		init_nav();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
		case R.id.tag_read:
			Intent intent = new Intent(MainActivity.this,
					NFCTagReadActivity.class);
			startActivity(intent);
			break;
		case R.id.tag_write:
			Intent intent2 = new Intent(MainActivity.this,
					NFCTagWriteActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void init_nav() {

		adapter = new SectionedAdapter() {
			protected View getHeaderView(String caption, int index,
					View convertView, ViewGroup parent) {
				TextView result = (TextView) convertView;

				if (convertView == null) {
					result = (TextView) getLayoutInflater().inflate(
							R.layout.layout_listview_header, null);
				}

				result.setText(caption);

				return (result);
			}
		};

		String[] nav_header = getResources().getStringArray(R.array.nav_header);

		for (String header : nav_header) {
			left_nav.put(header, getResources().getStringArray(R.array.nav_1));
		}

		left_nav_link.add(TagDiscoveredActivity.class);

		adapter.addSection(nav_header[0], new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.nav_1)));

		adapter.addSection(nav_header[1], new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.nav_2)));

		adapter.addSection(nav_header[2], new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.nav_3)));

		left_drawer.setAdapter(adapter);
		left_drawer.setOnItemClickListener(mItemClickListener);

	}

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = null;

			switch (position) {
			case 1:
				intent = new Intent(MainActivity.this, TagDiscoveredActivity.class);
				startActivity(intent);
			case 2:				
			case 3:				
			case 5:				
			case 6:		
			case 7:
				break;

			default:
				break;
			}

		}

	};

}
