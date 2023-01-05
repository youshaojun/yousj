package top.yousj.reload.service;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.VirtualMachine;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * 热部署, 参考https://gitee.com/huoyo/ko-time
 *
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
public class JvmAttachClassReloadService {

	private File agentJar;

	private JvmAttachClassReloadService() {
		this.agentJar = createAgentJar();
	}

	private static class InstanceHolder {
		private static final JvmAttachClassReloadService INSTANCE = new JvmAttachClassReloadService();
	}

	public static JvmAttachClassReloadService getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public void updateClass(String className, String classPath) {
		try {
			if (agentJar == null || !agentJar.exists()) {
				agentJar = createAgentJar();
			}
			VirtualMachine virtualMachine = VirtualMachine.ForHotSpot.attach(getPid());
			virtualMachine.loadAgent(agentJar.getAbsolutePath(), className + "-" + classPath);
			Thread.sleep(500);
			virtualMachine.detach();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Fail to update class:" + className);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public File createAgentJar() {
		File jarFile = null;
		try {
			jarFile = File.createTempFile("classTrans-", ".jar", new File(System.getProperty("java.io.tmpdir")));
			JarOutputStream out = new JarOutputStream(new FileOutputStream(jarFile));
			String[] jarNames = new String[]{
				"cn/langpy/kotime/RedefineClass.class",
				"cn/langpy/kotime/ClassTransformer.class",
				"META-INF/maven/cn.langpy/ko-time-retrans/pom.xml",
				"META-INF/maven/cn.langpy/ko-time-retrans/pom.properties",
				"MANIFEST.MF",
				"META-INF/MANIFEST.MF",
			};
			String[] jarFilePaths = new String[]{
				"retrans/RedefineClass.class",
				"retrans/ClassTransformer.class",
				"retrans/META-INF/maven/cn.langpy/ko-time-retrans/pom.xml",
				"retrans/META-INF/maven/cn.langpy/ko-time-retrans/pom.properties",
				"retrans/MANIFEST.MF",
				"retrans/META-INF/MANIFEST.MF"
			};
			buildElement(out, jarNames, jarFilePaths);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		jarFile.deleteOnExit();
		return jarFile;
	}

	private void buildElement(JarOutputStream out, String[] fileNames, String[] filePaths) throws IOException {
		for (int i = 0; i < fileNames.length; i++) {
			ClassPathResource classPathResource = new ClassPathResource(filePaths[i]);
			addJarFile(out, fileNames[i], classPathResource.getInputStream());
		}
	}


	private void addJarFile(JarOutputStream out, String packagePath, InputStream in) {
		try {
			out.putNextEntry(new JarEntry(packagePath));
			byte[] buffer = new byte[1024];
			int n = in.read(buffer);
			while (n != -1) {
				out.write(buffer, 0, n);
				n = in.read(buffer);
			}
			in.close();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getPid() {
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		return jvmName.split("@")[0];
	}

}
