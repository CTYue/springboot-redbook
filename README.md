In this branch,
1. add new dependencies in pom.
```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>2.4.5</version>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## ModelMapper
1. add package config and the code
2. in service layer, use modelMapper to replace mapToDto and mapToEntity 
   * PostServiceImpl 
   * CommentServiceImpl

## Fix Comments
* com.chuwa.redbook.payload.PostDto
  *     private Set<CommentDto> comments;
* com.chuwa.redbook.entity
  * ```java
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    ```

## Exception Handling
* com.chuwa.redbook.payload.ErrorDetails
* com.chuwa.redbook.exception.GlobalExceptionHandler
