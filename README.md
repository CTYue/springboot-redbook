
# springboot-redbook `branch 04_comment`#

In this branch, we add a new table `comments` which has a one to many relationshep with the `posts` table.


> ## In pakage `entity`, we added `Comment.java` where we declare how the table should look like and what is the relationship with posts.
At this file, we start with adding two annotation just like the `posts`, then we listed out all the columns the table should have. The only different is `Post` as foreign object from anther table, we named it "post_id" and `hibernate` will help take case with the rest and only show its id in the table, but  we need to notice that when we got a row of `comment` from the table in the server, we are actually getting the actual object of a post along with the comment(ManyToOne relationship, each comment has to correspond to a post).
```
@Entity
@Table(name = "comments")
...

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
```


> ## In pakage `controller`, added `commentController.java` where we specify every URL path for the APIs. 

###  `@RequestBody` is used mainly for saving objects

###  `@PathVariable`is used mainly for getting individual objects

For example: `GET http://localhost:8080/api/v1/posts/2/comments/3` 

Here we pass 2 and 3 as path variable. This will supposedly change the comment with postId == 2 and comment id == 3, with request body having following attributes:
```
body:
{
    "name": "XXX",
    "email": "XXX@gmail.com",
    "body": "Anohther new comment to discuess if Richard is Richy"
}
```
```java
@PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "id") Long commentId,
                                                    @RequestBody CommentDto commentDto) {
        CommentDto updateComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updateComment, HttpStatus.OK);
    }
```

###  `@RequestParam`is used mainly for filtering purposes. 

For example: `GET http://localhost:8080/api/v1/posts?pageNo=0&pageSize=2`

Here we pass `pageNo=0` and `pageSize=2` as request parameters. This will be used mainly for GET operation.


```java
@GetMapping()
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        return postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
    }
```


## Other than this two files
we also need to add a new file to all the other pakege in order to completed the APIs for the `comments` table. And add BlogAPIException in package exception


