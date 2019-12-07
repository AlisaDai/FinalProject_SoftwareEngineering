package bcit.ca.easyexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<File> files = new ArrayList<>();
    private List<File> showedFiles = new ArrayList<>();
    private List<String> fileNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    @Override
    protected void onResume() {
        super.onResume();
        resetAll();
        EditText searchBar = findViewById(R.id.searchLine);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == KeyEvent.KEYCODE_ENTER){
                    String text = v.getText().toString();
                    search(text);
                    return true;
                }
                return false;
            }
        });
    }

    void resetAll() {
        super.onResume();
        files = new ArrayList<>();
        showedFiles = new ArrayList<>();
        fileNames = new ArrayList<>();
        getAllFile();
    }

    void search(String text){
        resetAll();
        for(File file: files){
            if(file.getName().contains(text)){
                showedFiles.add(file);
                fileNames.add(file.getName());
            }
        }
        ListView listView = findViewById(R.id.result);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, fileNames
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("currentPath", showedFiles.get(position).getPath());
                resetAll();
                startActivity(intent);
            }
        });
    }

    void getAllFile(){
        Intent intent = getIntent();
        String path = intent.getStringExtra("currentPath");
        Log.d("current path of search", path);
        if(path == null){
            path = Environment.getRootDirectory().getPath();
        }
        Log.d("path", path);
        files = new ArrayList<>();
        accessFolder(new File(path));
    }

    void accessFolder(File file){
        files.add(file);
        if(file.isDirectory()){
            for(File tmpFile : file.listFiles()){
                if(tmpFile != null){
                    accessFolder(tmpFile);
                }
            }
        }
    }
}
