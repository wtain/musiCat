package ru.rz.musiCat.helpers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import ru.rz.musiCat.data.entities.Host;

public class HostHelpers {
	
	public static List<String> getMachineIds() {
		Enumeration<NetworkInterface> nis = null;
		List<String> rv = new ArrayList<String>();
		try {
			nis = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rv;
		}
		while (nis.hasMoreElements()) {
		    NetworkInterface ni = nis.nextElement();
		    
		    //rv.add(String.format("%s %s", ni.getName(), ni.getDisplayName()));
//		    try {
//				//rv.add(ni.getHardwareAddress().toString());
//		    	
//			} catch (SocketException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    try {
				if (ni.isLoopback() || ni.isVirtual())
					continue;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    rv.add(ni.getDisplayName());
		}
		return rv;
	}
	
	public static String getMachineId() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UNKNOWN";
		}
//		List<String> ids = getMachineIds();
//		if (ids.isEmpty())
//			return "UNKNOWN";
//		return ids.get(ids.size()-1);
	}
	
	public static String getMachineAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UNKNOWN";
		}
	}
	
	public static Host getCurrentHost() {
		return new Host(getMachineId(), 
				        getMachineAddress());
	}
}
