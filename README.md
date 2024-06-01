<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="https://i.postimg.cc/rmY4gqT4/image.png" alt="Thumbnail Image">
  </a>

<h1 align = "center">
<b><i>xJournal</i></b>
</h1>

  <p align="center">
    Ứng dụng Android kiến trúc MVVM 
    <br />


  
[Ảnh chụp màn hình](#camera_flash:**Ảnh_chụp_màn_hình**:camera_flash) ~
[Thông số](#arrow_lower_right:**Thông_số**:arrow_lower_right) ~
[Kiến trúc](#hammer_and_wrench:**Kiến_trúc**:hammer_and_wrench) ~
[Công nghệ](#building_construction:**Công_nghệ**:building_construction) ~ 
 
</div>
    
xJournal cho phép người dùng viết và lưu các mục nhật ký với tùy chọn có thể bao gồm ảnh và biểu cảm được thể hiện bởi biểu tượng cảm xúc.
xJournal được xây dựng theo kiến trúc **Model-View-ViewModel** (MVVM) của Android Clean Architecture .

# :camera_flash:**Ảnh_chụp_màn_hình**:camera_flash:

xJournal sử dụng Material 3 để có giao diện người dùng nhất quán:

<p align="center">
<img src="https://i.postimg.cc/bJMWpRmG/x-Journal-Demo.png" alt="Thumbnail Image">
<!-- <img img width="200" height="400" src="./readme-assets/screenshots/screen_1.png"> &nbsp;&nbsp;&nbsp;&nbsp;
<img img width="200" height="400" src="./readme-assets/screenshots/screen_2.png"> &nbsp;&nbsp;&nbsp;&nbsp;   
<img img width="200" height="400" src="./readme-assets/screenshots/screen_3.png"> &nbsp;&nbsp;&nbsp;&nbsp;  -->

</p>

<img src="https://i.postimg.cc/MGFmKts1/8-Ki-n-tr-c-v-ph-n-chia-ch-c-n-ng-Howkteam-vn.png" alt="Thumbnail ">
Tầng giao diện (Presentation layer)
Tầng xử lý nghiệp vụ (Application processing layer)
Tầng truy cập dữ liệu (Data management layer)

MVVM chia Lớp giao diện người dùng thành hai lớp bổ sung - Chế độ View và ViewModel.


# :arrow_lower_right:**Thông_số**:arrow_lower_right:
Các thông số chính của xJournal

| Thông Số       | Giá trị |
|----------------|---------|
| compileSdk     | 34      |
| targetSdk      | 34      |
| minSdk         | 26      |
| composeVersion | 1.5.1   |
| kotlinVersion  | 1.8.0   |

Để build và chạy ứng dụng, bạn cần cài đặt phiên bản Android Studio Iguana | 2023.2.1 hoặc mới hơn
# :hammer_and_wrench:**Kiến_trúc**:hammer_and_wrench:

### Điều Hướng
Ứng dụng có tổng cộng :three: màn hình đích sử dụng Compose để quản lý điều hướng.

| :tính năng:xác thực                | :tính năng:home                       | :tính năng:viết nhật ký           |
|-------------------------------------|-------------------------------------|-------------------------------------|
| <img src = "https://i.postimg.cc/KY5HQQkk/auth.jpg"> | <img src = "https://i.postimg.cc/4yY2fg8V/home.jpg"> | <img src = "https://i.postimg.cc/3rqbLkRB/write.jpg"> |


 - **Tính năng Xác thực**: Tính năng này tập trung vào việc xác thực và xác minh người dùng. Nó sử dụng Đăng nhập Google để đảm bảo rằng người dùng có thể truy cập an toàn vào các mục nhật ký của họ. Bằng cách xác thực người dùng, ứng dụng đảm bảo rằng chỉ những cá nhân được ủy quyền mới có thể tương tác với các nhật ký cá nhân của họ.

 - **Tính năng Trang chủ**: Tính năng Trang chủ chịu trách nhiệm hiển thị và lọc các mục nhật ký dựa trên ngày tháng. Nó cung cấp một giao diện thân thiện với người dùng để điều hướng qua các mục nhật ký và nhanh chóng lọc nhật ký theo các ngày cụ thể. Các lựa chọn bổ sung có thể được truy cập thông qua Ngăn Điều hướng.

 - **Tính năng Viết**: Tính năng Viết cho phép người dùng tạo các mục nhật ký mới hoặc chỉnh sửa các mục hiện có. Nó cung cấp một giao diện liền mạch và trực quan để người dùng ghi lại và tài liệu hóa những suy nghĩ, khoảnh khắc và kỷ niệm của họ. xJournal cho phép người dùng cá nhân hóa nội dung bằng cách thêm biểu tượng cảm xúc và hình ảnh kèm theo.


# :building_construction:**Công_nghệ**:building_construction:

Project xJournal sử dụng nhiều thư viện và công cụ phổ biến trong Hệ sinh thái Android:

* [Jetpack Compose](https://developer.android.com/jetpack/compose) - bộ công cụ hiện đại để xây dựng giao diện người dùng Android gốc.
* [Android KTX](https://developer.android.com/kotlin/ktx) - giúp viết mã Kotlin ngắn gọn và thành ngữ hơn.

* [Coroutines and Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines-overview.html) - được sử dụng để quản lý bộ nhớ cục bộ, tức là `ghi vào và đọc từ cơ sở dữ liệu`. Coroutine giúp quản lý các luồng nền và giảm nhu cầu gọi lại.
* [Material Design 3](https://m3.material.io/) - một hệ thống hướng dẫn, thành phần và công cụ có thể thích ứng để hỗ trợ hiển thị tốt nhất về thiết kế giao diện người dùng.
* [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - điều hướng giữa các thành phần kết hợp trong khi tận dụng các trạng thái của NavController giúp theo dõi ngăn xếp của các thành phần kết hợp tạo nên màn hình trong ứng dụng.
* [Dagger Hilt](https://dagger.dev/hilt/) - dùng cho Dependency Injection.
* [SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen) - API SplashScreen cho phép ứng dụng khởi chạy kèm theo hoạt ảnh, bao gồm chuyển động trong ứng dụng khi khởi chạy, màn hình chờ hiển thị biểu tượng ứng dụng của bạn và quá trình chuyển đổi sang chính ứng dụng của bạn.
* [Retrofit](https://square.github.io/retrofit/) - Ứng dụng client HTTP an toàn dành cho Android.
* [OkHttp3](https://square.github.io/okhttp/) - Một thư viện cho phép kết nối internet sử dụng giao thức Http một cách dễ dàng và nhanh chóng hơn.
* [Stevdza-San's MessageBarCompose](https://github.com/stevdza-san/MessageBarCompose) - Giao diện người dùng Thanh thông báo hoạt ảnh có thể được bao quanh nội dung màn hình của bạn để hiển thị thông báo Lỗi/Thành công trong ứng dụng của bạn. Nó được điều chỉnh và tối ưu hóa để sử dụng với các dự án Compose và Material 3.

* [Stevdza-San's OneTapCompose](https://github.com/stevdza-san/OneTapCompose) - Giao diện người dùng Thanh thông báo hoạt ảnh có thể được bao quanh nội dung màn hình của bạn để hiển thị thông báo Lỗi/Thành công trong ứng dụng của bạn. Nó được điều chỉnh và tối ưu hóa để sử dụng với các dự án Compose và Material 3.
