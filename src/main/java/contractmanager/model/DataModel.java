package contractmanager.model;

import cz.zcu.kiv.contractparser.ContractManagerApi;
import cz.zcu.kiv.contractparser.io.IOServices;
import cz.zcu.kiv.contractparser.model.ContractType;
import cz.zcu.kiv.contractparser.model.JavaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Václav Mareš
 */
public class DataModel {

    private List<JavaFile> files;
    private JavaFile currentFile;
    private HashMap<ContractType,Boolean> contractTypes;


    public DataModel() {
        this.files = new ArrayList<>();
        this.contractTypes = new HashMap<>();

        // add all available contract types
        for(ContractType contractType : ContractType.values()){
            contractTypes.put(contractType, true);
        }
    }

    public boolean addFile(File newFile) {

        // check whether this file is not already present
        boolean found = false;
        for(JavaFile javaFile : files) {
            if(javaFile.getPath().equals(newFile.getPath())) {
                found = true;
                break;
            }
        }

        // if the file is not in the list yet - add it
        if (!found) {
            JavaFile javaFile = ContractManagerApi.retrieveContracts(newFile, contractTypes);
            files.add(javaFile);
            return true;
        }
        else{
            return false;
        }
    }

    public int removeFiles(List<Integer> fileIds) {

        int deletedFiles = 0;

        for(int index : fileIds){

            int newIndex = index - deletedFiles;

            if(newIndex < files.size()) {
                files.remove(newIndex);
                deletedFiles++;
            }
        }

        return deletedFiles;
    }

    public int exportToJSON(List<Integer> fileIds, File outputFolder) {

        int exportedFiles = 0;

        for(int index : fileIds){

            if(index < files.size()) {
                try {
                    IOServices.exportToJson(files.get(index), outputFolder);
                    exportedFiles++;
                }
                catch(Exception e){
                    System.err.println("LOG ERROR");
                    // TODO handle error
                }

            }
        }

        return exportedFiles;
    }



    public List<JavaFile> getFiles() {
        return files;
    }

    public JavaFile getCurrentFile() {
        return currentFile;
    }

    public HashMap<ContractType, Boolean> getContractTypes() {
        return contractTypes;
    }

    public void setCurrentFile(JavaFile currentFile) {
        this.currentFile = currentFile;
    }
}