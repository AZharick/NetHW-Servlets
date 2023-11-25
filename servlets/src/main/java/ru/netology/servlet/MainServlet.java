package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//вынести методы в константы, убрать дублирующийся код.

public class MainServlet extends HttpServlet {

    //simple test method
//  @Override
//  protected void service(HttpServletRequest req, HttpServletResponse resp) {
//     resp.setStatus(HttpServletResponse.SC_OK);
//     resp.setContentType("text/plain");
//     try {
//       resp.getWriter().println("wasssups");
//     } catch (IOException e) {
//       throw new RuntimeException(e);
//     }
//  }

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

      if (method.equals("GET") && path.equals("/api/posts")) {
        controller.all(resp);
        return;
      }

      if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.getById(id, resp);
        return;
      }

      if (method.equals("POST") && path.equals("/api/posts")) {
        controller.save(req.getReader(), resp);
        return;
      }

      if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
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