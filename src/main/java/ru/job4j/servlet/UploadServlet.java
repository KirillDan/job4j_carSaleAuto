package ru.job4j.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ru.job4j.jsonDto.UuidJson;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebServlet("/upload/")
public class UploadServlet extends HttpServlet {
	private Jsonb jsonb;

	public void init() {
		jsonb = JsonbBuilder.create();
	}
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        resp.setContentType("name=" + name);
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        File file = new File("images" + File.separator + name);
        System.out.println("file.getAbsolutePath()  =  " + file.getAbsolutePath());
        try (FileInputStream in = new FileInputStream(file)) {
            resp.getOutputStream().write(in.readAllBytes());
        }
    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		Set<String> uuids = new HashSet();
		System.out.println("(1)  ");
		try {
			System.out.println("(2)  ");
			List<FileItem> items = upload.parseRequest(req);
			File folder = new File("images");
			if (!folder.exists()) {
				folder.mkdir();
			}
			System.out.println("(3)  ");
			for (FileItem item : items) {
				System.out.println("(4)  ");
				if (!item.isFormField()) {
					System.out.println("(5)  ");
					UUID uuid = UUID.randomUUID();
					System.out.println("uuid  ==  " + uuid);
					uuids.add(String.valueOf(uuid));
					File file = new File(folder + File.separator + String.valueOf(uuid));
					try (FileOutputStream out = new FileOutputStream(file)) {
						out.write(item.getInputStream().readAllBytes());
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter w = resp.getWriter();
		String result = jsonb.toJson(new UuidJson(uuids));
		w.println(result);
		w.println();
	}
}