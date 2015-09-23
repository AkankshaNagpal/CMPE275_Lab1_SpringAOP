package edu.sjsu.cmpe275.lab1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FileServiceImpl implements IFileService {

	HashMap<String, Set<Textfile>> fileMap;
	List<Textfile> files; // List is created which saves all files

	public HashMap<String, Set<Textfile>> getFileMap() {
		return fileMap;
	}

	public void setFileMap(HashMap<String, Set<Textfile>> fileMap) {
		this.fileMap = fileMap;
	}

	public List<Textfile> getFiles() {
		return files;
	}

	public void setFiles(List<Textfile> files) {
		this.files = files;
	}

	public void shareFile(String userId, String targetUserID, String filePath) {

		for (Textfile file : files) {

			if (file.getFilepath() == filePath) {

				fileMap.get(targetUserID).add(file);
				break;
			}
		}

	}

	public void unshareFile(String userId, String targetUserID, String filePath) {

		for (Textfile file : fileMap.get(targetUserID)) {

			if (file.getFilepath() == filePath) {

				fileMap.get(targetUserID).remove(file);
				break;
			}
		}
	}

	public byte[] readFile(String userId, String filePath) {
		// TODO Auto-generated method stub

		for (Textfile file : files) {

			if (file.getFilepath() == filePath) {

				File f = new File(filePath);
				byte[] fileContent = null;

				try {

					FileInputStream fin = new FileInputStream(f);
					fileContent = new byte[(int) f.length()];
					fin.read(fileContent);

				}

				catch (FileNotFoundException e) {
					// System.out.println("File not found" + e);
				}

				catch (IOException ioe) {
					System.out.println("Exception while reading the file "
							+ ioe);
				}

				return fileContent;

			}

		}

		return null;
	}

}
