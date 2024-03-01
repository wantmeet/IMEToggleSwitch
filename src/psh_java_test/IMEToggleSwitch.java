package psh_java_test;

import java.util.ArrayList;
import java.util.Iterator;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.UnsupportedEncodingException;

public class IMEToggleSwitch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f =new File ("C:\\temp\\export_reg.txt");
		
		if (f.exists() ) {
			f.delete();
		}
		
		try {
			String expCmdLine = "HKEY_CURRENT_USER\\Software\\Microsoft\\IME\\15.0\\IMEKR C:\\temp\\export_reg.txt";
			
			Runtime r = Runtime.getRuntime();
			
			Process p1 = r.exec("reg export " + expCmdLine);
			
			p1.getErrorStream().close();
			p1.getInputStream().close();
			p1.getOutputStream().close();
			p1.waitFor();
		} catch (InterruptedException iioe) {
			System.out.println("InterruptedException iioe: " + iioe.getMessage() );
			
		} catch (IOException ioe ) {
			System.out.println("IOException ioe: " + ioe.getMessage());
		}
		
		System.out.println("Registry를 export 받았습니다.");
		
		
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-16LE") );
			
			
		} catch (FileNotFoundException fne) {
			System.out.println("File Not Found at C:/temp/export_reg.txt");
			System.exit(-1);
		} catch (UnsupportedEncodingException uee) {
			System.out.println("UnsupportedEncodingException uee: " + uee.getMessage() );
		}
		
		String readLine = null;
		ArrayList<String> arrList = new ArrayList<String> ();
		
		System.out.println("export 파일을 읽기 시작합니다.");
		
		boolean threeBul = true;
		
		try {
			
			while ((readLine = br.readLine()) != null) {
				if (readLine.startsWith("\"InputMethod") ) {
					String curInputMethod = readLine.substring(readLine.lastIndexOf(":") + 1 );
					
					if (curInputMethod.equals("00000001") ) {
						readLine = readLine.replace("00000001", "00000000");
						threeBul = false;
					} else if (curInputMethod.equals("00000000") ) {
						readLine = readLine.replace("00000000", "00000001");
						threeBul = true;
					}
				} else if (readLine.startsWith("\"Timestamp") ) {
					String curStampDword = readLine.substring(readLine.lastIndexOf(":") +1 );
					
					long lStampDword = Long.parseLong(curStampDword, 16);
					
					lStampDword++;
					
					String rStampDword = Long.toHexString(lStampDword);
					
					readLine = readLine.replace(curStampDword, rStampDword);
					
					System.out.println("TimeStamp 값을 변경했습니다.");
					
				}
				arrList.add(readLine);
			} 
		} catch(IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage() );
		}
		
		File f2 = new File("C:\\temp\\import_reg.txt");
		
		try {
			if (!f2.exists() ) {
				f2.createNewFile();
			} else {
				f2.delete();
				f2.createNewFile();
			}
		} catch (IOException ioe) {
			System.out.println("IOException ioe: " + ioe.getMessage() );
		}
		
		BufferedWriter bw = null;
		
		try {
			
			bw = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(f2), "UTF-16LE") );
			
			Iterator<String> itr = arrList.iterator();
			
			while (itr.hasNext()) {
				bw.write(itr.next() );
				bw.write("\n");
				bw.flush();
			}
			
			bw.close();
		} catch (FileNotFoundException fne) {
			System.out.println("File Not Foud at C:/temp/import_reg.txt");
			System.exit(-1);
		} catch (UnsupportedEncodingException uee) {
			System.out.println("UnsupportedEncodingException uee: " + uee.getMessage() );
		} catch (IOException ioe) {
			System.out.println("IOException ioe: " + ioe.getMessage() );
			
		}
		
		System.out.println("export 추출파일을 변경하여 import 파일로 저장했습니다.");
		
		try {
			String expCmdLine = "C:\\temp\\import_reg.txt";
			
			Runtime r = Runtime.getRuntime();
			
			Process p1 = r.exec("reg import " + expCmdLine);
			
			p1.getErrorStream().close();
			p1.getInputStream().close();
			p1.getOutputStream().close();
			p1.waitFor();
			
			
			
		} catch (InterruptedException iioe) {
			System.out.println("InterruptedException iioe: " + iioe.getMessage() );
			
		} catch (IOException ioe) {
			System.out.println("IOException ioe: " + ioe.getMessage() );
		}
		
		
		System.out.println("Registry에 import 했습니다.");
		
		
		if (threeBul) {
			System.out.println("■■■ 두벌식 → 세벌식390 전환완료");
		} else {
			System.out.println("■■■ 세벌식390 → 두벌식 전환완료");
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		System.exit(0);
	}

}
