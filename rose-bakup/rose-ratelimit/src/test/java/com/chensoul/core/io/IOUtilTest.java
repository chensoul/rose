package com.chensoul.core.io;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class IOUtilTest {

	@Test
	void copy() throws IOException {
		URL url = new URL("https://wmsps.obs.cn-north-4.myhuaweicloud.com/test/client/capturepicture/capturepicture_4bb6b451-59f3-4378-adbe-784bef71c4c2.jpeg");
		File file = new File("test.jpeg");

		IOUtils.copy(url.openStream(), new FileWriter(file));
		file.delete();
	}
}
