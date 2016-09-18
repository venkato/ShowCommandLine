package nik.git.forcepush;

import java.util.logging.Logger;

import org.eclipse.jgit.transport.URIish;

import sun.reflect.Reflection;

public class MyPushHook2 extends MapSupport {
	private static final Logger log = Logger.getLogger(Reflection.getCallerClass(1).getName());

	public static String enabledUtlPush = "";

	@Override
	public Object get(Object key) {
		if (key instanceof org.eclipse.jgit.transport.Transport) {
			org.eclipse.jgit.transport.Transport transport = (org.eclipse.jgit.transport.Transport) key;
			log.info(transport.getURI() + "");
			URIish uris = transport.getURI();
			if (!uris.toString().startsWith(enabledUtlPush)) {
				throw new IllegalStateException("push not allowed for : " + uris);
			}
		}
		return null;
	}
}
