package io.github.rosemoe.editor;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.ArrayList;
import android.widget.LinearLayout;
import com.github.angads25.filepicker.controller.DialogSelectionListener ;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.angads25.filepicker.*;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import java.net.*;
import java.lang.reflect.*;
import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.langs.java.*;
import io.github.rosemoe.editor.langs.kotlin.*;
import io.github.rosemoe.editor.widget.schemes.*;
import io.github.rosemoe.editor.util.PackageImporter;
import org.apache.commons.lang3.StringUtils;

public class MainActivity extends Activity {
	
	private ArrayList<String> methods = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private CodeEditor mEditor;
	
	private FilePickerDialog pick;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		mEditor = findViewById(R.id.mEditor);
	}
	
	private void initializeLogic() {
		mEditor.setTypeface(Typeface.createFromAsset(getAssets(), "Mono.otf"));
		
		mEditor.setTypefaceLineNumber(Typeface.createFromAsset(getAssets(), "SourceCodePro-It.otf"));
		
		mEditor.setTypefacedCompletion(true);
		mEditor.setColorScheme(new SchemeDarcula());
		
		mEditor.setOverScrollEnabled(false);
		
		mEditor.setEditorLanguage(new JavaLanguage());
		
		mEditor.setTextSize(12);
		DialogProperties pickp = new DialogProperties();
		pickp.selection_mode = DialogConfigs.SINGLE_MODE;
		pickp.selection_type = DialogConfigs.FILE_SELECT;
		pickp.root = new java.io.File("/storage/emulated/0/");
		pickp.error_dir = new java.io.File("/storage/emulated/0/");
		pickp.offset = new java.io.File("/storage/emulated/0/");
		pickp.extensions = new String[] {".class"};
		SketchwareUtil.showMessage(getApplicationContext(), "Select a .class file");
		pick = new FilePickerDialog(MainActivity.this,pickp);
		pick.setTitle("Select a class file");
		pick.setPositiveBtnName("Select");
		pick.setDialogSelectionListener(new DialogSelectionListener() {
			@Override public void onSelectedFilePaths(String[] files) {
				test.set(Arrays.asList(files).get((int) 0).toString());
			} 
		});
		pick.show();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int _id = item.getItemId();
		final String _title = (String) item.getTitle();
		switch(_title) {
			case "Kotlin": {
				mEditor.setEditorLanguage(new KotlinLanguage());
				break;
			}
			case "Java": {
				mEditor.setEditorLanguage(new JavaLanguage());
				break;
			}
			case "None": {
				mEditor.setEditorLanguage(null);
				break;
			}
			case "Darcula": {
				mEditor.setColorScheme(new SchemeDarcula());
				break;
			}
			case "Notepad": {
				mEditor.setColorScheme(new SchemeNotepadXX());
				break;
			}
			case "Github": {
				mEditor.setColorScheme(new SchemeGitHub());
				break;
			}
			case "VS Code": {
				mEditor.setColorScheme(new SchemeVS2019());
				break;
			}
			case "Eclipse": {
				mEditor.setColorScheme(new SchemeEclipse());
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu submenu1 = menu.addSubMenu("Language");
		submenu1.add(0, 0, 0, "Kotlin");
		submenu1.add(0, 1, 0, "Java");
		submenu1.add(0, 2, 0, "None");
		SubMenu submenu2 = menu.addSubMenu("Theme");
		submenu2.add(0, 0, 0, "Darcula");
		submenu2.add(0, 1, 0, "Notepad");
		submenu2.add(0, 2, 0, "Github");
		submenu2.add(0, 3, 0, "Eclipse");
		submenu2.add(0, 4, 0, "VS Code");
		return super.onCreateOptionsMenu(menu);
	}
}