package bcit.ca.easyexplorer;

import java.util.Arrays;

public class FileName {
    /*private String oldName;
    private String newName;
    private String[] otherNmaes;
    private int filesNum;
     */

    public static boolean ifNeedToChange(String oldNmae, String newName){
        if(oldNmae.equalsIgnoreCase(newName)){
            return false;
        }
        return true;
    }

    public static String correctRepeatName(String newName, String[] otherNmaes){
        int num = 0;
        String orgName = newName;
        Arrays.sort(otherNmaes);
        for (String tmp : otherNmaes) {
            if(tmp.equalsIgnoreCase(newName)){
                num++;
                newName = orgName + "_" + num;
            }
        }
        if(num>0){
            newName = orgName + "_" + num;
        }
        return newName;
    }

    public static String[] newMultipleNames(String newName, int filesNum, String[] otherNmaes){
        String[] newNames = new String[filesNum];
        int num = 0;
        String orgName = newName;
        Arrays.sort(otherNmaes);
        for (String tmp : otherNmaes) {
            if(tmp.equalsIgnoreCase(newName)){
                num++;
                newName = orgName + "_" + num;
            }
        }
        if(num == 0){
            newNames[num] = orgName;
        }else{
            newNames[0] = orgName + "_" + num;
        }
        for(int i = 1; i < filesNum; i++){
            num++;
            newNames[i] = orgName + "_" + num;
        }
        return newNames;
    }


}
