/**
 *
 * @author giao-lang | fb/giao.lang.bis | github.com/doit-now
 * version 20.09
 */

package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class MyParameterizedTest {
    
    //Kho dữ liệu dùng cho việc test các hàm cần test
    //Dữ liệu này được đưa vào các hàm cần test dưới dạng tham số đầu vào - parameter
    //Hàm này phải là static
    public static Integer[][] initDataset() {
        return new Integer[][]{{20, 10}, {10, 10}, {2, 2}};
    }
    
    @ParameterizedTest
    @MethodSource("initDataset")         
    void testWithParameters(int expected, int input) {
        assertEquals(expected, input);
        //TODO: thay bằng các lệnh gọi các hàm cần test ở đây!!!
    }

}
