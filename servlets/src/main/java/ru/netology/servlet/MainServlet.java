package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
   private static final String GET = "GET";
   private static final String POST = "POST";
   private static final String DELETE = "DELETE";
   private static final String postsPath = "/api/posts";
   private PostController controller;

   @Override
   public void init() {
      final var repository = new PostRepository();
      final var service = new PostService(repository);
      controller = new PostController(service);
   }

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) {

      try {
         final var path = req.getRequestURI();
         final var method = req.getMethod();
         final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));

         if (method.equals(GET) && path.equals(postsPath)) {
            controller.all(resp);
            return;
         }

         if (method.equals(GET) && path.matches(postsPath + "\\d+")) {
            controller.getById(id, resp);
            return;
         }

         if (method.equals(POST) && path.equals(postsPath)) {
            controller.save(req.getReader(), resp);
            return;
         }

         if (method.equals(DELETE) && path.matches(postsPath + "\\d+")) {
            controller.removeById(id, resp);
            return;
         }

         resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

      } catch (Exception e) {
         e.printStackTrace();
         resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
   }
}