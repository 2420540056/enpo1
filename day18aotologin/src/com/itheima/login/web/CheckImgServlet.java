package com.itheima.login.web;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 生成验证码图片
 * 
 * 
 */
@WebServlet("/checkImg")
public class CheckImgServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int width = 120;
		int height = 40;

		// 步骤一 绘制一张内存中图片（画布）
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// 步骤二 图片绘制背景颜色 ---通过绘图对象
		Graphics graphics = bufferedImage.getGraphics();// 得到画图对象 --- 画笔
		// 绘制任何图形之前 都必须指定一个颜色
		graphics.setColor(getRandColor(220, 250));
		graphics.fillRect(0, 0, width, height);

		// 步骤三 绘制边框
		graphics.setColor(Color.WHITE);
		graphics.drawRect(0, 0, width - 1, height - 1);

		// 步骤四 四个随机数字
		Graphics2D graphics2d = (Graphics2D) graphics;
		// 设置抗锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// 设置输出字体
		graphics2d.setFont(new Font("宋体", Font.BOLD, 22));

		String words =
		 "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		Random random = new Random();// 生成随机数
		
		// 强随机生成的验证码保存到session:
		StringBuffer buffer = new StringBuffer();
		
		// 定义x坐标
		int x = 10;
		for (int i = 0; i < 4; i++) {
			// 随机颜色
			graphics2d.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			// 旋转 -30 --- 30度
			int jiaodu = random.nextInt(60) - 30;
			// 换算弧度
			double theta = jiaodu * Math.PI / 180;

			// 生成一个随机数字
			int index = random.nextInt(words.length()); // 生成随机数 0 到 length - 1
			// 获得字母数字
			char c = words.charAt(index);

			// 将生成汉字 加入buffer
			buffer.append(c);

			// 将c 输出到图片
			graphics2d.rotate(theta, x, 20);
			graphics2d.drawString(String.valueOf(c), x, 25);
			graphics2d.rotate(-theta, x, 20);
			x += 30;
		}
		
		request.getSession().setAttribute("checkCode_session", buffer.toString());

		// 步骤五 绘制干扰线
		graphics.setColor(getRandColor(180, 220));
		int x1;
		int x2;
		int y1;
		int y2;
		for (int i = 0; i < 15; i++) {
			x1 = random.nextInt(width);
			x2 = random.nextInt(12);
			y1 = random.nextInt(height);
			y2 = random.nextInt(12);
			graphics.drawLine(x1, y1, x1 + x2, x2 + y2);
		}

		// 将上面图片输出到浏览器 ImageIO
		graphics.dispose();// 释放资源
		ImageIO.write(bufferedImage, "jpg", response.getOutputStream());

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 取其某一范围的color
	 * 
	 * @param fc
	 *            int 范围参数1
	 * @param bc
	 *            int 范围参数2
	 * @return Color
	 */
	private Color getRandColor(int fc, int bc) {
		// 取其随机颜色
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
