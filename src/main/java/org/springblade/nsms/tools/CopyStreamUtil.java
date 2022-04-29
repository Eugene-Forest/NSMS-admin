package org.springblade.nsms.tools;

import java.io.*;

/**
 * @author Eugene-Forest
 * @date 2022/4/24
 **/
public class CopyStreamUtil {

	/**
	 * 通过序列化对象进行深度拷贝
	 * @param origin
	 * @return
	 * @param <T>
	 * @throws Exception
	 */
	public static <T extends Serializable> T deepClone(T origin) throws Exception{
		// 序列化
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(origin);

		// 反序列化
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);

		//需要处理类型变换异常
		T object = (T) ois.readObject();
		return object;
	}
}
