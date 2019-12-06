package bcit.ca.easyexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String currentPath;
    private File[] files;
    private String[] fileNames;
    private String selectPath;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Button> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        Intent intent = getIntent();
        String path = intent.getStringExtra("currentPath");
        if(path!=null){
            currentPath = path;
        }else{
            currentPath = Environment.getRootDirectory().getPath();
        }
        selectPath = currentPath;
        paths.add(currentPath);
        updateFolder();
        int size = intent.getIntExtra("buttons", 0);
        if(size == 0){
            addFolderBtn();
        }else{
            if(size != folders.size()){
                size = folders.size();
            }
            for(int i = 0; i < size; i++){
                addFolderBtn();
            }
            if(folders.size() > 2){
                findViewById(R.id.plus).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFolder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("currentPath", currentPath);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void updateFolder(){
        Log.d("file", currentPath + " has " +paths.size());
        getFiles(currentPath);
        Toolbar pathView = findViewById(R.id.currentPath);
        updatePathButton(pathView);
        ListView listView = findViewById(R.id.fileListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, fileNames
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((new File(currentPath+"/"+fileNames[position])).isDirectory()
                        && !fileNames[position].equalsIgnoreCase("lost+found")){
                    if(position == 0){
                        if(paths.size() > 1){
                            paths.remove(paths.size() - 1);
                            updatePath(paths.size());
                        }else{
                            Log.d("file", "Can't go back from " + fileNames[position]);
                        }
                    }else{
                        currentPath += "/" + fileNames[position];
                        paths.add(currentPath);
                    }
                }else{
                    Log.d("file", "Can't read file " + fileNames[position]);
                }
                updateFolder();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    selectPath = currentPath;
                }
                return false;
            }
        });
    }

    void updatePathButton(Toolbar bar){
        bar.removeAllViews();
        for (int i = 0; i < paths.size(); i++){
            Button curB = new Button(this);
            curB.setId(i+1);
            curB.setHeight(20);
            curB.setText((new File(paths.get(i))).getName());
            curB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePath(v.getId());
                    int i = paths.size() - v.getId();
                    while (i > 0){
                        paths.remove(paths.size() - 1);
                        i--;
                    }
                    updateFolder();
                }
            });
            bar.addView(curB);
        }
        TextView number = new TextView(this);
        number.setText(" (" + files.length + ")");
        bar.addView(number);
    }

    void getFiles(String currentPath){
        files = new File(currentPath).listFiles();
        //Log.d("file length", "file length " + files.length);
        fileNames = new String[files.length + 1];
        fileNames[0] = "/..";
        for(int i = 0; i < files.length; i++){
            fileNames[i + 1] = files[i].getName();
        }
    }

    void updatePath(int num){
        Log.d("file", "file " + (num-1));// + " is " + paths.get(num-1));
        currentPath = "";
        currentPath = paths.get(num-1);
    }

    void addFolderBtn(){
        Toolbar folderBar = findViewById(R.id.folderExchange);
        if(folderBar.getChildCount() <= 3) {
            Button curB = new Button(this);
            curB.setId(folders.size());
            curB.setText("Folder" + (folders.size() + 1));
            curB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = v.getId();
                    Intent intent;
                    switch (i) {
                        case 1:
                            intent = new Intent(v.getContext(), SecondFolderActivity.class);
                            break;
                        case 2:
                            intent = new Intent(v.getContext(), ThirdFolderActivity.class);
                            break;
                        default:
                            intent = new Intent(v.getContext(), MainActivity.class);
                    }
                    intent.putExtra("buttons", folders.size());
                    intent.putExtra("selectPath", selectPath);
                    startActivity(intent);
                }
            });
            folders.add(curB);
            folderBar.addView(curB);
        }
    }

    public void addButton(View view){
        addFolderBtn();
        if(folders.size() > 2){
            findViewById(R.id.plus).setVisibility(View.GONE);
        }
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // temporary fix to prevent crash if current Android version lower than required SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // temporary fix to prevent crash if current Android version lower than required SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
