package cn.itcast.fileupload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(name = "DownloadServlet", value = "/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1l;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置ContentType字段值
        response.setContentType("text/html;charset=utf-8");
        //获取所要下载的文件名称
        String filename = request.getParameter("filename");
        System.out.println(filename);
        //下载文件所在的目录
        String folder = "/download/";
        //通知浏览器以下载的方式打开
        response.addHeader("Content-type","application/octet-stream");
        response.addHeader("Content-Disposition",
                "attachment;filename="+filename);
        //通过文件流读取文件
        InputStream in = getServletContext().getResourceAsStream(folder + filename);
        //获取response对象的输出流
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        //循环取出流中的数据
        while ((len=in.read(buffer))!=-1){
            out.write(buffer,0,len);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
