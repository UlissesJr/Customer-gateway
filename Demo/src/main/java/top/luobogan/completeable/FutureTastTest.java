package top.luobogan.completeable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by LuoboGan
 * Date:2023-05-26
 */
public class FutureTastTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建任务T2的FutureTask
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());
        //创建任务T1的FutureTask
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));

        Thread T1  = new Thread(ft2);
        Thread T2  = new Thread(ft1);

        T1.start();
        T2.start();

        // 等待返回的结果
        System.out.println(ft1.get());
    }

}

/**
 * T2 任务： 洗茶壶，洗茶杯，拿茶叶
 */
class T2Task implements Callable<String>{

    @Override
    public String call() throws Exception {
        System.out.println("T2 洗茶壶.... ");
        TimeUnit.SECONDS.sleep(1);

        System.out.println("T2 洗茶杯.... ");
        TimeUnit.SECONDS.sleep(2);

        System.out.println("T2 拿茶叶.... ");
        TimeUnit.SECONDS.sleep(1);

        return "红茶";
    }
}

/**
 * T1 任务： 洗水壶，烧开水，泡茶
 */
class T1Task implements Callable<String>{

    FutureTask<String> ft2;

    T1Task(FutureTask<String> ft2){
        this.ft2 = ft2;
    }

    @Override
    public String call() throws Exception {
        System.out.println("T1 洗水壶.... ");
        TimeUnit.SECONDS.sleep(1);

        System.out.println("T1 烧开水.... ");
        TimeUnit.SECONDS.sleep(15);

        // 获取T2 线程的茶叶
        String res = ft2.get();
        System.out.println("T1 拿到茶叶....." + res);

        System.out.println("T1 泡茶.... ");
        TimeUnit.SECONDS.sleep(15);

        return "喝茶 " + res;
    }
}

