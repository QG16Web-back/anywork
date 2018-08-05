package com.qg.anywork.web;

import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.MailSendException;
import com.qg.anywork.exception.user.UserNotExitException;
import com.qg.anywork.service.MailService;
import com.qg.anywork.service.UserService;
import com.qg.anywork.utils.Encryption;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * 工具类访问接口
 *
 * @author FunriLy
 * @date 2017/8/18
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/utils")
@Slf4j
public class UtilController {
    private static final Logger logger = LoggerFactory.getLogger(UtilController.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    /**
     * 忘了密码邮箱找回
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/forget", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<?> sendMail(@RequestBody Map<String, String> map) {
        try {
            return mailService.sendPasswordMail(map.get("email"));
        } catch (UserNotExitException e) {
            logger.warn("不存在的用户！", e);
            return new RequestResult<>(StatEnum.LOGIN_NOT_EXIT_USER);
        } catch (MailSendException e) {
            logger.warn("发送邮件失败！", e);
            return new RequestResult<>(StatEnum.MAIL_SEND_FAIL);
        } catch (Exception e) {
            logger.warn("未知异常: ", e);
            return new RequestResult<>(StatEnum.DEFAULT_WRONG);
        }
    }

    // TODO 各种重定向页面

    /**
     * 验证邮箱秘钥，注册用户
     *
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String checkRegister(HttpServletRequest request) {
        String email = request.getParameter("email");
        String ciphertext = request.getParameter("ciphertext");
//        String email = map.get("email");
//        String ciphertext = map.get("ciphertext");
        if (email == null || "".equals(email) || ciphertext == null || "".equals(ciphertext)) {
            return "redirect:../html/failure.html";  // 跳转到错误页面 参数为空
        }
        if (ciphertext.equals(Encryption.getMD5(email))) {
            // 验证正确
            try {
                userService.register(email);
            } catch (Exception e) {
                return "redirect:../html/failure.html";  // 跳转到错误页面
            }
            return "redirect:../html/success.html";  // 跳转到登录页面
        } else {
            return "redirect:../html/failure.html";  // 跳转到错误页面 验证失败
        }
    }

    /**
     * 验证邮箱秘钥,修改密码
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<String> resetPassword(@RequestBody Map<String, String> map) {
        try {
            String email = map.get("email");
            String ciphertext = map.get("ciphertext");
            if (email == null || "".equals(email) || ciphertext == null || "".equals(ciphertext)) {
                return new RequestResult<>(StatEnum.ERROR_PARAM);
            }
            if (ciphertext.equals(Encryption.getMD5(email))) {
                String password = mailService.resetPassword(email);
                return new RequestResult<>(StatEnum.PASSWORD_RESET, password);
            }
            return new RequestResult<>(StatEnum.ERROR_PARAM);
        } catch (UserNotExitException e) {
            logger.warn("不存在的用户！", e);
            return new RequestResult<>(StatEnum.LOGIN_NOT_EXIT_USER);
        } catch (Exception e) {
            logger.warn("未知异常: ", e);
            return new RequestResult<>(StatEnum.DEFAULT_WRONG);
        }
    }

    /**
     * 生成图片验证码
     *
     * @param request  request
     * @param response response
     * @return 验证码
     */
    @RequestMapping(value = "/valcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String verification(HttpServletRequest request, HttpServletResponse response) {

        log.info("验证码");
        // 告知浏览当作图片处理
        response.setContentType("image/jpeg");
        // 告诉浏览器不缓存
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
        // 产生由4位数字构成的验证码
        int length = 4;
        StringBuilder valcode = new StringBuilder();
        Random rd = new Random();
        for (int i = 0; i < length; i++) {
            valcode.append(rd.nextInt(10));
        }
        // 把产生的验证码存入到Session中
        HttpSession session = request.getSession();
        session.setAttribute("valcode", valcode.toString());

        // 产生图片
        int width = 120;
        int height = 30;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取一个Graphics
        Graphics g = img.getGraphics();
        // 填充背景色
        g.setColor(Color.WHITE);
        //填充指定的矩形
        g.fillRect(0, 0, width, height);
        // 填充干扰线50
        for (int i = 0; i < 50; i++) {
            g.setColor(new Color(rd.nextInt(100) + 155, rd.nextInt(100) + 155, rd.nextInt(100) + 155));
            g.drawLine(rd.nextInt(width), rd.nextInt(height), rd.nextInt(width), rd.nextInt(height));
        }
        // 绘制边框
        g.setColor(Color.GRAY);
        //边框
        g.drawRect(0, 0, width - 1, height - 1);
        // 绘制验证码
        for (int i = 0; i < length; i++) {
            g.setColor(new Color(rd.nextInt(150), rd.nextInt(150), rd.nextInt(150)));
            g.drawString(valcode.charAt(i) + "", width / valcode.length() * i + 2, 18);
        }
        // 输出图像
        g.dispose();
        try {
            ImageIO.write(img, "jpeg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "html/failure.html";
        }
        log.info("验证码");
        return "html/success.html";
    }
}
