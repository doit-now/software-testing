/**
 *
 * @author giao-lang | fb/giao.lang.bis | github.com/doit-now
 * version 20.09
 */

package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyExceptionTest {

    //Trong JUnit 5 để test trường hợp hàm có ném ra ngoại lệ như đã dự tính hay ko
    //ta sẽ dùng hàm org.junit.jupiter.api.Assertions.assertThrows()
    //Hàm này cần tham số là Exception class bạn muốn thấy xuất hiện và
    //một Executable functional interface trong đó bạn đặt code cần test (coi code này có
    //"ném" ra ngoại lệ hay ko) dưới dạng một lambda expression
    //Với JUnit 4 ta sẽ dùng đặc tính expected của @Test. Ví dụ @Test(expected = NullPointerException.class)
    
    @Test
    void testExpectedException() {

        Assertions.assertThrows(NumberFormatException.class, 
                () -> {
                         Integer.parseInt("One");
                         //TODO: câu lệnh gọi đến hàm chính cần test nằm ở đây
                         //VÀ bị bao bọc trong biểu thức Lambda
                         //Là một implementation của functional interface Executable
                         //với hàm abstract duy nhất cần implement execute()
                      });
    }
    
    @Test
    void testExpectedExceptionAndMessage() {
        
        Exception ex = Assertions.assertThrows(NumberFormatException.class, 
                () -> {
                         Integer.parseInt("One");                         
                      });
        
        //Kiểm tra coi có "ném" ra thông báo lỗi đúng như kì vọng hem?
        String expectedMessage = "For input string";
        String actualMessage = ex.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

}
