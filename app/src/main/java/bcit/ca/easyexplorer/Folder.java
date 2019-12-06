package bcit.ca.easyexplorer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Folder {
    private String currentPath;
    private String currentName;
    private File[] files;
    private String[] fileNames;
    private String[] paths;

    public Folder (String path){
        currentPath = path;
        updateData();
    }

    public void setCurrentPath(String path){
        currentPath = path;
        updateData();
    }
    public String getCurrentName(){
        return currentName;
    }

    public String getCurrentPath(){
        return currentPath;
    }

    public File[] getFiles(){
        return files;
    }

    public String[] getFileNames(){
        return fileNames;
    }

    public ArrayList<String> getParentPaths(){
        ArrayList<String> tmpPaths = new ArrayList<String>();
        File file = new File(currentPath);
        file = file.getParentFile();
        while ( file!= null){
            tmpPaths.add(file.getPath());
            file = file.getParentFile();
        }
        paths = (String[])tmpPaths.toArray();
        return tmpPaths;
    }

    void sortFileByName(){
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File object1, File object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });
    }

    void updateData(){
        File file = new File(currentPath);
        currentName = file.getName();
        files = file.listFiles();
        sortFileByName();
        //Log.d("file length", "file length " + files.length);
        fileNames = new String[files.length + 1];
        fileNames[0] = "/..";
        for(int i = 0; i < files.length; i++){
            fileNames[i + 1] = files[i].getName();
        }
    }
}