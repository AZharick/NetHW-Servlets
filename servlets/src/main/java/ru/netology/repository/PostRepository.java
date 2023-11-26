package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Ex-Stub
public class PostRepository {
   private CopyOnWriteArrayList<Post> postsList = new CopyOnWriteArrayList<>();
   private AtomicInteger postsListCounter = new AtomicInteger(0);

   public List<Post> all() {
      return postsList;
   }

   public Optional<Post> getById(long id) {
      return Optional.ofNullable(postsList.get((int) id));
   }

   public Post save(Post post) throws NotFoundException {
      boolean isUpdated = false;
      if (post.getId() == 0) {
         postsList.add(post);
         postsListCounter.getAndIncrement();
      } else {
         for (int i = 0; i < postsList.size(); i++) {
            if (post.getId() == postsList.get(i).getId()) {
               postsList.get(i).setContent(post.getContent());
               isUpdated = true;
            }

            if ((i == postsList.size()) && !isUpdated) {
               throw new NotFoundException("No post with such ID found!");
            }
         }
      }
      return post;
   }

   public void removeById(long id) {
      for (Post post: postsList) {
         if (post.getId() == id) {
            postsList.remove(id);
            postsListCounter.getAndDecrement();
         }
      }
   }
}
