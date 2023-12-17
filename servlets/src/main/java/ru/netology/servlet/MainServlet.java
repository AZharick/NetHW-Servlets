package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
   private static final String POSTS_PATH = "/api/posts";
   private static final String POSTS_PATH_W_ID = POSTS_PATH + "/\\d+";
   private PostController controller;

   @Override
   public void init() {
      final var context = new AnnotationConfigApplicationContext("ru.netology");
      final var repository = context.getBean(PostRepository.class);
      final var service = context.getBean(PostService.class);
      controller = context.getBean(PostController.class);
   }

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) {

      try {
         final var path = req.getRequestURI();
         final var method = req.getMethod();

         if (method.equals(GET) && path.equals(POSTS_PATH)) {
            controller.all(resp);
            return;
         }

         if (method.equals(GET) && path.matches(POSTS_PATH_W_ID)) {
            var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.getById(id, resp);
            return;
         }

         if (method.equals(POST) && path.equals(POSTS_PATH)) {
            controller.save(req.getReader(), resp);
            return;
         }

         if (method.equals(DELETE) && path.matches(POSTS_PATH_W_ID)) {
            var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
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