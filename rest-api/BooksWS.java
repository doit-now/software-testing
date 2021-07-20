package com.giaolang.bookstore.ws;

import com.giaolang.bookstore.dto.Book;
import com.giaolang.bookstore.dao.BookDAO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 *
 * © giáo.làng | fb/giao.lang.bis | FPT University - HCMC Campus
 *             | https://www.youtube.com/channel/UChsPO5CLUjOWfgwjfC2Y-Wg
 * Version 21.07
 * Inspired by various resources in Internet...
 */

// QUY ƯỚC SỬ DỤNG API
// http(s)://tên-miền:port/tên-app/URL-pattern-in-web.xml/path-trỏ-tới-tài-nguyên
// http(s)://localhost:6969/giaolang-bookstore/books
    
@Path("/books")
@Tag(name = "[giao.lang] Bookstore service", 
    description = "CRUD operations for bookstore service. Some resources are authentication required (TBA)")
public class BooksWS {

    // Cầu nối tới CSDL
    private BookDAO dao = BookDAO.getInstance(); // Sử dụng Singleton design pattern

    @Context
    UriInfo ui; // Inject, cần chút info từ phía web server 
    
    // Endpoint-service #1.1: Liệt kê tất cả các cuốn sách đang có trong kho
    // URL 1: http://localhost:6969/giaolang-bookstore/api/books
    
    // Endpoint-service #1.2: Liệt kê tất cả các cuốn sách đang có trong kho
    //                        của tác giả XXX  
    //                        KĨ THUẬT QUERY STRING
    // URL 2: http://localhost:6969/giaolang-bookstore/api/books?author=tên-tác-giả
    
    @GET // Mặc định GET sẽ gọi method này
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all of books", description = "No authentication required!",
               responses = {@ApiResponse(responseCode = "200", description = "Get all of books",    
                                         content = @Content(mediaType = "application/json",
                                         array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
                            @ApiResponse(responseCode = "204", description = "Book(s) not found")
                           })
    public List<Book> getAll(@QueryParam("author") String author) {
        if (author == null)
            return dao.getAll();
        else
            return dao.getAllByAuthor(author);
    }

////////////////////////////////////////////////////////////  
//    @GET // Mặc định GET sẽ gọi method này
//    @Produces(MediaType.APPLICATION_JSON)    
//    public List<Book> getAll() {   
//        Hard-code thử nghiệm coi có chạy được không khi chưa chơi với CSDL         
//        List<Book> books = new ArrayList();
//        books.add(new Book("2518407786529", "The Alchemist (Nhà giả kim)", "Paulo Coelho", 1, 2013));
//        books.add(new Book("6911225907262", "Tuổi Trẻ Đáng Giá Bao Nhiêu", "Rosie Nguyễn", 2, 2018));
//        return books;
//    }
////////////////////////////////////////////////////////////    
    
    // Endpoint-service #2: Tìm cuốn sách có mã-số-isbn-xxx
    // URL: http://localhost:6969/giaolang-bookstore/api/books/mã-số-sách-muốn-tìm   
  
    @GET
    @Path("{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a book by isbn", description = "No authentication required!",
               responses = {
                            @ApiResponse(responseCode = "200", description = "Return a selected book", 
                                         content = @Content(mediaType = "application/json", 
                                         schema = @Schema(implementation = Book.class))),
                            @ApiResponse(responseCode = "204", description = "Book not found")
                           })
    public Response getOne(@PathParam("isbn") String isbn) {

        Book book = dao.getOne(isbn);
        if (book != null) {
            return Response.ok(book, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    // Endpoint-service #3: Thêm mới một cuốn sách
    // URL: POST http://localhost:6969/giaolang-bookstore/api/books/
    //           PHẢI gửi kèm theo HTTP Request Body một JSON object
    //           mang thông về một cuốn sách muốn thêm mới, ko trùng isbn  
    //           CHÈN THÀNH CÔNG trả về URL trỏ đến cuốn sách mới vừa thêm
    //           có thể dùng GET /mã-sách-vừa-thêm để kiểm tra lại 
    
    @POST
    //@Path("create")  XÀI CHUNG URL GET ALL
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Insert a new book", 
        responses = {
             @ApiResponse(description = "Insert a new given book", 
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = Book.class))),
             @ApiResponse(responseCode = "201", description = "Book created successfully"),
             @ApiResponse(responseCode = "406", description = "Book created unsuccessfully") 
         })
    public Response addOne(Book book) throws URISyntaxException {

        String newIsbn = dao.addOne(book);
        URI uri = new URI(ui.getBaseUri() + "books/" + newIsbn);
        if (newIsbn != null) {
            return Response.created(uri).build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    //Service #4: Cập nhật thông tin một cuốn sách có sẵn trong CSDL
    // URL: PUT http://localhost:6969/giaolang-bookstore/api/books/mã-số-sách-muốn-cập-nhật
    //          PHẢI gửi kèm HTTP Request Body một JSON object về một cuốn sách muốn cập nhật nội dung  
    //          trả về thành công hoặc thất bại 
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{isbn}")
    public Response updateOne(@PathParam("isbn") String isbn, Book book) {

        book.setIsbn(isbn);
        if (dao.updateOne(book)) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }

    //Service #5: Xóa một cuốn sách có sẵn trong CSDL
    // URL: DELETE http://localhost:6969/giaolang-bookstore/api/books/mã-số-sách-muốn-xóa
    //      trả về thành công hoặc thất bại 
    
    @DELETE
    @Path("{isbn}")
    public Response deleteOne(@PathParam("isbn") String isbn) {
        
        if (dao.deleteOne(isbn)) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }    
}
