package ajax.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class DetailService {
	private File tempdir = null;
	private File defaultPhotoFile = null;
	private byte[] defaultPhoto;
	private DetailDAO detailDao;
	public DetailService(File tempdir, File defaultPhotoFile) {
		this.detailDao = new DetailDAO();
		this.tempdir = tempdir;
		this.defaultPhotoFile = defaultPhotoFile;
		this.defaultPhoto = this.readDefaultPhoto(defaultPhotoFile);
	}
	public String getPhotoPath(String photoid) {
		StringBuilder result = new StringBuilder()
				.append("/")
				.append(this.tempdir.getName())
				.append("/");
		if(isPhotoInTempDir(photoid)) {
			File tempFile = tempFiles.get(photoid);
			result.append(tempFile.getName());
		} else {
			byte[] photo = readPhotoFromDetailTable(photoid);
			if(photo!=null) {
				File tempFile = tempFiles.get(photoid);
				result.append(tempFile.getName());
			} else {
				result.append(defaultPhotoFile.getName());
			}
		}
		return result.toString();
	}
	public byte[] getPhotoByteArray(String photoid) {
		byte[] result = null;
		if(isPhotoInTempDir(photoid)) {
			result = readPhotoFromTempDir(photoid);
		} else {
			System.out.println("there is no file with this photoid in "+tempdir+" directory");
			result = readPhotoFromDetailTable(photoid);
			if(result==null) {
				result = defaultPhoto;
			}
		}
		return result;
	}
	private boolean isPhotoInTempDir(String photoid) {
		if(photoid!=null && photoid.length()!=0) {
			File tempFile = tempFiles.get(photoid);
			if(tempFile != null && tempFile.exists()) {
				return true;
			}
		}
		return false;
	}
	
	private byte[] readPhotoFromDetailTable(String photoid) {
		byte[] result = detailDao.select(photoid);
		if(result!=null && result.length!=0) {
			File outputFile = new File(tempdir, photoid + ".png");
			tempFiles.put(photoid, outputFile);
			
			//Write photo to tempdir
			try(FileOutputStream fos = new FileOutputStream(outputFile);) {
				fos.write(result);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private Map<String, File> tempFiles = new Hashtable<String, File>();
	private byte[] readPhotoFromTempDir(String photoid) {
		byte[] result = null;
		if(isPhotoInTempDir(photoid)) {
			File tempFile = tempFiles.get(photoid);
			try (FileInputStream fis = new FileInputStream(tempFile);) {
				result = new byte[(int) tempFile.length()];
				fis.read(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	private byte[] readDefaultPhoto(File defaultPhotoFile) {
		int length = (int) defaultPhotoFile.length();
		byte[] result = new byte[length];
		try (FileInputStream fis = new FileInputStream(defaultPhotoFile);) {
			fis.read(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
