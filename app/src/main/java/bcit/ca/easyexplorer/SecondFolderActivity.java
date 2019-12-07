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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SecondFolderActivity extends AppCompatActivity {
    private Folder folder;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Button> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_folder);
        folder = new Folder(Environment.getExternalStorageDirectory().getPath());
        paths.add(folder.getCurrentPath());
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
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                forFABButton();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        folder = new Folder(folder.getCurrentPath());
        updateFolder();
    }

    void updateFolder(){
        Toolbar pathView = findViewById(R.id.currentPath);
        updatePathButton(pathView);
        ListView listView = findViewById(R.id.fileListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, folder.getFileNames()
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((new File(folder.getCurrentPath()+"/"+folder.getFileNames()[position])).isDirectory()
                        && !folder.getFileNames()[position].equalsIgnoreCase("lost+found")){
                    if(position == 0){
                        if(paths.size() > 1){
                            paths.remove(paths.size() - 1);
                            updatePath(paths.size());
                        }else{
                            Log.d("file", "Can't go back from " + folder.getFileNames()[position]);
                        }
                    }else{
                        folder.setCurrentPath(folder.getCurrentPath() + "/" + folder.getFileNames()[position]);
                        paths.add(folder.getCurrentPath());
                    }
                }else{
                    Log.d("file", "Can't read file " + folder.getFileNames()[position]);
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
                    final String oldName = folder.getFileNames()[position];
                    input.setId(R.id.renameInputField);
                    input.setText(oldName);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String srt1 = input.getEditableText().toString();
                            if(srt1.length() == 0){
                                Toast toast = Toast.makeText(SecondFolderActivity.this, "Successfully delete the file " + oldName, Toast.LENGTH_SHORT);
                                toast.show();
                                deleteSelectFile(oldName);
                            }else{
                                changeFileName(oldName, srt1);
                            }
                            paths.remove(paths.size() - 1);
                            updatePath(paths.size());
                            //folder = new Folder(folder.getParentPath());
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
        number.setText(" (" + folder.getFiles().length + ")");
        bar.addView(number);
    }

    void updatePath(int num){
        folder.setCurrentPath(paths.get(num-1));
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
                    //intent.putExtra("selectPath", selectPath);
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
            newName = FileName.correctRepeatName(newName, folder.getFileNames());
            File from = new File(folder.getParentPath(), oldName);
            if(from.exists()){
                from.renameTo(new File(folder.getParentPath(), newName));
                Toast toast = Toast.makeText(SecondFolderActivity.this, "Save the file from name " + oldName + " to " + newName + " Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(SecondFolderActivity.this, "Can't save the file from name " + oldName + " to " + newName, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void deleteSelectFile(String folderName){
        File file = new File(folder.getParentPath() + File.separator + folderName);

        if (file.exists()) {
            String deleteCmd = "rm -r " + folder.getParentPath() + File.separator + folderName;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }

    void forFABButton(){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                SecondFolderActivity.this);
        alert.setTitle("Add Folder");

        final EditText input = new EditText(SecondFolderActivity.this);
        alert.setView(input);
        input.setId(R.id.createFolderField);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String srt1 = input.getEditableText().toString();
                //Toast toast = Toast.makeText(SecondFolderActivity.this, "Try to save the file in " + currentPath + " from name " + oldName + " to " + srt1, Toast.LENGTH_SHORT);
                //toast.show();
                createNewFolder(srt1);
                //update your listview here
                folder = new Folder(folder.getCurrentPath());
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

    void createNewFolder(String folderName){
        File newFolder = new File(folder.getCurrentPath() + File.separator+folderName);
        boolean success = true;
        Toast toast;
        if(!newFolder.exists()){
            success = newFolder.mkdirs();
            if(success){
                toast = Toast.makeText(SecondFolderActivity.this, "Create the file " + folderName + " Successfully", Toast.LENGTH_SHORT);
            }else{
                toast = Toast.makeText(SecondFolderActivity.this, "Create the file " + folderName + " Failed", Toast.LENGTH_SHORT);
            }
        }else{
            toast = Toast.makeText(SecondFolderActivity.this, "The file " + folderName + " already exist", Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
