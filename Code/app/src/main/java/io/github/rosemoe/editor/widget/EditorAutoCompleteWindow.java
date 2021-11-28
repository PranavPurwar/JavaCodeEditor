package io.github.rosemoe.editor.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.BackgroundColorSpan;

import io.github.rosemoe.editor.R;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.Cursor;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.struct.ResultItem;
import io.github.rosemoe.editor.util.*;

import java.util.ArrayList;
import java.util.List;
import android.widget.RelativeLayout;

/**
* Auto complete window for editing code quicker
*
* @author Rose
*/
public class EditorAutoCompleteWindow extends EditorBasePopupWindow {
	private final CodeEditor mEditor;
	private final ListView mListView;
	private final GradientDrawable mBg;
	
	private int mCurrent = 0;
	private long mRequestTime;
	public static String mLastPrefix;
	private AutoCompleteProvider mProvider;
	private boolean mLoading;
	
	protected boolean mCancelShowUp = false;
	
	@Override
	public void show() {
		if (mCancelShowUp) {
			return;
		}
		super.show();
	}
	
	/**
	* Create a panel instance for the given editor
	*
	* @param editor Target editor
	*/
	public EditorAutoCompleteWindow(CodeEditor editor) {
		super(editor);
		mEditor = editor;
		RelativeLayout layout = new RelativeLayout(mEditor.getContext());
		mListView = new ListView(mEditor.getContext());
		LinearLayout.LayoutParams mListViewLP = new LinearLayout.LayoutParams(-1, -2);
		int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		6, editor.getContext().getResources().getDisplayMetrics()));
		mListViewLP.setMargins(margin, margin, margin, margin);
		layout.setPadding(2, 2, 2, 2);
		
		layout.addView(mListView, mListViewLP);
		setContentView(layout);
		GradientDrawable gd = new GradientDrawable();
		gd.setCornerRadius(1);
		layout.setBackgroundDrawable(gd);
		mBg = gd;
		applyColor();
		mListView.setDividerHeight(0);
		setLoading(true);
		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int position, long p4) {
				try {
					select(position);
				} catch (Exception e) {
					Toast.makeText(mEditor.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	* Set a auto completion items provider
	*
	* @param p New provider.can not be null
	*/
	public void setProvider(AutoCompleteProvider p) {
		mProvider = p;
	}
	
	/**
	* Apply colors for self
	*/
	public void applyColor() {
		EditorColorScheme colors = mEditor.getColorScheme();
		mBg.setStroke(2, 0xff575757);
		mBg.setColor(0xff2b2b2b);
	}
	
	/**
	* Change layout to loading/idle
	*
	* @param state Whether loading
	*/
	public void setLoading(boolean state) {
		// no longer needed
	}
	
	/**
	* Move selection down
	*/
	public void moveDown() {
		if (mCurrent + 1 >= mListView.getAdapter().getCount()) {
			return;
		}
		mCurrent++;
		((ItemAdapter) mListView.getAdapter()).notifyDataSetChanged();
	}
	
	/**
	* Move selection up
	*/
	public void moveUp() {
		if (mCurrent - 1 < 0) {
			return;
		}
		mCurrent--;
		((ItemAdapter) mListView.getAdapter()).notifyDataSetChanged();
	}
	
	/**
	* Select current position
	*/
	public void select() {
		select(mCurrent);
	}
	
	/**
	* Select the given position
	*
	* @param pos Index of auto complete item
	*/
	public void select(int pos) {
		ResultItem item = ((ItemAdapter) mListView.getAdapter()).getItem(pos);
		Cursor cursor = mEditor.getCursor();
		if (!cursor.isSelected()) {
			mCancelShowUp = true;
			mEditor.getText().delete(cursor.getLeftLine(), cursor.getLeftColumn() - mLastPrefix.length(), cursor.getLeftLine(), cursor.getLeftColumn());
			if (item.type != ResultItem.TYPE_LOCAL_METHOD) {
				if (item.type == ResultItem.TYPE_METHOD) {
					cursor.onCommitText(item.commit);
					if (item.desc.equals("void")) {
						cursor.onCommitText(";");
						if (!item.label.endsWith("()")) {
						mEditor.getCursor().set(mEditor.getCursor().getLeftLine(), mEditor.getCursor().getLeftColumn() - 2);
						}
						
					} else if (!item.label.endsWith("()")) {
						mEditor.getCursor().set(mEditor.getCursor().getLeftLine(), mEditor.getCursor().getLeftColumn() - 1);
					}
				} else if (item.type == ResultItem.TYPE_FIELD) {
					cursor.onCommitText(item.commit);
				} else if (item.type == ResultItem.TYPE_KEYWORD) {
					cursor.onCommitText(item.commit + " ");
				} else {
					cursor.onCommitText(item.commit);
				}
			} else {
				cursor.onCommitText(item.commit);
			}
			if ((item.mask & ResultItem.MASK_SHIFT_LEFT_TWICE) != 0) {
				mEditor.moveSelectionLeft();
				mEditor.moveSelectionLeft();
			}
			if ((item.mask & ResultItem.MASK_SHIFT_LEFT_ONCE) != 0) {
				mEditor.moveSelectionLeft();
			}
			mCancelShowUp = false;
		}
		mEditor.postHideCompletionWindow();
	}
	
	/**
	* Set prefix for auto complete analysis
	*
	* @param prefix The user's input code's prefix
	*/
	public void setPrefix(String prefix) {
		if (mCancelShowUp) {
			return;
		}
		setLoading(true);
		mLastPrefix = prefix;
		mRequestTime = System.currentTimeMillis();
		MatchThread mThread = new MatchThread(mRequestTime, prefix);
		mThread.start();
	}
	
	/**
	* Get prefix set
	*
	* @return The previous prefix
	*/
	public static String getPrefix() {
		return mLastPrefix;
	}
	
	private int maxHeight;
	
	public void setMaxHeight(int height) {
		maxHeight = height;
	}
	
	/**
	* Display result of analysis
	*
	* @param results     Items of analysis
	* @param requestTime The time that this thread starts
	*/
	private void displayResults(final List<ResultItem> results, long requestTime) {
		if (mRequestTime != requestTime) {
			return;
		}
		mEditor.post(new Runnable() {
			@Override
			public void run() {
				setLoading(false);
				if (results == null || results.isEmpty()) {
					hide();
					return;
				}
				mListView.setAdapter(new ItemAdapter(results));
				float newHeight = mEditor.getDpUnit() * 30 * results.size();
				if (isShowing()) update(-2, (int) Math.min(newHeight, maxHeight));
			}
		});
	}
	
	/**
	* Adapter to display results
	*
	* @author Rose
	*/
	@SuppressWarnings("CanBeFinal")
	private class ItemAdapter extends BaseAdapter {
		
		private List<ResultItem> mItems;
		
		private BitmapDrawable[] bmps;
		
		public ItemAdapter(List<ResultItem> items) {
			mItems = items;
		}
		
		@Override
		public int getCount() {
			return mItems.size();
		}
		
		@Override
		public ResultItem getItem(int pos) {
			return mItems.get(pos);
		}
		
		@Override
		public long getItemId(int pos) {
			return getItem(pos).hashCode();
		}
		
		@Override
		public View getView(int pos, View view, ViewGroup parent) {
			if (view == null) {
				view = LayoutInflater.from(mEditor.getContext()).inflate(R.layout.completion_result_item, parent, false);
			}
			ResultItem item = getItem(pos);
			TextView label = (TextView) view.findViewById(R.id.result_item_label);
			TextView desc = (TextView) view.findViewById(R.id.result_item_desc);
			if (mEditor.isTypefacedCompletion()) {
				desc.setTypeface(mEditor.getTypefaceText(), 0);
				label.setTypeface(mEditor.getTypefaceText(), 0);
			}
			label.setText(item.label);
			desc.setText(item.desc);
			view.setTag(pos);
			String partial = EditorAutoCompleteWindow.getPrefix();
			if (partial != null && partial.length() > 0) {
				if (item.label.startsWith(partial)) {
					ForegroundColorSpan span = new ForegroundColorSpan(0xFFCC7832);
					
					SpannableString spannable = new SpannableString(item.label);
					spannable.setSpan(span, 0, partial.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					label.setText(spannable);
					
				}
			}
			if (mCurrent == pos) {
				view.setBackgroundColor(0x40000000);
			} else {
				view.setBackgroundColor(0xff2b2b2b);
			}
			
			ImageView iv = (ImageView) view.findViewById(R.id.result_item_image);
			Kind kind;
			switch (item.type) {
				case ResultItem.TYPE_KEYWORD:
				kind = Kind.Keyword;
				break;
				case ResultItem.TYPE_LOCAL_METHOD:
				kind = Kind.LocalVariable;
				break;
				case ResultItem.TYPE_METHOD:
				kind = Kind.Method;
				break;
				case ResultItem.TYPE_FIELD:
				kind = Kind.Field;
				break;
				case ResultItem.TYPE_CLASS:
				kind = Kind.Class;
				break;
				default:
				kind = Kind.LocalVariable;
				break;
			}
			iv.setImageDrawable(new CircleDrawable(kind, mEditor.getContext()));
			iv.setClipToOutline(true);
			return view;
		}
	}
	
	/**
	* Analysis thread
	*
	* @author Rose
	*/
	private class MatchThread extends Thread {
		
		private final long mTime;
		private final String mPrefix;
		private final boolean mInner;
		private final TextAnalyzeResult mColors;
		private final int mLine;
		private final AutoCompleteProvider mLocalProvider = mProvider;
		
		public MatchThread(long requestTime, String prefix) {
			mTime = requestTime;
			mPrefix = prefix;
			mColors = mEditor.getTextAnalyzeResult();
			mLine = mEditor.getCursor().getLeftLine();
			mInner = (!mEditor.isHighlightCurrentBlock()) || (mEditor.getBlockIndex() != -1);
		}
		
		@Override
		public void run() {
			displayResults(mLocalProvider.getAutoCompleteItems(mPrefix, mInner, mColors, mLine), mTime);
		}
		
		
	}
	
}
