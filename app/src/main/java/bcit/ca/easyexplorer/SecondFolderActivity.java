package bcit.ca.easyexplorer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SecondFolderActivity extends AppCompatActivity {
    private String currentPath;
    private File[] files;
    private String[] fileNames;
    private String selectPath;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Button> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_folder);
        currentPath = Environment.getExternalStorageDirectory().getPath();
        Log.d("ExternalStorageDirectory", currentPath);
        paths.add(currentPath);
        updateFolder();
        Intent intent = getIntent();
        int size = intent.getIntExtra("buttons", 0);
        if(size == 0){
            addFolderBtn();
        }else{
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

    void updateFolder(){
        Log.d("file", currentPath + " has " +paths.size());
        getFiles(currentPath);
        Toolbar pathView = findViewById(R.id.currentPath);
        updatePathButton(pathView);
        ListView listView = findViewById(R.id.fileListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
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
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            SecondFolderActivity.this);
                    alert.setTitle("Rename");

                    final EditText input = new EditText(SecondFolderActivity.this);
                    alert.setView(input);
                    final String oldName = fileNames[position];
                    input.setText(oldName);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String srt1 = input.getEditableText().toString();
                            //Toast toast = Toast.makeText(SecondFolderActivity.this, "Try to save the file in " + currentPath + " from name " + oldName + " to " + srt1, Toast.LENGTH_SHORT);
                            //toast.show();
                            changeFileName(oldName, srt1);
                            //update your listview here
                            updateFolder();
                        }
                    });

                    alert.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
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
        if(folderBar.getChildCount() <= 3){
            Button curB = new Button(this);
            curB.setId(folders.size());
            curB.setText("Folder" + (folders.size()+1));
            curB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = v.getId();
                    Intent intent;
                    switch (i){
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

    void changeFileName(String oldName, String newName){
        if(FileName.ifNeedToChange(oldName, newName)){
            newName = FileName.correctRepeatName(newName, fileNames);
            File from = new File(currentPath, oldName);
            if(from.exists()){
                from.renameTo(new File(currentPath, newName));
                Toast toast = Toast.makeText(SecondFolderActivity.this, "Save the file from name " + oldName + " to " + newName + " Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(SecondFolderActivity.this, "Can't save the file from name " + oldName + " to " + newName, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
