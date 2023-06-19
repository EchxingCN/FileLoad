package cn.itcast.fileupload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.util.List;
import java.util.UUID;

//�ϴ��ļ���servlet��
@WebServlet(name = "UploadServlet", value = "/UploadServlet")
//��ע�����ڱ�ע�ļ��ϴ���servlet
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1l;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //����ContentType�ֶ�ֵ
            response.setContentType("text/html;charset=utf-8");
            //����DiskFileItemFactory��������
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //�����ļ�����Ŀ¼��������ļ������ڣ����ٴ���һ��
            File f = new File("D:\\IdeaProjects\\FileUpload\\TempFolder");
//            File f = new File("D:\\TempFolder");
            if(!f.exists()){
                f.mkdirs();
            }
            //�����ļ�����·��
            factory.setRepository(f);
            //����ServletFileUpload����
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            //�����ַ�����
            fileUpload.setHeaderEncoding("utf-8");
            //����request,�õ��ϴ��ļ���FileItem����
            List<FileItem> fileItems = fileUpload.parseRequest(request);
            //��ȡ�ַ���
            PrintWriter writer = response.getWriter();
            //��������
            for (FileItem fileitem :
                    fileItems) {
                //�ж��Ƿ�Ϊ��ͨ�ֶ�
                if(fileitem.isFormField()){
                    //��ȡ�ֶ������ֶ�ֵ
                    String name = fileitem.getFieldName();
                    if(name.equals("name")){
                        //����ļ���Ϊ�գ����浽value��
                        if(!fileitem.getString().equals("")){
                            String value = fileitem.getString("utf-8");
                            writer.print("�ϴ��ߣ�" +value+"<br />");
                        }
                    }
                }else {
                    //��ȡ�ϴ����ļ���
                    String filename = fileitem.getName();
                    //�����ϴ��ļ�
                    if(fileitem != null && !fileitem.equals("")){
                        writer.print("�ϴ����ļ������ǣ�" +filename+"<br />");
                        //��ȡ�ļ���
                        filename = filename.substring(filename.lastIndexOf("\\") + 1);
                        //�ļ�������Ψһ
                        filename = UUID.randomUUID().toString() + "_" + filename;
                        //�ڷ���������ͬ���ļ�
                        String webPath = "/upload/";
                        //�����������ļ���·�����ļ�����ϳ������ķ����·��
                        String filepath = getServletContext().getRealPath(webPath + filename);
                        //�����ļ�
                        File file = new File(filepath);
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                        //����ϴ��ļ���
                        InputStream in = fileitem.getInputStream();
                        //ʹ��FileOutputStream�򿪷���˵��ϴ��ļ�
                        FileOutputStream out = new FileOutputStream(file);
                        //���ĸ���
                        byte[] buffer = new byte[1024];//ÿ�ζ�ȡ1�ֽ�
                        int len;
                        //��ʼ��ȡ�ϴ��ļ����ֽڣ����������뵽����˵��ϴ��ļ����������
                        while ((len=in.read(buffer))>0){
                            out.write(buffer,0,len);
                            //�ر���
                            in.close();;
                            out.close();
                            //ɾ����ʱ�ļ�
                            fileitem.delete();
                            writer.print("�ϴ��ļ��ɹ���<br />");
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
