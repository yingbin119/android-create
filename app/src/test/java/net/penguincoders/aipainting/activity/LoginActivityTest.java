//package net.penguincoders.aipainting.activity;
//
//import net.penguincoders.aipainting.reponse.UserResultResponse;
//import net.penguincoders.aipainting.util.ApiService;
//import net.penguincoders.aipainting.util.RetrofitClient;
//import net.penguincoders.aipainting.util.UserResult;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LoginActivityTest {
//
//    private LoginActivity loginActivity;
//    private ApiService apiService;
//
//    @Before
//    public void setUp() {
//        // 在测试之前初始化 LoginActivity 实例和 ApiService 实例
//        loginActivity = new LoginActivity();
//        apiService = RetrofitClient.getClient().create(ApiService.class);
//    }
//
//    @Test
//    public void testPerformLogin_Success() {
//        // 模拟成功的响应数据
//        UserResultResponse successResponse = new UserResultResponse();
//        successResponse.setResult(UserResult.SUCCESS);
//
//        // 模拟 Call 对象
//        Call<UserResultResponse> call = new Call<UserResultResponse>() {
//            @Override
//            public Response<UserResultResponse> execute() throws IOException {
//                return Response.success(successResponse);
//            }
//
//            @Override
//            public void enqueue(Callback<UserResultResponse> callback) {
//                try {
//                    callback.onResponse(this, execute());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            // 其他未实现的方法...
//        };
//
//        // 使用模拟的 ApiService 返回模拟的 Call 对象
//        RetrofitClient.setApiServiceForTesting(apiService, call);
//
//        // 调用 performLogin 方法
//        loginActivity.performLogin("testAccount", "testPassword");
//
//        // 在这里可以添加断言来验证登录成功后的期望行为
//    }
//}
