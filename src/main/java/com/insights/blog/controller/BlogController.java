package com.insights.blog.controller;

import com.insights.blog.model.Image;
import com.insights.blog.model.User;
import com.insights.blog.payload.BlogRequestDTO;
import com.insights.blog.payload.BlogResponseDTO;
import com.insights.blog.payload.ImageModelDTO;
import com.insights.blog.repository.ImageRepository;
import com.insights.blog.security.CurrentUser;
import com.insights.blog.service.BlogService;
import com.insights.blog.service.cloud.ImageService;
import com.insights.blog.service.cloud.ImageServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class BlogController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    private final BlogService blogService;
    @Autowired
    private ImageServiceImpl imageServiceImpl;

    @GetMapping("/all")
    public ResponseEntity<Page<BlogResponseDTO>> getAllPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String query) {
        Page<BlogResponseDTO> postPage = blogService.getAllPosts(page, query);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    @GetMapping("/get/blog/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BlogResponseDTO> getBlog(@PathVariable Integer id) {
        return new ResponseEntity<>(blogService.getBlogById(id), HttpStatus.OK);
    }

    @GetMapping("/get/blog/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<BlogResponseDTO>> getBlog(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String query, @CurrentUser User currentUser) {
        Page<BlogResponseDTO> postPage = blogService.getBlogsByUser(page, query, currentUser);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    @PostMapping("/create/blog")
    @PreAuthorize("hasRole('USER')")
    public BlogResponseDTO createBlog(
            @RequestPart("blog") BlogRequestDTO blogRequestDTO,
            @CurrentUser User currentUser,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        ImageModelDTO imageModelDTO = new ImageModelDTO();
        imageModelDTO.setImageFile(imageFile);
        if (imageFile != null && !imageFile.isEmpty()) {
            System.out.println("Image file received: " + imageFile.getOriginalFilename());
        } else {
            System.out.println("No image file received");
        }
        // Then add the blog
        return blogService.addBlog(blogRequestDTO, currentUser, imageModelDTO);
    }

    @DeleteMapping("/delete/blog/{id}")
    @PreAuthorize("hasRole('USER')")
    public boolean deleteBlog(@PathVariable Integer id) {
        imageServiceImpl.deleteImagesByBlogId(id);
        return blogService.deleteBlog(id);
    }

    @PutMapping("/update/blog/{id}")
    @PreAuthorize("hasRole('USER')")
    public BlogResponseDTO updateBlog(@PathVariable Integer id, @RequestPart("blog") BlogRequestDTO blogRequestDTO, @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        ImageModelDTO imageModelDTO = new ImageModelDTO();
        imageModelDTO.setImageFile(imageFile);
        System.out.println("Received create blog request with following details:");
        System.out.println("Blog Request DTO: " + blogRequestDTO);
        if (imageFile != null && !imageFile.isEmpty()) {
            System.out.println("Image file update received: " + imageFile.getOriginalFilename());
        } else {
            System.out.println("No image file update received");
        }

        return blogService.updateBlog(id, blogRequestDTO, imageModelDTO);
    }

//update init
}
