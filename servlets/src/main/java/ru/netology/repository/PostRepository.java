package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Ex-Stub
public class PostRepository {
   protected ConcurrentHashMap<AtomicLong, String> posts = new ConcurrentHashMap<>();
   protected AtomicLong postsListCounter = new AtomicLong(0);

   public List<Post> all() {
      ArrayList<Post> allPosts = new ArrayList<>();
      for (int i = 1; i < posts.size(); i++) {
         Post post = new Post(i, posts.get(i));
         allPosts.add(post);
      }
      allPosts.add(new Post(666, "sixsixsix"));    //test
      return allPosts;
   }

   public Optional<Post> getById(long id) {
      Post post = null;
      if (posts.containsKey(id)) {
         post = new Post(id, posts.get(id));
      }
      return Optional.ofNullable(post);
   }

   public Post save(Post post) {
      Post updatedPost;
      if (post.getId() == 0) {
         postsListCounter.getAndIncrement();
         posts.put(postsListCounter, post.getContent());
         updatedPost = new Post(postsListCounter.longValue(), post.getContent());
      } else {
         AtomicLong id = new AtomicLong(post.getId());
         posts.put(id, post.getContent());
         updatedPost = new Post(id.longValue(), post.getContent());
      }
      return updatedPost;
   }

   public void removeById(long id) throws NoSuchElementException {
      if (this.getById(id).equals(Optional.empty())) {
         throw new NoSuchElementException("No such ID!");
      }
      AtomicLong atomicId = new AtomicLong(id);
      posts.remove(atomicId);
      postsListCounter.getAndDecrement();
   }
}